package com.example.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "escenari")
public class Escenari {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "escenari_id")
	private Long escenariId;

	@Column(name = "nom", length = 50)
	private String nom;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipus")
	private TipusEscenari tipus;

	@ManyToOne
	@JoinColumn(name = "artista_id")
	@JsonBackReference("artista-escenaris")
	private Artista artista;

	public Escenari() {
		super();
	}

	public Escenari(String nom, TipusEscenari principal) {
		this.nom = nom;
		this.tipus = principal;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public TipusEscenari getTipus() {
		return tipus;
	}

	public void setTipus(TipusEscenari tipus) {
		this.tipus = tipus;
	}

	public Artista getArtista() {
		return artista;
	}

	public void setArtista(Artista artista) {
		this.artista = artista;
	}

	@Override
	public int hashCode() {
		return Objects.hash(escenariId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Escenari other = (Escenari) obj;
		return Objects.equals(escenariId, other.escenariId);
	}

	@Override
	public String toString() {
		return "Escenari [escenariId=" + escenariId + ", nom=" + nom + ", tipus=" + tipus + ", artista=" + artista.getNom()
				+ "]";
	}
	
	
	
}
