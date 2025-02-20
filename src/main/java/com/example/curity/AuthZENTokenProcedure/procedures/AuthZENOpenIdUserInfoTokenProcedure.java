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
import se.curity.identityserver.sdk.procedure.token.OpenIdUserInfoTokenProcedure;
import se.curity.identityserver.sdk.procedure.token.context.OpenIdUserInfoTokenProcedurePluginContext;
import se.curity.identityserver.sdk.web.ResponseModel;

public final class AuthZENOpenIdUserInfoTokenProcedure implements OpenIdUserInfoTokenProcedure
{
    private final AuthZENTokenProcedureConfig _configuration;

    public AuthZENOpenIdUserInfoTokenProcedure(AuthZENTokenProcedureConfig configuration)
    {
        _configuration = configuration;
    }

    @Override
    public ResponseModel run(OpenIdUserInfoTokenProcedurePluginContext context)
    {
        var responseData = context.getDefaultResponseData().asMap();

        var name = context.getAccountAttributes().getName();
        if (name != null)
        {
            var formattedName = name.getFormatted();
            if (formattedName != null && !formattedName.isEmpty())
            {
                responseData.put("name", formattedName);
            }
        }

        var presentedTokenData = context.getPresentedToken().getTokenData();

        responseData.put("scope", presentedTokenData.getMandatoryValue("scope", String.class));

        var delegation = context.getPresentedToken().getTokenDelegation();
        if (delegation != null)
        {
            responseData.put("client_id", delegation.getClientId());
        }

        var accountAttributes = context.getAccountAttributes();
        if (accountAttributes != null)
        {
            responseData.put("preferred_username", accountAttributes.getUserName());
        }

        return ResponseModel.mapResponseModel(responseData);
    }
}
