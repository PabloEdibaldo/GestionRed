package com.GestionRed.GestionRed.dto.dtoNetworkAccessPoint.dtoPort;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortRequest {
    private Long boxNap;
    private int portNumber;
    private String nameClient;


}

