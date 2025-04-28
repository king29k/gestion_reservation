package com.projet4.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.projet4.model.Equipement;
import com.projet4.model.Reservation;
import com.projet4.model.Utilisateur;
import com.projet4.model.enums.StatutReservation;
import com.projet4.repository.EquipementRepository;
import com.projet4.repository.ReservationRepository;
import com.projet4.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDateTime;


import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Réservations", description = "Endpoints de gestion des réservations")
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationRepository reservationRepo;
    private final UtilisateurRepository utilisateurRepo;
    private final EquipementRepository equipementRepo;

    @Operation(summary = "Créer une réservation", description = "Accessible uniquement par les chercheurs")
    @PostMapping
    @PreAuthorize("hasRole('CHERCHEUR')")
    public ResponseEntity<?> create(@RequestBody Reservation r, Authentication auth) {
        String email = auth.getName();
        Utilisateur user = utilisateurRepo.findByEmail(email).orElseThrow();
        Equipement eq = equipementRepo.findById(r.getEquipement().getId()).orElseThrow();

        r.setUtilisateur(user);
        r.setEquipement(eq);
        r.setStatut(StatutReservation.EN_ATTENTE);
        return new ResponseEntity<>(reservationRepo.save(r), HttpStatus.CREATED);
    }

    @Operation(summary = "Lister toutes les réservations", description = "Filtrable par utilisateur, équipement ou statut")
    @GetMapping
    public List<Reservation> getAll(
            @Parameter(description = "ID de l'utilisateur") @RequestParam(required = false) Long userId,
            @Parameter(description = "ID de l'équipement") @RequestParam(required = false) Long equipementId,
            @Parameter(description = "Statut de la réservation") @RequestParam(required = false) StatutReservation statut) {
        if (userId != null) return reservationRepo.findByUtilisateurId(userId);
        if (equipementId != null) return reservationRepo.findByEquipementId(equipementId);
        if (statut != null) return reservationRepo.findByStatut(statut);
        return reservationRepo.findAll();
    }

    @Operation(summary = "Obtenir une réservation par ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        return reservationRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Modifier le statut d'une réservation", description = "Accessible uniquement au gestionnaire")
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<?> updateStatus(
            @Parameter(description = "ID de la réservation") @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return reservationRepo.findById(id).map(r -> {
            r.setStatut(StatutReservation.valueOf(body.get("etat")));
            return ResponseEntity.ok(reservationRepo.save(r));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Annuler une réservation", description = "Accessible au chercheur uniquement")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CHERCHEUR')")
    public void delete(@PathVariable Long id) {
        reservationRepo.deleteById(id);
    }

    @Operation(summary = "Lister les réservations dans une période")
    @GetMapping("/periode")
    public List<Reservation> getByDateRange(
            @Parameter(description = "Date de début, format : YYYY-MM-DDTHH:mm") @RequestParam("debut") String debut,
            @Parameter(description = "Date de fin, format : YYYY-MM-DDTHH:mm") @RequestParam("fin") String fin) {
        LocalDateTime d1 = LocalDateTime.parse(debut);
        LocalDateTime d2 = LocalDateTime.parse(fin);
        return reservationRepo.findByDateDebutBetween(d1, d2);
    }

    @Operation(summary = "Réservations futures d'un utilisateur")
    @GetMapping("/utilisateur/{id}/futures")
    public List<Reservation> getFuturesByUser(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long id) {
        return reservationRepo.findByUtilisateurIdAndDateDebutAfter(id, LocalDateTime.now());
    }

    @Operation(summary = "Réservations actives actuellement")
    @GetMapping("/actives")
    public List<Reservation> getActivesNow() {
        LocalDateTime now = LocalDateTime.now();
        return reservationRepo.findByDateDebutBeforeAndDateFinAfter(now, now);
    }

    @Operation(summary = "Réservations en conflit pour un équipement et une période")
    @GetMapping("/conflits")
    public List<Reservation> getConflicting(
            @Parameter(description = "ID de l'équipement") @RequestParam("equipementId") Long equipementId,
            @Parameter(description = "Date de début") @RequestParam("debut") String debut,
            @Parameter(description = "Date de fin") @RequestParam("fin") String fin) {
        return reservationRepo.findConflictingReservations(
                equipementId,
                LocalDateTime.parse(debut),
                LocalDateTime.parse(fin)
        );
    }

    @Operation(summary = "Compter les réservations par statut")
    @GetMapping("/statistiques")
    public long countByStatut(
            @Parameter(description = "Statut de la réservation") @RequestParam("statut") StatutReservation statut) {
        return reservationRepo.countByStatut(statut);
    }

    @Operation(summary = "Lister les réservations d'un utilisateur avec pagination")
    @GetMapping("/utilisateur/{id}/page")
    public Page<Reservation> getByUserPaged(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long id,
            @Parameter(description = "Numéro de page (0 par défaut)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de page (5 par défaut)") @RequestParam(defaultValue = "5") int size) {
        return reservationRepo.findByUtilisateurId(id, PageRequest.of(page, size));
    }
}
