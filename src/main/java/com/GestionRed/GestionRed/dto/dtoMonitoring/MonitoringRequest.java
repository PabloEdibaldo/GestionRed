package com.GestionRed.GestionRed.dto.dtoMonitoring;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringRequest {

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
