package com.example.WaterWise_app.ServiceInterface;

import com.example.WaterWise_app.Dto.UserRequestDTO;
import com.example.WaterWise_app.Dto.UserResponseDTO;
import com.example.WaterWise_app.Entity.User;

import java.util.Optional;

public interface UserInterfaceService {

    UserResponseDTO createUser(UserRequestDTO userRequestDTO);
    UserResponseDTO getUserById(Long id);
    Optional<User> findByUsername(String username); // Nouvelle méthode ajoutée
}
