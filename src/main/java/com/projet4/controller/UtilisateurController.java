package com.projet4.controller;

import com.projet4.model.Utilisateur;
import com.projet4.model.enums.Role;
import com.projet4.repository.UtilisateurRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Utilisateurs", description = "Gestion des utilisateurs (profil, rôles, etc.)")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurRepository utilisateurRepo;

    @Operation(summary = "Obtenir son propre profil utilisateur")
    public ResponseEntity<Utilisateur> getMyProfile(Authentication auth) {
        // 1. Récupère l'email depuis le token JWT injecté par Spring Security
        String email = auth.getName();

        // 2. Charge l'entité Utilisateur depuis la DB
        Utilisateur user = utilisateurRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));


        Utilisateur dto = new Utilisateur();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());

        // 4. Renvoie le DTO
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Lister tous les utilisateurs")
    @GetMapping
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public List<Utilisateur> getAllUsers() {
        return utilisateurRepo.findAll();
    }

    @Operation(summary = "Obtenir un utilisateur par son ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return utilisateurRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Mettre à jour un utilisateur")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Utilisateur updated) {
        return utilisateurRepo.findById(id).map(u -> {
            u.setName(updated.getName());
            u.setEmail(updated.getEmail());
            u.setRole(updated.getRole());
            utilisateurRepo.save(u);
            return ResponseEntity.ok(u);
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Supprimer un utilisateur par ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public void deleteUser(@PathVariable Long id) {
        utilisateurRepo.deleteById(id);
    }

    @Operation(summary = "Rechercher un utilisateur par nom")
    @GetMapping("/by-name")
    public ResponseEntity<Optional<Utilisateur>> getByName(@RequestParam String name) {
        return ResponseEntity.ok(utilisateurRepo.findByName(name));
    }

    @Operation(summary = "Rechercher un utilisateur par nom ou email")
    @GetMapping("/search")
    public List<Utilisateur> findByNameOrEmail(@RequestParam String value) {
        return utilisateurRepo.findByNameOrEmail(value);
    }

    @Operation(summary = "Lister les utilisateurs par rôle")
    @GetMapping("/role")
    public List<Utilisateur> getByRole(@RequestParam Role role) {
        return utilisateurRepo.findByRole(role);
    }

    @Operation(summary = "Compter les utilisateurs par rôle")
    @GetMapping("/count-by-role")
    public long countByRole(@RequestParam Role role) {
        return utilisateurRepo.countByRole(role);
    }

    @Operation(summary = "Vérifier si un nom d'utilisateur existe")
    @GetMapping("/exists-by-name")
    public boolean existsByName(@RequestParam String name) {
        return utilisateurRepo.existsByName(name);
    }
}
