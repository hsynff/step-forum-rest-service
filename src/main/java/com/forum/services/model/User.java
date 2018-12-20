package com.forum.services.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String email;
    private String password;
    private String lastName;
    private String firstName;
    private String token;
    private Integer status;
    private String imagePath;
    private Role role;

}
