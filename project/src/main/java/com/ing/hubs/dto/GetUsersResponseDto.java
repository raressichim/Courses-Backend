package com.ing.hubs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUsersResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;

}
