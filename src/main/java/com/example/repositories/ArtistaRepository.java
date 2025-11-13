package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Artista;

@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Long> {
	Artista findByNom(String nom);
}
