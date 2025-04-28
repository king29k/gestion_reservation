package com.projet4.controller;

import com.projet4.model.Equipement;
import com.projet4.model.enums.EtatEquipement;
import com.projet4.repository.EquipementRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/equipements")
@Tag(name = "Équipements", description = "Gestion des équipements scientifiques")
@RequiredArgsConstructor
public class EquipementController {

    private final EquipementRepository equipementRepo;

    @Operation(summary = "Lister tous les équipements avec filtres")
    @GetMapping
    public List<Equipement> getAll(
            @Parameter(description = "Filtrer par type") @RequestParam(required = false) String type,
            @Parameter(description = "Filtrer par état") @RequestParam(required = false) EtatEquipement etat,
            @Parameter(description = "Filtrer par laboratoire") @RequestParam(required = false) String laboratoire) {
        if (type != null) return equipementRepo.findByType(type);
        if (etat != null) return equipementRepo.findByEtat(etat);
        if (laboratoire != null) return equipementRepo.findByLaboratoire(laboratoire);
        return equipementRepo.findAll();
    }

    @Operation(summary = "Obtenir un équipement par ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@Parameter(description = "ID de l'équipement") @PathVariable Long id) {
        return equipementRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Créer un nouvel équipement")
    @PostMapping
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<?> create(@RequestBody Equipement e) {
        return new ResponseEntity<>(equipementRepo.save(e), HttpStatus.CREATED);
    }

    @Operation(summary = "Mettre à jour un équipement")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Equipement updated) {
        return equipementRepo.findById(id).map(e -> {
            e.setType(updated.getType());
            e.setNumeroSerie(updated.getNumeroSerie());
            e.setEtat(updated.getEtat());
            e.setLaboratoire(updated.getLaboratoire());
            return ResponseEntity.ok(equipementRepo.save(e));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Supprimer un équipement par ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public void delete(@PathVariable Long id) {
        equipementRepo.deleteById(id);
    }

    @Operation(summary = "Lister les équipements disponibles dans une période")
    @GetMapping("/disponibles")
    public ResponseEntity<List<Equipement>> getAvailableBetween(
            @Parameter(description = "Date de début (format ISO, ex: 2025-04-24T08:00)") @RequestParam("debut") String debut,
            @Parameter(description = "Date de fin (format ISO)") @RequestParam("fin") String fin,
            @Parameter(description = "État requis de l’équipement") @RequestParam("etat") EtatEquipement etat) {
        try {
            LocalDateTime dateDebut = LocalDateTime.parse(debut);
            LocalDateTime dateFin = LocalDateTime.parse(fin);
            List<Equipement> disponibles = equipementRepo.findAvailableBetween(dateDebut, dateFin, etat);
            return ResponseEntity.ok(disponibles);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
