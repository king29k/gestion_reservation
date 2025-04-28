package com.projet4.controller;

import com.projet4.model.Equipement;
import com.projet4.model.Maintenance;
import com.projet4.repository.EquipementRepository;
import com.projet4.repository.MaintenanceRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/maintenances")
@Tag(name = "Maintenances", description = "Gestion des maintenances des équipements")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceRepository maintenanceRepo;
    private final EquipementRepository equipementRepo;

    @Operation(summary = "Lister toutes les maintenances (ou par équipement)")
    @GetMapping
    public List<Maintenance> getAll(@Parameter(description = "ID de l'équipement") @RequestParam(required = false) Long equipementId) {
        if (equipementId != null) return maintenanceRepo.findByEquipementId(equipementId);
        return maintenanceRepo.findAll();
    }

    @Operation(summary = "Obtenir une maintenance par ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        return maintenanceRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Créer une nouvelle maintenance")
    @PostMapping
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<?> create(@RequestBody Maintenance m) {
        Equipement eq = equipementRepo.findById(m.getEquipement().getId()).orElseThrow();
        m.setEquipement(eq);
        return new ResponseEntity<>(maintenanceRepo.save(m), HttpStatus.CREATED);
    }

    @Operation(summary = "Mettre à jour une maintenance")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Maintenance updated) {
        return maintenanceRepo.findById(id).map(m -> {
            m.setDate(updated.getDate());
            m.setActions(updated.getActions());
            return ResponseEntity.ok(maintenanceRepo.save(m));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Supprimer une maintenance")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public void delete(@PathVariable Long id) {
        maintenanceRepo.deleteById(id);
    }

    @Operation(summary = "Rechercher les maintenances à une date précise")
    @GetMapping("/date")
    public List<Maintenance> getByDate(@RequestParam("date") String date) {
        return maintenanceRepo.findByDate(LocalDate.parse(date));
    }

    @Operation(summary = "Rechercher les maintenances dans une période")
    @GetMapping("/periode")
    public List<Maintenance> getByDateRange(@RequestParam("debut") String debut,
                                            @RequestParam("fin") String fin) {
        return maintenanceRepo.findByDateBetween(LocalDate.parse(debut), LocalDate.parse(fin));
    }

    @Operation(summary = "Rechercher par mot-clé dans les actions de maintenance")
    @GetMapping("/recherche")
    public List<Maintenance> searchActions(@RequestParam("motcle") String keyword) {
        return maintenanceRepo.findByActionsContainingIgnoreCase(keyword);
    }

    @Operation(summary = "Historique des maintenances triées par date descendante")
    @GetMapping("/equipement/{id}/historique")
    public List<Maintenance> getHistorique(@PathVariable Long id) {
        return maintenanceRepo.findByEquipementIdOrderByDateDesc(id);
    }

    @Operation(summary = "Compter les maintenances d’un équipement")
    @GetMapping("/equipement/{id}/count")
    public long countByEquipement(@PathVariable Long id) {
        return maintenanceRepo.countByEquipementId(id);
    }

    @Operation(summary = "Lister les maintenances récentes d’un équipement après une date")
    @GetMapping("/equipement/{id}/recentes")
    public List<Maintenance> getRecentes(@PathVariable Long id, @RequestParam("apres") String date) {
        return maintenanceRepo.findRecentMaintenances(id, LocalDate.parse(date));
    }
}
