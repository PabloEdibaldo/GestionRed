package com.GestionRed.GestionRed.dto.dtoRedIpv4.dtoIps;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class IpsRequest {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IpsRequestConfig{
        private String userName;
        private Long idRedIpv4;
        private String ip;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteRequestConfig{
        private String userName;
        private Long idRedIpv4;

    }

}


