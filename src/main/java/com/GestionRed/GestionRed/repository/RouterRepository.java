package com.GestionRed.GestionRed.repository;


import com.GestionRed.GestionRed.model.Router;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouterRepository extends JpaRepository<Router,Long> {

    Optional<Router> findByName(String routerName);
}


