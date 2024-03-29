package com.GestionRed.GestionRed.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.annotation.Id;


@Table(name = "t_Router")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouterResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    private String ipAddress;
    private String userMikrotik;
    //Mikrotik
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
