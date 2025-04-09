package com.example.WaterWise_app.Controller;

import com.example.WaterWise_app.Dto.UserRequestDTO;
import com.example.WaterWise_app.Dto.UserResponseDTO;
import com.example.WaterWise_app.ServiceInterface.UserInterfaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserInterfaceService userService;

    // Injection par constructeur (recommandé par Spring)
    public UserController(UserInterfaceService userService) {
        this.userService = userService;
    }

    // Création d'un utilisateur
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO response = userService.createUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Récupération par ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    // Récupération par username
    @GetMapping("/by-username/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(user -> ResponseEntity.ok(userService.getUserById(user.getId())))
                .orElse(ResponseEntity.notFound().build());
    }

}
