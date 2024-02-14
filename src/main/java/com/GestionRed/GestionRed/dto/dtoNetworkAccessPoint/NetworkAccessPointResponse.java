package com.GestionRed.GestionRed.dto.dtoNetworkAccessPoint;

import lombok.*;

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
    private Long ports;
    private String details;
}
