package com.example.WaterWise_app.ServiceInterface;

import com.example.WaterWise_app.Dto.UserDTO;
import com.example.WaterWise_app.Entity.User;

import java.util.Optional;

public interface UserService {
    UserDTO registerUser(UserDTO userDTO);
    UserDTO getUserByUsername(String username);
    UserDTO login(String username, String password);

}
