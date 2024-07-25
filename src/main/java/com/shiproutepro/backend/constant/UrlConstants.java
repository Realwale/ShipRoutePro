package com.shiproutepro.backend.constant;

public class UrlConstants {

    public static final String BASE_URL="/api/v1/";

    public static final String[] WHITE_LIST_URLS = new String[]{
            "/",
            "/api/v1/auth/**",
            "/v3/api-docs.yaml",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/api/v1/account/registration",
            "/api/v1/account/registration/verify",
            "/api/v1/account/registration/resend-link",
            "/api/v1/account/login",
            "/api/v1/reset-password/init",
            "/api/v1/reset-password/verify",
            "/api/v1/reset-password/complete",
            "/api/v1/account/refresh-token",
            "/api/v1/account/registration/set-password"

    };

    public static final String[] ADMIN_ONLY_URLS = new String[]{
            "/api/v1/role/update",
            "api/v1/user/authorize",
            "api/v1/user/delete",
            "api/v1/user/fetch",
            "api/v1/user/revoke",
            "api/v1/user/violation",
            "api/v1/user/registration/invite-user"

    };

    public static final String[] SECURITY_GUARDS_ONLY_URLS = new String[]{
            "api/v1/access/code/verify",
            "api/v1/access/code/approve",
            "api/v1/access/code/decline"

    };

    public static final String[] ADMIN_AND_RESIDENT_ONLY_URLS = new String[]{
            "api/v1/access/code/generate",
    };

    public static final String ADMIN_AUTHORITY = "ADMIN";

    public static final String RESIDENT_AUTHORITY = "RESIDENT";

    public static final String SECURITY_GUARDS_AUTHORITY = "SECURITY";


}
