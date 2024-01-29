package com.GestionRed.GestionRed.services;


import com.GestionRed.GestionRed.dto.dtoRedIpv4.RedIpv4Request;
import com.GestionRed.GestionRed.dto.dtoRedIpv4.RedIpv4Response;
import com.GestionRed.GestionRed.model.RedIpv4;
import com.GestionRed.GestionRed.repository.RedIpv4Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedIpv4Service {
    private final RedIpv4Repository redIpv4Repository;

    public void createRedIpv4(RedIpv4Request redIpv4Request){
        RedIpv4 redIpv4 = RedIpv4.builder()
                .name(redIpv4Request.getName())
                .name_router(redIpv4Request.getName_router())
                .red_ip(redIpv4Request.getRed_ip())
                .cidr(redIpv4Request.getCidr())
                .use_type(redIpv4Request.getUse_type())
                .build();
        redIpv4Repository.save(redIpv4);
        log.info("Red {} in saved",redIpv4.getId());

    }

    public List<RedIpv4Response> getAllRedIpv4(){
        List<RedIpv4> redIpv4s = redIpv4Repository.findAll();

        return redIpv4s.stream().map(this::mapToRedIpv4Response).toList();
    }

    private RedIpv4Response mapToRedIpv4Response(RedIpv4 redIpv4) {
        return RedIpv4Response.builder()
                .id(redIpv4.getId())
                .name(redIpv4.getName())
                .name_router(redIpv4.getName_router())
                .red_ip(redIpv4.getRed_ip())
                .cidr(redIpv4.getCidr())
                .use_type(redIpv4.getUse_type())
                .build();
    }
    public void updateRedIpv4(Long id, RedIpv4Request redIpv4Request){
        Optional<RedIpv4> optionalRedIpv4 = redIpv4Repository.findById(id);

        if(optionalRedIpv4.isPresent()){
            RedIpv4 exitingModels = optionalRedIpv4.get();
            exitingModels.setName(redIpv4Request.getName());
            exitingModels.setName_router(redIpv4Request.getName_router());
            exitingModels.setRed_ip(redIpv4Request.getRed_ip());
            exitingModels.setCidr(redIpv4Request.getCidr());
            exitingModels.setUse_type(redIpv4Request.getUse_type());

            redIpv4Repository.save(exitingModels);
            log.info("Red Ipv4 {}  updated",id);
        }else
            log.warn("Red ipv4 if {} not found",id);

    }

    public void deleteRedIpv4(Long id){
        Optional<RedIpv4> optionalRedIpv4 = redIpv4Repository.findById(id);
        if(optionalRedIpv4.isPresent()){
            redIpv4Repository.deleteById(id);
        }else
            log.warn(" Red ipv4  with Id {} not found", id);
    }



}
