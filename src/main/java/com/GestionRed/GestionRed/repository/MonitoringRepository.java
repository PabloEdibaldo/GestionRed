package com.GestionRed.GestionRed.repository;

import com.GestionRed.GestionRed.model.Monitoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitoringRepository  extends JpaRepository<Monitoring,Long> {
}
