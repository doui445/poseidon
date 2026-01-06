package com.nnk.poseidon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "Id")
    private Integer id;

    @NotBlank(message = "Username is mandatory")
    @Column(name = "username", length = 125)
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Column(name = "password", length = 125)
    private String password;

    @NotBlank(message = "FullName is mandatory")
    @Column(name = "fullname", length = 125)
    private String fullname;

    @NotBlank(message = "Role is mandatory")
    @Column(name = "role", length = 125)
    private String role;
}
