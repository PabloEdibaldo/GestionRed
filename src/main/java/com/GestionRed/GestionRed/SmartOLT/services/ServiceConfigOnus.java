package com.GestionRed.GestionRed.SmartOLT.services;

import com.GestionRed.GestionRed.SmartOLT.ApiProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;


@Slf4j
@Service
public class ServiceConfigOnus {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    ApiProperties apiProperties;




    public Object OptionCase(int caseOption,String linkRequest ){


        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Token", apiProperties.getToken());
        HttpEntity<String> entity = new HttpEntity<>(headers);


        return switch (caseOption) {
            case 1 -> restTemplate.exchange(apiProperties.getUrl() + linkRequest, HttpMethod.GET, entity, Object.class);
            //return  responseGET.getBody();
            case 2 -> restTemplate.exchange(apiProperties.getUrl() + linkRequest, HttpMethod.POST, entity, Object.class);
            //return  responsePOST.getBody();
            default -> throw new IllegalArgumentException(STR."Invalid case action:\{caseOption}");
        };

    }

}
