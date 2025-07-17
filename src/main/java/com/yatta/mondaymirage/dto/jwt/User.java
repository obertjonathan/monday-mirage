package com.yatta.mondaymirage.dto.jwt;

import lombok.Data;

import java.util.List;

@Data
public class User {
    public static final String INTERNAL = "Internal";
    public static final String EXTERNAL = "External";

    public static final String ROLE_ADMIN = "Admin";

    private String clientId;
    private String roleName;
    private List<String> permission;
}
