package com.GestionRed.GestionRed.SmartOLT.services;
import com.GestionRed.GestionRed.SmartOLT.ApiProperties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;

@Slf4j
@Service
public class ServiceConfigOnus {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    ApiProperties apiProperties;

    public ResponseEntity<Object> OptionCase(int caseOption,
                             String linkRequest,
                             MultiValueMap<String, Object> requestDtoAuthorizeONU){
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Token", apiProperties.getToken());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestDtoAuthorizeONU, headers);
        return switch (caseOption) {
            case 1 -> restTemplate.exchange(
                    apiProperties.getUrl() + linkRequest,
                    HttpMethod.GET,
                    entity,
                    Object.class);
            case 2 -> restTemplate.exchange(
                        apiProperties.getUrl() + linkRequest,
                        HttpMethod.POST,
                        requestEntity,
                        Object.class);
            default -> throw new IllegalArgumentException("Invalid case action:");
        };
    }

    public Object GetVlans(String url){
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Token", apiProperties.getToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Object> response = restTemplate.exchange(
                apiProperties.getUrl()+url,
               HttpMethod.GET,
                entity,
                Object.class
        );

        return response.getBody();
    }
}
