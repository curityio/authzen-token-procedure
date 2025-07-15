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
import se.curity.identityserver.sdk.attribute.token.AccessTokenAttributes;
import se.curity.identityserver.sdk.data.tokens.TokenIssuerException;
import se.curity.identityserver.sdk.procedure.token.IntrospectionApplicationJwtTokenProcedure;
import se.curity.identityserver.sdk.procedure.token.context.IntrospectionTokenProcedurePluginContext;
import se.curity.identityserver.sdk.web.ResponseModel;

import java.util.HashMap;


public final class AuthZENIntrospectionApplicationJwtTokenProcedure implements IntrospectionApplicationJwtTokenProcedure
{
    private final AuthZENTokenProcedureConfig _configuration;

    public AuthZENIntrospectionApplicationJwtTokenProcedure(AuthZENTokenProcedureConfig configuration)
    {
        _configuration = configuration;
    }

    @Override
    public ResponseModel run(IntrospectionTokenProcedurePluginContext context)
    {
        var responseData = new HashMap<String, Object>(2);
        var defaultAtJwtIssuer = context.getDefaultAccessTokenJwtIssuer();

        var delegation = context.getDelegation();

        try
        {
            if (defaultAtJwtIssuer != null && context.getPresentedToken().isActive() && delegation != null)
            {
                responseData.put("jwt", defaultAtJwtIssuer.issue(AccessTokenAttributes.of(context.getPresentedToken().getTokenData()), delegation));
                responseData.put("active", true);
            }

            return ResponseModel.mapResponseModel(responseData);
        }
        catch (TokenIssuerException e)
        {
            return ResponseModel.problemResponseModel("token_issuer_exception", "Could not issue new tokens");
        }
    }
}
