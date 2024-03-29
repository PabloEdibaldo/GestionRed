package com.GestionRed.GestionRed.repository;

import com.GestionRed.GestionRed.model.Port;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Port,Long> {
}
