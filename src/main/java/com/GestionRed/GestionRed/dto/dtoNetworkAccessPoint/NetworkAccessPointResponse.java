package com.GestionRed.GestionRed.dto.dtoNetworkAccessPoint;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NetworkAccessPointResponse {
    private Long id;
    private String name;
    private String coordinates;
    private String location;
    private int ports;
    private String details;
}
