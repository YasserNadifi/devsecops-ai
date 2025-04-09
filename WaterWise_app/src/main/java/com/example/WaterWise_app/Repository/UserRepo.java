package com.example.WaterWise_app.Repository;

import com.example.WaterWise_app.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    // Trouve un utilisateur par son username (utile pour la connexion)
    Optional<User> findByUsername(String username);

    // Vérifie si un username existe déjà (éviter les doublons)
    boolean existsByUsername(String username);

}
