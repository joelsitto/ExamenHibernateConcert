package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Canco;
import com.example.model.Genere;

import java.util.List;

@Repository
public interface CancoRepository extends JpaRepository<Canco, Long> {

	List<Canco> findByGenere(Genere genere);

    //  Amb @Query podem fer consultes JPQL personalitzades
    //    @Query("SELECT e FROM Estudiant e WHERE e.edat BETWEEN :edatMin AND :edatMax")
    //    List<Estudiant> trobarPerRangEdat(@Param("edatMin") Integer min,
    //                                      @Param("edatMax") Integer max);

}