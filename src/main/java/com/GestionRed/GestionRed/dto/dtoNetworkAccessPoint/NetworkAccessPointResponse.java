package com.GestionRed.GestionRed.dto.dtoNetworkAccessPoint;

import com.GestionRed.GestionRed.model.Port;
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
    private List<Port> ports;
    private String details;
}
