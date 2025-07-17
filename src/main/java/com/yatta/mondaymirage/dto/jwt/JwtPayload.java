package com.yatta.mondaymirage.dto.jwt;

import lombok.Data;

@Data
public class JwtPayload {
    private String sub;
    private Integer exp;
    private User user;
    private Integer iat;
}
