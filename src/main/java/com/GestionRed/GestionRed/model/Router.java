package com.GestionRed.GestionRed.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
    @Table(name = "t_Router")
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class Router {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(nullable = false)
        private String name;
        @Column(nullable = false)
        private String ipAddress;
        @Column(nullable = false)
        private String userMikrotik;
        //Mikrotik
        @Column(nullable = false)
        private String password;

        //Data
        private String location;
        private String security;
        private String security_alt;
        //Configuration Radius
        private String radius_secret;
        private String radius_nas_ip;

    }





