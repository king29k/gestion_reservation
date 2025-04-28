package com.projet4.service;

import com.projet4.model.Utilisateur;
import com.projet4.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    public List<Utilisateur> getAll() {
        return utilisateurRepository.findAll();
    }

    public Optional<Utilisateur> getById(Long id) {
        return utilisateurRepository.findById(id);
    }

    public Optional<Utilisateur> getByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    public Utilisateur save(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    public void delete(Long id) {
        utilisateurRepository.deleteById(id);
    }
}
