package com.GestionRed.GestionRed.dto.dtoRedIpv4;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedIpv4Request {
    private Long id;
    private String name;
    private String name_router;
    private String red_ip;
    private String cidr;
    private String use_type;
}
