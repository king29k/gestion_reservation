package com.projet4.repository;

import com.projet4.model.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {

    // Toutes les maintenances d'un équipement donné
    List<Maintenance> findByEquipementId(Long equipementId);

    // Toutes les maintenances à une date donnée
    List<Maintenance> findByDate(LocalDate date);

    // Toutes les maintenances dans une période
    List<Maintenance> findByDateBetween(LocalDate startDate, LocalDate endDate);

    // Rechercher dans les descriptions d'actions (full text)
    List<Maintenance> findByActionsContainingIgnoreCase(String keyword);

    // Compter les maintenances d’un équipement
    long countByEquipementId(Long equipementId);

    // Historique des maintenances d’un équipement trié par date
    List<Maintenance> findByEquipementIdOrderByDateDesc(Long equipementId);

    // Rechercher les maintenances récentes après une date donnée
    @Query("SELECT m FROM Maintenance m WHERE m.equipement.id = :equipementId AND m.date > :date")
    List<Maintenance> findRecentMaintenances(@Param("equipementId") Long equipementId, @Param("date") LocalDate date);
}
