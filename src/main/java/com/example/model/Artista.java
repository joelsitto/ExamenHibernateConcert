package com.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "artista")
public class Artista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artista_id")
    private Long artistaId;

    @Column(name = "nom", length = 80)
    private String nom;

    @Column(name = "concerts_disponibles")
    private int concertsDisponibles;

    @Column(name = "cache")
    private double cache;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Column(name = "generes")
    private List<Genere> generes;

    @OneToMany(mappedBy = "artista")
    @JsonManagedReference("artista-cancons") 
    private List<Canco> cancons = new ArrayList<>();

    @OneToMany(mappedBy = "artista")
    @JsonManagedReference("artista-escenaris")
    private List<Escenari> escenaris = new ArrayList<>();

    @OneToOne(cascade =  CascadeType.ALL)
    @JoinColumn(name = "instrument_principal_id")
    @JsonBackReference
    private Instrument instrumentPrincipal;
    
	
	public Artista() {
	}

	public Artista(String nom, double cache, List<Genere> generes) {
		this.nom = nom;
		this.cache = cache;
		this.generes = generes;
	}

	public Artista(String nom, int concertsDisponibles, double cache, List<Genere> generes, List<Canco> cancons,
			List<Escenari> escenaris) {
		super();
		this.nom = nom;
		this.concertsDisponibles = concertsDisponibles;
		this.cache = cache;
		this.generes = generes;
		this.cancons = cancons;
		this.escenaris = escenaris;
	}

	public Long getArtistaId() {
		return artistaId;
	}

	public void setArtistaId(Long artistaId) {
		this.artistaId = artistaId;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getConcertsDisponibles() {
		return concertsDisponibles;
	}

	public void setConcertsDisponibles(int concertsDisponibles) {
		this.concertsDisponibles = concertsDisponibles;
	}

	public double getCache() {
		return cache;
	}

	public void setCache(double cache) {
		this.cache = cache;
	}

	public List<Genere> getGeneres() {
		return generes;
	}

	public void setGeneres(List<Genere> generes) {
		this.generes = generes;
	}

	public List<Canco> getCancons() {
		return cancons;
	}

	public void setCancons(List<Canco> cancons) {
		this.cancons = cancons;
	}

	public List<Escenari> getEscenaris() {
		return escenaris;
	}

	public void setEscenaris(List<Escenari> escenaris) {
		this.escenaris = escenaris;
	}

	@Override
	public String toString() {
		return "Artista [artistaId=" + artistaId + ", nom=" + nom + ", concertsDisponibles=" + concertsDisponibles
				+ ", cache=" + cache + ", generes=" + generes + ", cancons=" + cancons;
	}

	@Override
	public int hashCode() {
		return Objects.hash(artistaId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Artista other = (Artista) obj;
		return Objects.equals(artistaId, other.artistaId);
	}
    
}
