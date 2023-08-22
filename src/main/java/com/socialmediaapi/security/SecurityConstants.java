package com.socialmediaapi.security;

public class SecurityConstants {

    public static String SIGN_UP_URL = "/api/v1/auth/**";
    public static String ADMIN_URL = "/api/v1/admin/**";
    public static String ADMIN = "ADMIN";
    public static String SECRET = "SecretKeyGenJWTSecretKeyGenJWTSecretKeyGenJWTSecretKeyGenJWTSecretKeyGenJWTSecretKeyGenJWTSecretKeyGenJWT";
    public static String TOKEN_PREFIX = "Bearer ";
    public static String HEADER_STRING = "Authorization";
    public static String CONTENT_TYPE = "application/json";
    public static long EXPIRATION_TIME = 600_000_000;
    public static String EMAIL_PATTERN = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{1,5}|[0-9]{1,3})(\\]?)$";
    public static final String API_DOCS = "/v3/api-docs/**";
    public static final String SWAGGER_UI = "/swagger-ui/**";
    public static final String SWAGGER_UI_HTML = "/swagger-ui.html";
    public static final int BCRYPT_STRENGTH = 16;
}
