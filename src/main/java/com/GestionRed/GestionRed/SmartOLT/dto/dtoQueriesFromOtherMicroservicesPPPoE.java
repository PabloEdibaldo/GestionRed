package com.GestionRed.GestionRed.SmartOLT.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class dtoQueriesFromOtherMicroservicesPPPoE {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class requestDtoAuthorizeONU{
        private int olt_id;
        private String pon_type;
        private int board;
        private int port;
        private String sn;
        private String vlan;
        private String onu_type;
        private String zone;
        private String odb;
        private String name;
        private String address_or_comment;
        private String onu_mode;
        private String onu_external_id;
        private String upload_speed_profile_name;
        private String download_speed_profile_name;

        private String password;


        private String modeConfigOnu;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static  class requestDtoSetONUWANModeToPPPoE{
        private String username;
        private String password;
        private String idOnu;
    }

}
