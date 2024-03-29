package com.GestionRed.GestionRed.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "t_port_box")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Port {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "box_nap_id",nullable = false)
    private BoxNap boxNap;
    private int portNumber;
    @Column(nullable = false)
    private int status;
    private String nameClient;


}
