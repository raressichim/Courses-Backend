package com.ing.hubs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String username;

    @Size(min = 4,message = "Password must be at least 4 characters long")
    private String password;

    @NotBlank
    private  String email;

    private String groupName;

    @NotBlank
    private String role;
}
