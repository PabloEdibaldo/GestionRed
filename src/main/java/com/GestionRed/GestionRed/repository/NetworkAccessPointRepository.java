package com.GestionRed.GestionRed.repository;

import com.GestionRed.GestionRed.model.BoxNap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NetworkAccessPointRepository extends JpaRepository<BoxNap,Long> {
}
