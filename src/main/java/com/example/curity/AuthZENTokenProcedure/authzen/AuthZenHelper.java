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
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthZenHelper
{
    private static final String SUBJECT = "subject";
    private static final String ACTION = "action";
    private static final String RESOURCE = "resource";
    private static final String CONTEXT = "context";
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

        JSONObject subjectAttributes = new JSONObject();
        subjectAttributes.put("type", _type).put("id", subject);

        JSONObject resourceAttributes = new JSONObject();
        resourceAttributes.put("type", "api").put("id", resource);

        /*
        If scope authorization is enabled add the requested scope(s) to the authorization request
         */
        if (_config.getAuthzConfig().getAuthorizeScope()) {
            resourceAttributes.put("properties", new JSONObject().put("scope", scope));
        }
        /*
        If clientID authorization is enabled add the clientID to the authorization request
         */
        if (_config.getAuthzConfig().getAuthorizeClient()) {

            if( resourceAttributes.has("properties") ) {
                resourceAttributes.getJSONObject("properties").put("clientId", clientId);
            }
            else
            {
                resourceAttributes.put("properties", new JSONObject().put("clientId", clientId));
            }
        }

        JSONObject actionAttributes = new JSONObject()
        .put("name", "can_issue")
                .put("properties", new JSONObject()
                        .put("method", action));

        return new JSONObject().put(SUBJECT, subjectAttributes)
                .put(RESOURCE, resourceAttributes)
                .put(ACTION, actionAttributes)
                .toString();
    }
}
