package com.GestionRed.GestionRed.dto.dtoNetworkAccessPoint;

import com.GestionRed.GestionRed.dto.dtoNetworkAccessPoint.dtoPort.PortRequest;
import com.GestionRed.GestionRed.model.Port;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NetworkAccessPointRequest {
    private String name;
    private String coordinates;
    private String location;
    private int ports;
    private String details;
}
