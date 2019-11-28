/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.cxf.systest.jaxrs.security.oauth2.grants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.cxf.rs.security.oauth2.common.AccessTokenRegistration;
import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.OAuthPermission;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeDataProvider;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeRegistration;
import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.apache.cxf.rs.security.oauth2.tokens.bearer.BearerAccessToken;



public class OAuthDataProviderImpl implements AuthorizationCodeDataProvider {

    private static final Map<String, String> USERS = new HashMap<String, String>() {
        {
            put("alice", "alice");
            put("consumer-id", "this-is-a-secret");
            put("consumer-id-aud", "this-is-a-secret");
        }
    };

    private static final Map<String, ServerAuthorizationCodeGrant> GRANTS =
        new ConcurrentHashMap<String, ServerAuthorizationCodeGrant>();


    public Client getClient(String clientId) throws OAuthServiceException {
        if (!USERS.containsKey(clientId)) {
            return null;
        }

        final Client client = new Client(clientId, USERS.get(clientId), true);
        client.getRedirectUris().add("http://www.blah.apache.org");
        return client;
    }

    public ServerAccessToken createAccessToken(AccessTokenRegistration accessToken)
        throws OAuthServiceException {
        return new BearerAccessToken(accessToken.getClient(), 3600);
    }

    public ServerAccessToken getAccessToken(String accessToken) throws OAuthServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    public ServerAccessToken getPreauthorizedToken(Client client, List<String> requestedScopes,
                                                   UserSubject subject, String grantType)
        throws OAuthServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    public ServerAccessToken refreshAccessToken(Client client, String refreshToken,
                                                List<String> requestedScopes) throws OAuthServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    public void removeAccessToken(ServerAccessToken accessToken) throws OAuthServiceException {
        // TODO Auto-generated method stub
        
    }

    public List<OAuthPermission> convertScopeToPermissions(Client client, List<String> requestedScope) {
        final List<OAuthPermission> perms = new ArrayList<OAuthPermission>();
        for (final String scope : requestedScope) {
            perms.add(new OAuthPermission(scope, scope));
        }

        return perms;
    }


    public ServerAuthorizationCodeGrant createCodeGrant(AuthorizationCodeRegistration reg)
        throws OAuthServiceException {
        final ServerAuthorizationCodeGrant serverAuthorizationCodeGrant =
            new ServerAuthorizationCodeGrant(reg.getClient(), 3600);

        GRANTS.put(serverAuthorizationCodeGrant.getCode(), serverAuthorizationCodeGrant);
        return serverAuthorizationCodeGrant;
    }

    public ServerAuthorizationCodeGrant removeCodeGrant(String code) throws OAuthServiceException {
        return GRANTS.get(code);
    }
}
