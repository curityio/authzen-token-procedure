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

import com.example.curity.AuthZENTokenProcedure.config.AuthZENTokenProcedureConfig;
import se.curity.identityserver.sdk.data.tokens.TokenIssuerException;
import se.curity.identityserver.sdk.procedure.token.TokenExchangeTokenProcedure;
import se.curity.identityserver.sdk.procedure.token.context.TokenExchangeTokenProcedurePluginContext;
import se.curity.identityserver.sdk.service.issuer.AccessTokenIssuer;
import se.curity.identityserver.sdk.web.ResponseModel;

import java.time.Instant;
import java.util.HashMap;

public final class AuthZENTokenExchangeTokenProcedure implements TokenExchangeTokenProcedure
{
    private final AuthZENTokenProcedureConfig _configuration;
    private final AccessTokenIssuer accessTokenIssuer;

    public AuthZENTokenExchangeTokenProcedure(AuthZENTokenProcedureConfig configuration)
    {
        _configuration = configuration;
        accessTokenIssuer = _configuration.getAccessTokenIssuer();
    }

    @Override
    public ResponseModel run(TokenExchangeTokenProcedurePluginContext context)
    {
        var accessTokenData = context.getDefaultAccessTokenData(context.getDelegation());
        try
        {
            var issuedAccessToken = accessTokenIssuer.issue(accessTokenData, context.getDelegation());

            var responseData = new HashMap<String, Object>(4);
            responseData.put("scope", accessTokenData.getScope());
            responseData.put("access_token", issuedAccessToken);
            responseData.put("token_type", "bearer");
            responseData.put("expires_in", accessTokenData.getExpires().getEpochSecond() - Instant.now().getEpochSecond());

            return ResponseModel.mapResponseModel(responseData);
        }
        catch (TokenIssuerException e)
        {
            return ResponseModel.problemResponseModel("token_issuer_exception", "Could not issue new tokens");
        }
    }
}
