package com.yatta.mondaymirage.service;

import com.yatta.mondaymirage.dto.merchant.MerchantInfoResponseDto;
import com.yatta.mondaymirage.entity.Merchant;
import com.yatta.mondaymirage.enums.ResponseEnum;
import com.yatta.mondaymirage.exception.ValidationException;
import com.yatta.mondaymirage.repository.MerchantRepository;
import com.yatta.mondaymirage.security.CredentialContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public MerchantInfoResponseDto getMerchantInfo() {
        var credential = CredentialContextHolder.getContext();
        return ofNullable(credential)
                .map(data -> {
                    String clientId = data.getUser().getClientId();
                    Merchant merchant = merchantRepository.findByClientId(clientId)
                            .orElseThrow(() -> new ValidationException(ResponseEnum.NOT_FOUND, "Merchant not found"));

                    MerchantInfoResponseDto response = new MerchantInfoResponseDto();
                    response.setClientId(merchant.getClientId());
                    response.setName(merchant.getName());
                    response.setEmail(merchant.getEmail());
                    response.setStatus(merchant.getStatus());

                    return response;
                })
                .orElseThrow(() -> new ValidationException(ResponseEnum.NOT_FOUND));
    }
}
