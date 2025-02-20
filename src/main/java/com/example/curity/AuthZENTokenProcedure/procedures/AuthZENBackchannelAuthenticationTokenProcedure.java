//package com.example.curity.AuthZENTokenProcedure.procedures;
//
//import com.example.curity.AuthZENTokenProcedure.config.AuthZENTokenProcedureConfig;
//import se.curity.identityserver.sdk.attribute.Attribute;
//import se.curity.identityserver.sdk.data.tokens.TokenIssuerException;
//import se.curity.identityserver.sdk.procedure.token.BackchannelAuthenticationTokenProcedure;
//import se.curity.identityserver.sdk.procedure.token.context.BackchannelAuthenticationTokenProcedurePluginContext;
//import se.curity.identityserver.sdk.service.issuer.AccessTokenIssuer;
//import se.curity.identityserver.sdk.service.issuer.IdTokenIssuer;
//import se.curity.identityserver.sdk.service.issuer.RefreshTokenIssuer;
//import se.curity.identityserver.sdk.web.ResponseModel;
//
//import java.time.Instant;
//import java.util.HashMap;
//
//public final class AuthZENBackchannelAuthenticationTokenProcedure implements BackchannelAuthenticationTokenProcedure
//{
//    private final AuthZENTokenProcedureConfig _configuration;
//    private final AccessTokenIssuer accessTokenIssuer;
//    private final RefreshTokenIssuer refreshTokenIssuer;
//    private final IdTokenIssuer idTokenIssuer;
//
//    public AuthZENBackchannelAuthenticationTokenProcedure(AuthZENTokenProcedureConfig configuration)
//    {
//        _configuration = configuration;
//        accessTokenIssuer = _configuration.getAccessTokenIssuer();
//        refreshTokenIssuer = _configuration.getRefreshTokenIssuer();
//        idTokenIssuer = _configuration.getIdTokenIssuer();
//    }
//
//    @Override
//    public ResponseModel run(BackchannelAuthenticationTokenProcedurePluginContext context)
//    {
//        var delegationData = context.getDefaultDelegationData();
//        var issuedDelegation = context.getDelegationIssuer().issue(delegationData);
//
//        var accessTokenData = context.getDefaultAccessTokenData();
//        try
//        {
//            var issuedAccessToken = accessTokenIssuer.issue(accessTokenData, issuedDelegation);
//
//            var refreshTokenData = context.getDefaultRefreshTokenData();
//            var issuedRefreshToken = refreshTokenIssuer.issue(refreshTokenData, issuedDelegation);
//
//            var responseData = new HashMap<String, Object>(6);
//
//            responseData.put("access_token", issuedAccessToken);
//            responseData.put("scope", accessTokenData.getScope());
//            responseData.put("refresh_token", issuedRefreshToken);
//            responseData.put("token_type", "bearer");
//            responseData.put("expires_in", accessTokenData.getExpires().getEpochSecond() - Instant.now().getEpochSecond());
//
//            var idTokenData = context.getDefaultIdTokenData();
//            idTokenData.with(Attribute.of("at_hash", idTokenIssuer.atHash(issuedAccessToken)));
//
//            responseData.put("id_token", idTokenIssuer.issue(idTokenData, issuedDelegation));
//
//            return ResponseModel.mapResponseModel(responseData);
//        }
//        catch (TokenIssuerException e)
//        {
//           return ResponseModel.problemResponseModel("token_issuer_exception", "Could not issue new tokens");
//        }
//    }
//}
