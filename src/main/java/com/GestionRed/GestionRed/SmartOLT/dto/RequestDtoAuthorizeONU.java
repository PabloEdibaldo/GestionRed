package com.GestionRed.GestionRed.SmartOLT.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDtoAuthorizeONU {
    private String olt_id;
    private String pon_type;
    private String board;
    private String port;
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

}
