package com.GestionRed.GestionRed.SmartOLT.services;

import com.GestionRed.GestionRed.SmartOLT.ApiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
@Service
public class ServiceConfigOnus {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    ApiProperties apiProperties;



    public Object getOnusZones(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Token", STR."\{apiProperties.getToken()}");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Object> response = restTemplate.exchange(
                STR."\{apiProperties.getUrl()}/system/get_zones",
                HttpMethod.GET,
                entity,
                Object.class
        );

        return response.getBody();
    }

    public Object GetAllUnconfiguredONUs(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Token", STR."\{apiProperties.getToken()}");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Object> response = restTemplate.exchange(
                STR."\{apiProperties.getUrl()}/onu/unconfigured_onus",
                HttpMethod.GET,
                entity,
                Object.class
        );

        return response.getBody();
    }
}
