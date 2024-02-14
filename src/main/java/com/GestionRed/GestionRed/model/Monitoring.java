package com.GestionRed.GestionRed.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "t_monitoring")


public class Monitoring {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String issuer_name;
    @Column(nullable = false)
    private String direction_ip;
    private String maker;
    private String name_model;
    private String user;
    private String password;
    private Double monitoring_SNMP;
    private String community_SNMP;
    private String version_SNMP;


}
