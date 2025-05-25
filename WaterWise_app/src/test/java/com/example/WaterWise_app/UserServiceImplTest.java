package com.example.WaterWise_app;

import com.example.WaterWise_app.Dto.UserDTO;
import com.example.WaterWise_app.Entity.User;
import com.example.WaterWise_app.Repository.UserRepository;
import com.example.WaterWise_app.ServiceImp.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDTO userDTO;
    private User user;

    @BeforeEach
    void setup() {
        userDTO = new UserDTO();
        userDTO.setId(null);
        userDTO.setUsername("testuser");
        userDTO.setPassword("plainPassword");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
    }

    @Test
    void registerUser_ShouldSaveUser_WhenUsernameNotTaken() {
        when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.registerUser(userDTO);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());

        verify(userRepository).existsByUsername(userDTO.getUsername());
        verify(passwordEncoder).encode(userDTO.getPassword());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals("testuser", savedUser.getUsername());
    }

    @Test
    void registerUser_ShouldThrowException_WhenUsernameTaken() {
        when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.registerUser(userDTO));
        assertEquals("Nom d'utilisateur déjà pris.", ex.getMessage());

        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserByUsername_ShouldReturnUserDTO_WhenUserExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserDTO result = userService.getUserByUsername("testuser");

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
    }

    @Test
    void getUserByUsername_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.getUserByUsername("testuser"));
        assertEquals("Utilisateur introuvable", ex.getMessage());
    }

    @Test
    void login_ShouldReturnUserDTO_WhenCredentialsCorrect() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plainPassword", user.getPassword())).thenReturn(true);

        UserDTO result = userService.login("testuser", "plainPassword");

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
    }

    @Test
    void login_ShouldThrowException_WhenUsernameIncorrect() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.login("testuser", "anyPassword"));
        assertEquals("Nom d'utilisateur incorrect.", ex.getMessage());
    }

    @Test
    void login_ShouldThrowException_WhenPasswordIncorrect() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", user.getPassword())).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.login("testuser", "wrongPassword"));
        assertEquals("Mot de passe incorrect.", ex.getMessage());
    }
}