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
package com.example.curity.AuthZENTokenProcedure.descriptor;

import com.example.curity.AuthZENTokenProcedure.config.AuthZENTokenProcedureConfig;
import com.example.curity.AuthZENTokenProcedure.procedures.*;
import se.curity.identityserver.sdk.plugin.descriptor.TokenProcedurePluginDescriptor;
import se.curity.identityserver.sdk.procedure.token.AssertionTokenProcedure;
import se.curity.identityserver.sdk.procedure.token.AssistedTokenProcedure;
import se.curity.identityserver.sdk.procedure.token.AuthorizationCodeTokenProcedure;
import se.curity.identityserver.sdk.procedure.token.AuthorizeCodeTokenProcedure;
import se.curity.identityserver.sdk.procedure.token.AuthorizeImplicitTokenProcedure;
import se.curity.identityserver.sdk.procedure.token.BackchannelAuthenticationTokenProcedure;
import se.curity.identityserver.sdk.procedure.token.ClientCredentialsTokenProcedure;
import se.curity.identityserver.sdk.procedure.token.DeviceAuthorizationTokenProcedure;
import se.curity.identityserver.sdk.procedure.token.DeviceCodeTokenProcedure;
import se.curity.identityserver.sdk.procedure.token.IntrospectionApplicationJwtTokenProcedure;
import se.curity.identityserver.sdk.procedure.token.IntrospectionTokenProcedure;
import se.curity.identityserver.sdk.procedure.token.OpenIdAuthorizeEndpointHybridTokenProcedure;
import se.curity.identityserver.sdk.procedure.token.OpenIdUserInfoTokenProcedure;
import se.curity.identityserver.sdk.procedure.token.RefreshTokenProcedure;
import se.curity.identityserver.sdk.procedure.token.RopcTokenProcedure;
import se.curity.identityserver.sdk.procedure.token.TokenExchangeTokenProcedure;

public final class AuthZENTokenProcedureDescriptor implements TokenProcedurePluginDescriptor<AuthZENTokenProcedureConfig>
{
    @Override
    public Class<? extends RefreshTokenProcedure> getOAuthTokenEndpointRefreshTokenProcedure()
    {
        return AuthZENRefreshTokenProcedure.class;
    }

//    @Override
//    public Class<? extends AuthorizeCodeTokenProcedure> getOAuthAuthorizeEndpointCodeTokenProcedure()
//    {
//        return AuthZENAuthorizeCodeTokenProcedure.class;
//    }

//    @Override
//    public Class<? extends AuthorizeImplicitTokenProcedure> getOAuthAuthorizeEndpointImplicitTokenProcedure()
//    {
//        return AuthZENAuthorizeImplicitTokenProcedure.class;
//    }

    @Override
    public Class<? extends DeviceAuthorizationTokenProcedure> getOAuthDeviceAuthorizationTokenProcedure()
    {
        return AuthZENDeviceAuthorizationTokenProcedure.class;
    }

    @Override
    public Class<? extends IntrospectionApplicationJwtTokenProcedure> getOAuthIntrospectApplicationJwtTokenProcedure()
    {
        return AuthZENIntrospectionApplicationJwtTokenProcedure.class;
    }

//    @Override
//    public Class<? extends AssertionTokenProcedure> getOAuthTokenEndpointAssertionTokenProcedure()
//    {
//        return AuthZENAssertionTokenProcedure.class;
//    }

    @Override
    public Class<? extends AuthorizationCodeTokenProcedure> getOAuthTokenEndpointAuthorizationCodeTokenProcedure()
    {
        return AuthZENAuthorizationCodeTokenProcedure.class;
    }

    @Override
    public Class<? extends IntrospectionTokenProcedure> getOAuthIntrospectTokenProcedure()
    {
        return AuthZENIntrospectionTokenProcedure.class;
    }

//    @Override
//    public Class<? extends BackchannelAuthenticationTokenProcedure> getOAuthTokenEndpointBackchannelAuthenticationTokenProcedure()
//    {
//        return AuthZENBackchannelAuthenticationTokenProcedure.class;
//    }

    @Override
    public Class<? extends ClientCredentialsTokenProcedure> getOAuthTokenEndpointClientCredentialsTokenProcedure()
    {
        return AuthZENClientCredentialsTokenProcedure.class;
    }

    @Override
    public Class<? extends DeviceCodeTokenProcedure> getOAuthTokenEndpointDeviceCodeTokenProcedure()
    {
        return AuthZENDeviceCodeTokenProcedure.class;
    }

//    @Override
//    public Class<? extends RopcTokenProcedure> getOAuthTokenEndpointRopcTokenProcedure()
//    {
//        return AuthZENRopcTokenProcedure.class;
//    }

    @Override
    public Class<? extends TokenExchangeTokenProcedure> getOAuthTokenEndpointTokenExchangeTokenProcedure()
    {
        return AuthZENTokenExchangeTokenProcedure.class;
    }

//    @Override
//    public Class<? extends OpenIdAuthorizeEndpointHybridTokenProcedure> getOpenIdAuthorizeEndpointHybridTokenProcedure()
//    {
//        return AuthZENOpenIdAuthorizeEndpointHybridTokenProcedure.class;
//    }

    @Override
    public Class<? extends OpenIdUserInfoTokenProcedure> getOpenIdUserInfoTokenProcedure()
    {
        return AuthZENOpenIdUserInfoTokenProcedure.class;
    }

//    @Override
//    public Class<? extends AssistedTokenProcedure> getOAuthAssistedTokenTokenProcedure()
//    {
//        return AuthZENAssistedTokenProcedure.class;
//    }
    
    @Override
    public String getPluginImplementationType()
    {
        return "authzen-token-procedure";
    }

    @Override
    public Class<? extends AuthZENTokenProcedureConfig> getConfigurationType()
    {
        return AuthZENTokenProcedureConfig.class;
    }    
}
