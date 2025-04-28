package com.projet4.repository;

import com.projet4.model.Equipement;
import com.projet4.model.enums.EtatEquipement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EquipementRepository extends JpaRepository<Equipement, Long> {
    List<Equipement> findByType(String type);
    List<Equipement> findByEtat(EtatEquipement etat);
    List<Equipement> findByLaboratoire(String laboratoire);
    Page<Equipement> findByType(String type, Pageable pageable);
    List<Equipement> findByTypeAndEtatAndLaboratoire(
            String type,
            EtatEquipement etat,
            String laboratoire
    );

    @Query("SELECT e FROM Equipement e WHERE e.etat = :etat AND e NOT IN (" +
            " SELECT r.equipement FROM Reservation r WHERE r.dateDebut < :fin AND r.dateFin > :debut )")
    List<Equipement> findAvailableBetween(
            @Param("debut") LocalDateTime debut,
            @Param("fin") LocalDateTime fin,
            @Param("etat") EtatEquipement etat
    );


}
