package com.GestionRed.GestionRed.dto.dtoMonitoring;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonitoringResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String issuer_name;
    private String direction_ip;
    private String maker;
    private String name_model;
    private String user;
    private String password;
    private Double monitoring_SNMP;
    private String community_SNMP;
    private String version_SNMP;
}
