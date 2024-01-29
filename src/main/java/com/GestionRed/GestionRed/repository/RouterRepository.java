package com.GestionRed.GestionRed.repository;


import com.GestionRed.GestionRed.model.Router;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouterRepository extends JpaRepository<Router,Long> {

}


