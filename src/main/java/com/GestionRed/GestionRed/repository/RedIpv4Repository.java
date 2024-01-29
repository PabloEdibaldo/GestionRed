package com.GestionRed.GestionRed.repository;

import com.GestionRed.GestionRed.model.RedIpv4;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedIpv4Repository extends JpaRepository<RedIpv4,Long> {

}
