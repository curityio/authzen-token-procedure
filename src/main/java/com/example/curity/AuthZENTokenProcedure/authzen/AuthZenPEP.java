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
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.curity.identityserver.sdk.Nullable;
import se.curity.identityserver.sdk.http.HttpResponse;
import se.curity.identityserver.sdk.procedure.token.context.GrantTypeTokenProcedurePluginContext;
import se.curity.identityserver.sdk.service.ExceptionFactory;
import se.curity.identityserver.sdk.service.HttpClient;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static se.curity.identityserver.sdk.http.HttpRequest.fromString;

public class AuthZenPEP
{
    private static final Logger _logger = LoggerFactory.getLogger(AuthZenPEP.class);
    private final AuthZENTokenProcedureConfig _config;
    private final HttpClient _pdpClient;
    private final ExceptionFactory _exceptionFactory;
    private final String _requestedScopes;
    private final URI pdpURI;

    @Nullable
    private final String _clientId;

    public AuthZenPEP(AuthZENTokenProcedureConfig config, GrantTypeTokenProcedurePluginContext context)
    {
        _config = config;
        _pdpClient = config.getPdpConfig().getHttpClient();
        _exceptionFactory = config.getExceptionFactory();
        _requestedScopes = context.getScope();
        _clientId = context.getClient().getId();

        try
        {
            pdpURI = new URI(String.format("%s://%s:%s%s",
                    _pdpClient.getScheme(),
                    _config.getPdpConfig().getPDPHost(),
                    _config.getPdpConfig().getPDPPort(),
                    _config.getPdpConfig().getPDPPath()));
        }
        catch (URISyntaxException e)
        {
            throw _exceptionFactory.configurationException("PDP URI is incorrectly configured");
        }
    }

    public boolean getAuthorization(String subject, String method, String resource)
    {
        _logger.error("CLAIM NAMES: {}", _clientId);

        String authZenRequestBody = AuthZenHelper.getAuthZenRequest(
                _config,
                subject,
                method,
                resource,
                _requestedScopes,
                _clientId
                );

        HttpResponse pdpResponse = _pdpClient.request(pdpURI)
                .contentType("application/json")
                .body(fromString(authZenRequestBody, StandardCharsets.UTF_8))
                .post()
                .response();

        if (pdpResponse.statusCode() != 200)
        {
            _logger.debug("PDP Http Status code {}", pdpResponse.statusCode());
            return false;
        }

        JSONObject pdpResponseBody = new JSONObject(pdpResponse.body(HttpResponse.asString()));

        boolean decision = pdpResponseBody.optBoolean("decision");

        if (decision == Boolean.TRUE)
        {
            return true;
        }
        else if(decision == Boolean.FALSE)
        {
            return false;
        }

        _logger.debug("Unable to determine decision, returning Deny");

        return false;
    }
}
