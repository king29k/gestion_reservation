package com.projet4.service;

import com.projet4.model.Reservation;
import com.projet4.model.enums.StatutReservation;
import com.projet4.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    public List<Reservation> findByUtilisateurId(Long userId) {
        return reservationRepository.findByUtilisateurId(userId);
    }

    public List<Reservation> findByEquipementId(Long equipementId) {
        return reservationRepository.findByEquipementId(equipementId);
    }

    public List<Reservation> findByStatut(StatutReservation statut) {
        return reservationRepository.findByStatut(statut);
    }

    public Optional<Reservation> getById(Long id) {
        return reservationRepository.findById(id);
    }

    public Reservation save(Reservation r) {
        return reservationRepository.save(r);
    }

    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }
}
