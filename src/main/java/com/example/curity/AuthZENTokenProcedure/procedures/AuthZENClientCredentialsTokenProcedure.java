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
import se.curity.identityserver.sdk.data.tokens.TokenIssuerException;
import se.curity.identityserver.sdk.procedure.token.ClientCredentialsTokenProcedure;
import se.curity.identityserver.sdk.procedure.token.context.ClientCredentialsTokenProcedurePluginContext;
import se.curity.identityserver.sdk.service.issuer.AccessTokenIssuer;
import se.curity.identityserver.sdk.web.ResponseModel;

import java.time.Instant;
import java.util.HashMap;

import static com.example.curity.AuthZENTokenProcedure.procedures.AuthZENTokenProcedureConstants.ProcedureType.CLIENT_CREDENTIALS;

public final class AuthZENClientCredentialsTokenProcedure implements ClientCredentialsTokenProcedure
{
    private final AuthZENTokenProcedureConfig _configuration;
    private final AccessTokenIssuer accessTokenIssuer;

    public AuthZENClientCredentialsTokenProcedure(AuthZENTokenProcedureConfig configuration)
    {
        _configuration = configuration;
        accessTokenIssuer = _configuration.getAccessTokenIssuer();
    }

    @Override
    public ResponseModel run(ClientCredentialsTokenProcedurePluginContext context)
    {
        var delegationData = context.getDefaultDelegationData();
        var issuedDelegation = context.getDelegationIssuer().issue(delegationData);

        var accessTokenData = context.getDefaultAccessTokenData();

        AuthZenPEP pep = new AuthZenPEP(_configuration, context);
        boolean decision = pep.getAuthorization(context.subjectAttributes().getSubject(), "POST", CLIENT_CREDENTIALS);

        try
        {
            if (decision) {
                var issuedAccessToken = accessTokenIssuer.issue(accessTokenData, issuedDelegation);

                var responseData = new HashMap<String, Object>(4);

                responseData.put("scope", accessTokenData.getScope());
                responseData.put("access_token", issuedAccessToken);
                responseData.put("token_type", "bearer");
                responseData.put("expires_in", accessTokenData.getExpires().getEpochSecond() - Instant.now().getEpochSecond());

                return ResponseModel.mapResponseModel(responseData);
            }
            throw new TokenIssuerException();
        }
        catch (TokenIssuerException e)
        {
           return ResponseModel.problemResponseModel("token_issuer_exception", "Could not issue new tokens");
        }
    }
}
