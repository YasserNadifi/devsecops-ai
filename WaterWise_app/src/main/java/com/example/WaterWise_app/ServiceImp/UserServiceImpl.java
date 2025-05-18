package com.example.WaterWise_app.ServiceImp;

import com.example.WaterWise_app.Dto.UserDTO;
import com.example.WaterWise_app.Entity.User;
import com.example.WaterWise_app.Mapper.UserMapper;
import com.example.WaterWise_app.Repository.UserRepository;
import com.example.WaterWise_app.ServiceInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Nom d'utilisateur déjà pris.");
        }

        User user = UserMapper.toEntity(userDTO);
        user.setPassword(user.getPassword()); // Pas de hash ici
        User saved = userRepository.save(user);
        return UserMapper.toDTO(saved);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Utilisateur introuvable");
        }
        return UserMapper.toDTO(userOpt.get());
    }

    @Override
    public UserDTO login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Nom d'utilisateur incorrect.");
        }

        User user = userOpt.get();

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Mot de passe incorrect.");
        }

        return UserMapper.toDTO(user);
    }

}
