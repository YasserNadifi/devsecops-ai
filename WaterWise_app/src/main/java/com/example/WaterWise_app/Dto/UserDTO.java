package com.example.WaterWise_app.Dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String password; // À masquer ou crypter côté sécurité réelle
}
