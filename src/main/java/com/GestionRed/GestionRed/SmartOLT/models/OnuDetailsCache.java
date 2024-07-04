package com.GestionRed.GestionRed.SmartOLT.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "onu_details", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" }) })
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OnuDetailsCache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String responseData;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime timestamp;


}
