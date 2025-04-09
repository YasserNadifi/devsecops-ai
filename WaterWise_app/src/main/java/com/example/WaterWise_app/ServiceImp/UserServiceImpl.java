package com.example.WaterWise_app.ServiceImp;

import com.example.WaterWise_app.Dto.UserRequestDTO;
import com.example.WaterWise_app.Dto.UserResponseDTO;
import com.example.WaterWise_app.Entity.User;
import com.example.WaterWise_app.Mapper.UserMapper;
import com.example.WaterWise_app.Repository.UserRepo;
import com.example.WaterWise_app.ServiceInterface.UserInterfaceService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserInterfaceService {

    private final UserRepo userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepo userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        // Vérifie si l'username existe déjà
        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
            throw new RuntimeException("Username already exists!");
        }

        // Convertit le DTO en User et hash le mot de passe
        User user = userMapper.toUser(userRequestDTO);
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));

        // Sauvegarde l'utilisateur en base de données
        User savedUser = userRepository.save(user);

        // Retourne le DTO de réponse
        return userMapper.toUserResponseDTO(savedUser);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        // Récupère l'utilisateur par son ID ou lance une exception si non trouvé
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!")); // À personnaliser

        return userMapper.toUserResponseDTO(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username); // Délègue la requête au repository
    }
}
