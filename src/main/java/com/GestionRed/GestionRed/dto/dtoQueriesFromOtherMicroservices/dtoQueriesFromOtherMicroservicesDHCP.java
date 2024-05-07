package com.GestionRed.GestionRed.dto.dtoQueriesFromOtherMicroservices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class dtoQueriesFromOtherMicroservicesDHCP {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClientDHCPRequest{

        private String userName;
        private String address;
        private Long idRouter;
        private String macAddress;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClientDHCPDeleteRequest{
        private String address;
        private Long idRouter;
    }
}
