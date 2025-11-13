package com.example.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Table(name = "canco")
public class Canco {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "canco_id")
	private Long cancoId;

	@Column(length = 120, nullable = false)
	private String titol;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Genere genere;

	@Column(nullable = false)
	private double durada;

	@Column(nullable = false)
	private boolean disponible = true;

	@ManyToOne
	@JoinColumn(name = "artista_id")
    @JsonBackReference("artista-cancons") // Per no tornar referencies infinites
	private Artista artista;

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "canco_instrument", 
	joinColumns = @JoinColumn(name = "canco_id"), 
	inverseJoinColumns = @JoinColumn(name = "instrument_id"))
	@JsonManagedReference("canco-instruments")
	private List<Instrument> instruments = new ArrayList<>();

	// Constructors
	public Canco() {
	}

	public Canco(String titol, Genere genere, Double durada) {
		this.titol = titol;
		this.genere = genere;
		this.durada = durada;
		this.disponible = true;
	}

	// Getters i Setters
	public Long getCancoId() {
		return cancoId;
	}

	public void setCancoId(Long cancoId) {
		this.cancoId = cancoId;
	}

	public String getTitol() {
		return titol;
	}

	public void setTitol(String titol) {
		this.titol = titol;
	}

	public Genere getGenere() {
		return genere;
	}

	public void setGenere(Genere genere) {
		this.genere = genere;
	}

	public Double getDurada() {
		return durada;
	}

	public void setDurada(Double durada) {
		this.durada = durada;
	}

	public Boolean getDisponible() {
		return disponible;
	}

	public void setDisponible(Boolean disponible) {
		this.disponible = disponible;
	}

	public Artista getArtista() {
		return artista;
	}

	public void setArtista(Artista artista) {
		this.artista = artista;
	}

	public List<Instrument> getInstruments() {
		return instruments;
	}

	public void setInstruments(List<Instrument> instruments) {
		this.instruments = instruments;
	}

	@Override
	public String toString() {
		return "Canco{" + "cancoId=" + cancoId + ", titol='" + titol + '\'' + ", genere=" + genere + ", durada="
				+ durada + ", disponible=" + disponible + ", numInstruments=" + instruments.size() + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Canco canco = (Canco) o;
		return Objects.equals(cancoId, canco.cancoId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cancoId);
	}
}
