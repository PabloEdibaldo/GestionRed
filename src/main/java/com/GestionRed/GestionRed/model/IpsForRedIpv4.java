package com.GestionRed.GestionRed.model;

import com.GestionRed.GestionRed.services.NetworkAccessPointService;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_IpsForRedIpv4")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IpsForRedIpv4 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "redIpv4_id", nullable = false)
    private RedIpv4 redIpv4;

    private String ipAddress;
    private String ip;
    private int status;
    private String nameClient;

}
