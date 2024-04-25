package com.GestionRed.GestionRed.dto.dtoRedInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class RedInformationResponse {
        private  String subnetInfo;
        private  String ipAddress;
        private  String ipAddressHex;
        private  String ipAddressBinary;
        private  String netmask;
        private  String netmaskHex;
        private  String netmaskBinary;
        private  String networkAddress;
        private  String networkAddressHex;
        private  String networkAddressBinary;
        private  String broadcastAddress;
        private  String lowAddress;
        private  String highAddress;
        private  Long totalUsableAddresses;


}
