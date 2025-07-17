package com.yatta.mondaymirage.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yatta.mondaymirage.annotation.Permission;
import com.yatta.mondaymirage.constant.RestConstant;
import com.yatta.mondaymirage.dto.jwt.JwtPayload;
import com.yatta.mondaymirage.enums.ResponseEnum;
import com.yatta.mondaymirage.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import static java.util.Optional.ofNullable;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {
    private final ObjectMapper objectMapper;

    public TokenInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        var permission = getPermission(handler);
        if (permission != null) {
            log.info("endpoint permission: {} with type: {} and roles: {}",
                    permission.value(), permission.type(), permission.roles());

            var auth = request.getHeader(RestConstant.AUTHORIZATION);
            log.info("request authorization : {}", auth);

            if (!isAuthorized(auth, permission)) {
                log.warn("user permission is not authorized");
                throw new ValidationException(ResponseEnum.PERMISSION_NOT_PERMITTED);
            }
        }
        return true;
    }

    private boolean isAuthorized(String auth, Permission permission) {
        try {
            return ofNullable(getTokenPermissions(auth))
                    .map(jwtPayload -> {
                        // store on context credential
                        CredentialContextHolder.setContext(jwtPayload);

                        return jwtPayload.getUser();
                    })
                    .map(user -> {
                        var allowed = true;
                        if (!permission.value().isEmpty()) {
                            var jwtPermissions = user.getPermission();
                            allowed = jwtPermissions.stream()
                                    .anyMatch(permission.value()::equalsIgnoreCase);
                        }

                        if (permission.roles().length > 0) {
                            allowed = Arrays.stream(permission.roles())
                                    .anyMatch(user.getRoleName()::equalsIgnoreCase);
                        }

                        return allowed;
                    })
                    .orElse(false);
        } catch (Exception e) {
            throw new ValidationException(ResponseEnum.PERMISSION_NOT_PERMITTED);
        }
    }

    private JwtPayload getTokenPermissions(String auth) throws JsonProcessingException {
        String token = auth.split(" ")[1]; // get jwt token
        String tokenPayload = token.split("[.]")[1]; // get jwt token payload
        // decode jwt token payload
        String decoded = new String(Base64.getDecoder().decode(tokenPayload), StandardCharsets.UTF_8);

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        JwtPayload payload = objectMapper.readValue(decoded, JwtPayload.class);
        log.info("request authorization payload: {}", payload);

        return payload;
    }

    private Permission getPermission(Object handler) {
        Method method = getMethod(handler);
        if (method != null && method.isAnnotationPresent(Permission.class)) {
            return method.getAnnotation(Permission.class);
        }
        return null;
    }

    private Method getMethod(Object handler) {
        HandlerMethod handlerMethod;

        if (handler instanceof HandlerMethod handlerMethods) {
            handlerMethod = handlerMethods;
            return handlerMethod.getMethod();
        }
        return null;
    }
}
