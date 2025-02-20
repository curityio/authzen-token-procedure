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
import se.curity.identityserver.sdk.procedure.token.RefreshTokenProcedure;
import se.curity.identityserver.sdk.procedure.token.context.RefreshTokenProcedurePluginContext;
import se.curity.identityserver.sdk.service.issuer.AccessTokenIssuer;
import se.curity.identityserver.sdk.service.issuer.RefreshTokenIssuer;
import se.curity.identityserver.sdk.web.ResponseModel;
import java.time.Instant;
import java.util.HashMap;

public final class AuthZENRefreshTokenProcedure implements RefreshTokenProcedure
{
    private final AuthZENTokenProcedureConfig _configuration;
    private final AccessTokenIssuer accessTokenIssuer;
    private final RefreshTokenIssuer refreshTokenIssuer;

    public AuthZENRefreshTokenProcedure(AuthZENTokenProcedureConfig configuration)
    {
        _configuration = configuration;
        accessTokenIssuer = _configuration.getAccessTokenIssuer();
        refreshTokenIssuer = _configuration.getRefreshTokenIssuer();
    }

    @Override
    public ResponseModel run(RefreshTokenProcedurePluginContext pluginContext)
    {
        var accessTokenData = pluginContext.getDefaultAccessTokenData(pluginContext.getDelegation());
        String issuedAccessToken = null;
        try
        {
            issuedAccessToken = accessTokenIssuer.issue(accessTokenData, pluginContext.getDelegation());
            var refreshToken = pluginContext.getPresentedToken().getValue();

            if (refreshToken == null)
            {
                var refreshTokenData = pluginContext.getDefaultRefreshTokenData();
                refreshToken = refreshTokenIssuer.issue(refreshTokenData, pluginContext.getDelegation());
            }

            var responseMap = new HashMap<String, Object>(5);
            responseMap.put("scope", accessTokenData.getScope());
            responseMap.put("access_token", issuedAccessToken);
            responseMap.put("refresh_token", refreshToken);
            responseMap.put("token_type", "bearer");
            responseMap.put("expires_in", accessTokenData.getExpires().getEpochSecond() - Instant.now().getEpochSecond());

            return ResponseModel.mapResponseModel(responseMap);
        }
        catch (TokenIssuerException e)
        {
            return ResponseModel.problemResponseModel("token_issuer_exception", "Could not issue new tokens");
        }
    }
}
