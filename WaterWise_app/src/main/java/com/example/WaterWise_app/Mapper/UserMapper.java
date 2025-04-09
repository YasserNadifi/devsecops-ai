package com.example.WaterWise_app.Mapper;

import com.example.WaterWise_app.Dto.UserRequestDTO;
import com.example.WaterWise_app.Dto.UserResponseDTO;
import com.example.WaterWise_app.Entity.User;

public class UserMapper {
    // Convertit UserRequestDTO -> User
    public User toUser(UserRequestDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        return user;
    }

    // Convertit User -> UserResponseDTO
    public UserResponseDTO toUserResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        return dto;
    }
}
