package com.yatta.mondaymirage.security;

import com.yatta.mondaymirage.dto.jwt.JwtPayload;

import java.util.Optional;

public class CredentialContextHolder {
    private static final ThreadLocal<JwtPayload> contextHolder = new ThreadLocal<>();

    private CredentialContextHolder() {
        // No Constructor
    }

    public static JwtPayload getContext() {
        var jwtPayload = contextHolder.get();
        return Optional.ofNullable(jwtPayload)
                .orElse(null);
    }

    public static void setContext(JwtPayload jwtPayload) {
        contextHolder.set(jwtPayload);
    }

    public static void unset() {
        contextHolder.remove();
    }

    public static String getClientId() {
        var jwtPayload = contextHolder.get();
        return Optional.ofNullable(jwtPayload)
                .map(payload -> payload.getUser().getClientId())
                .orElse(null);
    }
}
