package com.projet4.controller;

import com.projet4.dto.AuthResponse;
import com.projet4.dto.LoginRequest;
import com.projet4.dto.RegisterRequest;
import com.projet4.model.Utilisateur;
import com.projet4.repository.UtilisateurRepository;
import com.projet4.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentification", description = "Endpoints pour login et inscription")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UtilisateurRepository utilisateurRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Operation(summary = "Connexion", description = "Retourne un token JWT si les identifiants sont valides")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        UserDetails user = (UserDetails) auth.getPrincipal();

        Utilisateur utilisateur = utilisateurRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

        String token = jwtService.generateToken(user); // méthode sans paramètre "role"
        AuthResponse response = new AuthResponse(token, utilisateur.getRole().name(), utilisateur.getName());

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Inscription", description = "Permet à un chercheur ou gestionnaire de s'enregistrer")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (utilisateurRepo.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email déjà utilisé");
        }

        Utilisateur user = new Utilisateur();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        utilisateurRepo.save(user);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Récupérer l'utilisateur courant")
    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();

        Utilisateur user = utilisateurRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        Map<String, Object> dto = Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole()
        );

        return ResponseEntity.ok(dto);
    }
}
