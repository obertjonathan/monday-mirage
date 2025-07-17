package com.yatta.mondaymirage.service;

import com.yatta.mondaymirage.dto.BaseResponse;
import com.yatta.mondaymirage.enums.ResponseEnum;
import com.yatta.mondaymirage.exception.ValidationException;
import com.yatta.mondaymirage.repository.MerchantRepository;
import com.yatta.mondaymirage.security.CredentialContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public BaseResponse getMerchantInfo() {
        var credential = CredentialContextHolder.getContext();
        return ofNullable(credential)
                .map(data -> {
                    return new BaseResponse(ResponseEnum.SUCCESS);
                })
                .orElseThrow(() -> new ValidationException(ResponseEnum.NOT_FOUND));
    }
}
