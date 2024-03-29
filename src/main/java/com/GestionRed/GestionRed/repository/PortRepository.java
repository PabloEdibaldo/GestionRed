package com.GestionRed.GestionRed.repository;

import com.GestionRed.GestionRed.model.BoxNap;
import com.GestionRed.GestionRed.model.Port;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortRepository extends JpaRepository<Port,Long> {

}
