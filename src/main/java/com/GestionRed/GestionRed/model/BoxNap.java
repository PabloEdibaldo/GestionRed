package com.GestionRed.GestionRed.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="t_cajaNap")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoxNap{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String coordinates;
    @Column(nullable = false)
    private String location;
    @Column(nullable = false)
    private Long ports;
    @Column(nullable = false)
    private String details;
}
