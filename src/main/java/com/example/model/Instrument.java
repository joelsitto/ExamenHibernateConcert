package com.example.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
@Table(name = "instrument")
public class Instrument {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "instrument_id")
	private Long instrumentId;

	@Column(name = "nom")
	private String nom;

	@Column(name = "popularitat")
	private double popularitat;

	@ManyToMany(mappedBy = "instruments")
	@JsonBackReference("canco-instruments")
	private List<Canco> cancons;

    @OneToOne(mappedBy = "instrumentPrincipal")
    @JsonManagedReference
    private Artista artistaPrincipal;


	public Instrument() {
		super();
	}

	public Instrument(String nom, double popularitat) {
		this.nom = nom;
		this.popularitat = popularitat;
	}

	public Long getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(Long instrumentId) {
		this.instrumentId = instrumentId;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public double getPopularitat() {
		return popularitat;
	}

	public void setPopularitat(double popularitat) {
		this.popularitat = popularitat;
	}

	public List<Canco> getCancons() {
		return cancons;
	}

	public void setCancons(List<Canco> cancons) {
		this.cancons = cancons;
	}

	@Override
	public String toString() {
		return "Instrument [instrumentId=" + instrumentId + ", nom=" + nom + ", popularitat=" + popularitat + "]";
	}

}
