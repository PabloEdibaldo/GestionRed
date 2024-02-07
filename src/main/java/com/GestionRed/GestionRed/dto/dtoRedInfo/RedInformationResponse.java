package com.GestionRed.GestionRed.dto.dtoRedInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class RedInformationResponse {
        private  String networkAddress;
        private  String address;
        private  String subnetMask;
        private  String broadcastAddress;

        private Long totalAddresses;
        private Long usableAddresses;


}
