package com.projet4.repository;

import com.projet4.model.Reservation;
import com.projet4.model.enums.StatutReservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUtilisateurId(Long userId);
    List<Reservation> findByEquipementId(Long equipementId);
    List<Reservation> findByStatut(StatutReservation statut);
    List<Reservation> findByDateDebutBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Reservation> findByUtilisateurIdAndDateDebutAfter(Long userId, LocalDateTime currentDate);
    List<Reservation> findByEquipementIdAndStatut(Long equipementId, StatutReservation statut);
    List<Reservation> findByDateDebutBeforeAndDateFinAfter(LocalDateTime now1, LocalDateTime now2);
    @Query("SELECT r FROM Reservation r WHERE r.equipement.id = :equipementId AND r.dateDebut < :endDate AND r.dateFin > :startDate")
    List<Reservation> findConflictingReservations(@Param("equipementId") Long equipementId,
                                                  @Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate);

    long countByStatut(StatutReservation statut);
    Page<Reservation> findByUtilisateurId(Long userId, Pageable pageable);

}
