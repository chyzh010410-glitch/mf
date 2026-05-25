package com.mf.fertilizer.context;

/**
 * ThreadLocal-based user context — holds current request's user info parsed from JWT.
 * Populated by JwtInterceptor, cleared by afterCompletion. Read anywhere via static getters.
 */
public final class UserContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> USERNAME = new ThreadLocal<>();
    private static final ThreadLocal<String> ROLE = new ThreadLocal<>();
    private static final ThreadLocal<String> USER_TYPE = new ThreadLocal<>();

    private UserContext() {}

    public static void set(Long userId, String username, String role, String userType) {
        USER_ID.set(userId);
        USERNAME.set(username);
        ROLE.set(role);
        USER_TYPE.set(userType);
    }

    public static Long getUserId() {
        return USER_ID.get();
    }

    public static String getUsername() {
        return USERNAME.get();
    }

    public static String getRole() {
        return ROLE.get();
    }

    public static String getUserType() {
        return USER_TYPE.get();
    }

    public static void clear() {
        USER_ID.remove();
        USERNAME.remove();
        ROLE.remove();
        USER_TYPE.remove();
    }
}
