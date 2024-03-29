package com.GestionRed.GestionRed.dto;



import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouterRequest {
    private Long id;
    private String name;
    private String ipAddress;
    private String userMikrotik;
    //Mikrotik
    @Getter
    private String password;

    //Data

    private String location;
    private String security;
    private String security_alt;
    //Configuration Radius
    private String radius_secret;
    private String radius_nas_ip;
    private String typeServer;
}
