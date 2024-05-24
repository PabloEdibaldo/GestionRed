package com.GestionRed.GestionRed.dto.dtoQueriesFromOtherMicroservices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.ref.SoftReference;
import java.util.stream.IntStream;

public class QueriesFromOtherMicroservicesRequest {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClientPPPoERequest{
        private Long idUser;
        private String userName;
        private String address;
        private Long idRouter;
        private String userPassword;


    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignPromotionRequest{
        private String listPromotion;
        private String address;
        private Long idRouter;

    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteClientInListPromotion{
        private String address;
        private String namePromotion;
        private Long idRouter;
        private String profile;

    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CutServiceClientRequest{
        private String nameClient;
        private String address;
        private Long idRouter;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateProfilePPP{
        private Long idRouter;
        private String name;
        private String lowSpeed;
        private String uploadSpeed;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CutServicePPPoEClient{
        private Long idRouter;
        private String remoteAddress;

    }


}

