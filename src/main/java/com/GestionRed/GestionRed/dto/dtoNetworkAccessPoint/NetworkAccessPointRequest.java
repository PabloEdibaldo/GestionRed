package com.GestionRed.GestionRed.dto.dtoNetworkAccessPoint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NetworkAccessPointRequest {
    private String name;
    private String coordinates;
    private String location;
    private Long ports;
    private String details;
}
