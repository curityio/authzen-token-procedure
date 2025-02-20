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
package com.example.curity.AuthZENTokenProcedure.authzen;

import com.example.curity.AuthZENTokenProcedure.config.AuthZENTokenProcedureConfig;
import com.example.curity.AuthZENTokenProcedure.procedures.AuthZENTokenProcedureConstants.ProcedureType;
import jakarta.json.Json;
import jakarta.json.JsonObject;

public class AuthZenHelper
{
    private static String SUBJECT = "subject";
    private static String ACTION = "action";
    private static String RESOURCE = "resource";
    private static String CONTEXT = "context";
    private static AuthZENTokenProcedureConfig _config;
    private static String _type;

    public static String getAuthZenRequest(AuthZENTokenProcedureConfig config,
                                           String subject,
                                           String action,
                                           String resource,
                                           String scope,
                                           String clientId)
    {
        _config = config;

        if(resource.equals(ProcedureType.AUTHORIZATION_CODE))
        {
            _type = "user";
        }
        else if(resource.equals(ProcedureType.CLIENT_CREDENTIALS))
        {
            _type = "client";
        }

        JsonObject subjectAttributes = Json.createObjectBuilder()
                .add(SUBJECT, Json.createObjectBuilder()
                        .add("type", _type)
                        .add("id", subject))
                .build();

        JsonObject resourceAttributes = Json.createObjectBuilder()
                .add(RESOURCE, Json.createObjectBuilder()
                        .add("type", "api")
                        .add("id", resource))
                .build();

        /*
        If scope authorization is enabled add the requested scope(s) to the authorization request
         */
        if(_config.getAuthzConfig().getAuthorizeScope())
        {
            resourceAttributes.get(RESOURCE).asJsonObject().put("scope", Json.createValue(scope));
        }
        /*
        If clientID authorization is enabled add the clientID to the authorization request
         */
        if(_config.getAuthzConfig().getAuthorizeClient())
        {
            resourceAttributes.get(RESOURCE).asJsonObject().put("clientId", Json.createValue(clientId));
        }

        JsonObject actionAttributes = Json.createObjectBuilder()
                .add(ACTION, Json.createObjectBuilder()
                        .add("name", "can_issue")
                        .add("properties", Json.createObjectBuilder().add("method", action))
                )
                .build();

        return Json.createObjectBuilder()
                .add(SUBJECT, subjectAttributes.get(SUBJECT))
                .add(RESOURCE, resourceAttributes.get(RESOURCE))
                .add(ACTION, actionAttributes.get(ACTION))
                .build()
                .toString();
    }
}
