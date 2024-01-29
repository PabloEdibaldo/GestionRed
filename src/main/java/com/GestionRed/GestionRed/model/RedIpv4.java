package com.GestionRed.GestionRed.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "t_redIpv4")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RedIpv4 {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String name_router;
    @Column(nullable = false)
    private String red_ip;
    @Column(nullable = false)
    private String cidr;
    @Column(nullable = false)
    private String use_type;
}
