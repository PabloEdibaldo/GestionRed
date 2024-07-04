package com.GestionRed.GestionRed.SmartOLT.repository;

import com.GestionRed.GestionRed.SmartOLT.models.OnuDetailsCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnuDetailsRepository extends JpaRepository<OnuDetailsCache,Long> {

}
