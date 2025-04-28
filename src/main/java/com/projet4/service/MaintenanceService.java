package com.projet4.service;

import com.projet4.model.Maintenance;
import com.projet4.repository.MaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;

    public List<Maintenance> getAll() {
        return maintenanceRepository.findAll();
    }

    public List<Maintenance> findByEquipementId(Long id) {
        return maintenanceRepository.findByEquipementId(id);
    }

    public Optional<Maintenance> getById(Long id) {
        return maintenanceRepository.findById(id);
    }

    public Maintenance save(Maintenance m) {
        return maintenanceRepository.save(m);
    }

    public void delete(Long id) {
        maintenanceRepository.deleteById(id);
    }
}
