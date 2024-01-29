package com.GestionRed.GestionRed.dto.dtoRedIpv4;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.annotation.Id;

@Table(name = "t_redIpv4")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedIpv4Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String name_router;
    private String red_ip;
    private String cidr;
    private String use_type;
}
