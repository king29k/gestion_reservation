package com.projet4.service;

import com.projet4.model.Equipement;
import com.projet4.model.enums.EtatEquipement;
import com.projet4.repository.EquipementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EquipementService {

    private final EquipementRepository equipementRepository;

    public List<Equipement> getAll() {
        return equipementRepository.findAll();
    }

    public List<Equipement> findByType(String type) {
        return equipementRepository.findByType(type);
    }

    public List<Equipement> findByEtat(EtatEquipement etat) {
        return equipementRepository.findByEtat(etat);
    }

    public List<Equipement> findByLaboratoire(String lab) {
        return equipementRepository.findByLaboratoire(lab);
    }

    public Optional<Equipement> getById(Long id) {
        return equipementRepository.findById(id);
    }

    public Equipement save(Equipement e) {
        return equipementRepository.save(e);
    }

    public void delete(Long id) {
        equipementRepository.deleteById(id);
    }
}
