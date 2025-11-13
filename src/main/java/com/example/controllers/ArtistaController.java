package com.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Artista;
import com.example.repositories.ArtistaRepository;

@RestController
@RequestMapping("/artistes")
public class ArtistaController {

    @Autowired
    private ArtistaRepository artistaRepository;

    @GetMapping("/getArtistaCancons")
    public Artista obtenirArtistaCancons(@RequestParam("nom") String nom) {
        return artistaRepository.findByNom(nom);
    }
}
