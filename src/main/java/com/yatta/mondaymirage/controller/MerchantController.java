package com.yatta.mondaymirage.controller;

import com.yatta.mondaymirage.annotation.Permission;
import com.yatta.mondaymirage.dto.BaseResponse;
import com.yatta.mondaymirage.service.MerchantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.yatta.mondaymirage.dto.jwt.User.EXTERNAL;
import static com.yatta.mondaymirage.dto.jwt.User.ROLE_ADMIN;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/v1/merchant")
@RestController
public class MerchantController {
    private final MerchantService merchantService;

    @Permission(type = EXTERNAL, roles = {ROLE_ADMIN})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> getMerchantInfo() {
        var response = ResponseEntity.ok(merchantService.getMerchantInfo());
        return response;
    }
}
