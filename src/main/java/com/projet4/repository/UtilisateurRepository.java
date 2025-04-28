package com.projet4.repository;

import com.projet4.model.Utilisateur;
import com.projet4.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<Utilisateur> findByName(String name);
    Optional<Utilisateur> findByNameAndEmail(String name, String email);
    List<Utilisateur> findByRole(Role role);
    long countByRole(Role role);
    boolean existsByName(String name);
    @Query("SELECT u FROM Utilisateur u WHERE u.name = :value OR u.email = :value")
    List<Utilisateur> findByNameOrEmail(@Param("value") String value);

}
