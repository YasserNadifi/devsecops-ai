package com.example.WaterWise_app.ServiceImp;

import com.example.WaterWise_app.Dto.UserDTO;
import com.example.WaterWise_app.Entity.User;
import com.example.WaterWise_app.Mapper.UserMapper;
import com.example.WaterWise_app.Repository.UserRepository;
import com.example.WaterWise_app.Repository.UserRepository;
import com.example.WaterWise_app.ServiceInterface.UserService;
import com.example.WaterWise_app.ServiceInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(); // Pour hasher les mots de passe
    }

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Nom d'utilisateur déjà pris.");
        }

        User user = UserMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword())); // hash du mot de passe
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
}
