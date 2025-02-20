/*
 *  Copyright 2025 Curity AB
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.example.curity.AuthZENTokenProcedure.procedures;

import com.example.curity.AuthZENTokenProcedure.authzen.AuthZenPEP;
import com.example.curity.AuthZENTokenProcedure.config.AuthZENTokenProcedureConfig;
import static com.example.curity.AuthZENTokenProcedure.procedures.AuthZENTokenProcedureConstants.ProcedureType.AUTHORIZATION_CODE;

import se.curity.identityserver.sdk.attribute.Attribute;
import se.curity.identityserver.sdk.data.tokens.TokenIssuerException;

import se.curity.identityserver.sdk.procedure.token.AuthorizationCodeTokenProcedure;
import se.curity.identityserver.sdk.procedure.token.context.AuthorizationCodeTokenProcedurePluginContext;
import se.curity.identityserver.sdk.service.ExceptionFactory;
import se.curity.identityserver.sdk.web.ResponseModel;
import se.curity.identityserver.sdk.service.issuer.AccessTokenIssuer;
import se.curity.identityserver.sdk.service.issuer.IdTokenIssuer;
import se.curity.identityserver.sdk.service.issuer.RefreshTokenIssuer;

import java.util.HashMap;
import java.time.Instant;

public final class AuthZENAuthorizationCodeTokenProcedure implements AuthorizationCodeTokenProcedure
{
    private final AuthZENTokenProcedureConfig _configuration;
    private final AccessTokenIssuer accessTokenIssuer;
    private final RefreshTokenIssuer refreshTokenIssuer;
    private final IdTokenIssuer idTokenIssuer;

    public AuthZENAuthorizationCodeTokenProcedure(AuthZENTokenProcedureConfig configuration)
    {
        _configuration = configuration;
        accessTokenIssuer = _configuration.getAccessTokenIssuer();
        refreshTokenIssuer = _configuration.getRefreshTokenIssuer();
        idTokenIssuer = _configuration.getIdTokenIssuer();
    }

    @Override
    public ResponseModel run(AuthorizationCodeTokenProcedurePluginContext context)
    {
        var delegationData = context.getDefaultDelegationData();
        var issuedDelegation = context.getDelegationIssuer().issue(delegationData);
        var accessTokenData = context.getDefaultAccessTokenData();

        AuthZenPEP pep = new AuthZenPEP(_configuration, context);
        boolean decision = pep.getAuthorization(context.subjectAttributes().getSubject(), "POST", AUTHORIZATION_CODE);

        try
        {
            if(decision)
            {
                var issuedAccessToken = accessTokenIssuer.issue(accessTokenData, issuedDelegation);

                var refreshTokenData = context.getDefaultRefreshTokenData();
                var issuedRefreshToken = refreshTokenIssuer.issue(refreshTokenData, issuedDelegation);

                var responseData = new HashMap<String, Object>(6);
                responseData.put("access_token", issuedAccessToken);
                responseData.put("scope", accessTokenData.getScope());
                responseData.put("refresh_token", issuedRefreshToken);
                responseData.put("token_type", "bearer");
                responseData.put("expires_in", accessTokenData.getExpires().getEpochSecond() - Instant.now().getEpochSecond());

                var idTokenData = context.getDefaultIdTokenData();
                if (idTokenData != null) {
                    idTokenData.with(Attribute.of("at_hash", idTokenIssuer.atHash(issuedAccessToken)));

                    responseData.put("id_token", idTokenIssuer.issue(idTokenData, issuedDelegation));
                }

                return ResponseModel.mapResponseModel(responseData);
            }

            throw new TokenIssuerException();

        } catch (TokenIssuerException e) {
            return ResponseModel.problemResponseModel("token_issuer_exception", "Could not issue new tokens");
        }
    }
}
