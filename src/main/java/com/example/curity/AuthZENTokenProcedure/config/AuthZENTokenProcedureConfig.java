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
package com.example.curity.AuthZENTokenProcedure.config;

import se.curity.identityserver.sdk.config.Configuration;
import se.curity.identityserver.sdk.config.annotation.*;
import se.curity.identityserver.sdk.service.ExceptionFactory;
import se.curity.identityserver.sdk.service.HttpClient;
import se.curity.identityserver.sdk.service.issuer.AccessTokenIssuer;
import se.curity.identityserver.sdk.service.issuer.IdTokenIssuer;
import se.curity.identityserver.sdk.service.issuer.NonceIssuer;
import se.curity.identityserver.sdk.service.issuer.RefreshTokenIssuer;

public interface AuthZENTokenProcedureConfig extends Configuration
{
    ExceptionFactory getExceptionFactory();

    @DefaultService
    AccessTokenIssuer getAccessTokenIssuer();

    @DefaultService
    RefreshTokenIssuer getRefreshTokenIssuer();

    @DefaultService
    IdTokenIssuer getIdTokenIssuer();

    @DefaultService
    NonceIssuer getAuthorizationCodeIssuer();

    @Name("PDP-Configuration")
    PDPConfig getPdpConfig();

    @Name("Authorization-Configuration")
    AuthzConfig getAuthzConfig();

    interface AuthzConfig extends Configuration
    {
        @Description("Enable authorization for scope(s) requested by the client.")
        @DefaultBoolean(false)
        boolean getAuthorizeScope();

        @Description("Enable authorization for client used.")
        @DefaultBoolean(false)
        boolean getAuthorizeClient();
    }

    interface PDPConfig extends Configuration
    {
        @Description("The Http Client used to communicate with the PDP.")
        HttpClient getHttpClient();

        @Description("The hostname of the PDP")
        @DefaultString("localhost")
        @Name("PDP-Host")
        String getPDPHost();

        @Description("The port of the PDP the authorization request is sent to.")
        @DefaultString("443")
        @Name("PDP-Port")
        String getPDPPort();

        @Description("The path of the PDP the authorization request is sent to.")
        @DefaultString("/access/v1/evaluation")
        @Name("PDP-Path")
        String getPDPPath();
    }
}
