package com.example.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Artista;
import com.example.model.Canco;
import com.example.model.Genere;
import com.example.model.Instrument;
import com.example.repositories.ArtistaRepository;
import com.example.repositories.CancoRepository;

@RestController
@RequestMapping("/cancons")
public class CancoController {

	@Autowired
	private CancoRepository cancoRepo;

	@Autowired
	private ArtistaRepository artistaRepo;

	@GetMapping("/{genere}")
	public List<Canco> getCanconsPerGenere(@PathVariable Genere genere) {
		return cancoRepo.findByGenere(genere);
	}

	@PostMapping("/nomesCanconsPopulars")
	public Map<String, Object> eliminarCanconsNoPopulars() {		
		// Tota aquesta logica hauria d'anar al Service, pero vam simplificar per l'examen
		// Recordeu que els controllers nomes han d'ocupar-se dels HTTP  
		List<Canco> cancons = cancoRepo.findAll();
		int eliminades = 0;
		List<Artista> artistesAfectats = new ArrayList<>();

		for (Canco canco : cancons) {
			double suma = 0;

			if (canco.getInstruments() != null && !canco.getInstruments().isEmpty()) {
				for (Instrument ins : canco.getInstruments()) {
					suma += ins.getPopularitat();
				}
			}
			
			suma /= canco.getInstruments().size();

			if (suma < 3.0) {
				canco.setDisponible(false);
				cancoRepo.save(canco);

				Artista artista = canco.getArtista();
				if (artista != null) {
					artista.setCache(artista.getCache() - 10.0);
					artistaRepo.save(artista);
					artistesAfectats.add(artista);
				}
				eliminades++;
			}
		}

		// Preparem resposta JSON, encara que es pot fer d'altres formes
		Map<String, Object> resposta = new HashMap<>();
		resposta.put("cançonsEliminades", eliminades);
		resposta.put("artistesAfectats", artistesAfectats.size());
		resposta.put("missatge", "Setlist actualitzada correctament");

		return resposta;
	}

}
