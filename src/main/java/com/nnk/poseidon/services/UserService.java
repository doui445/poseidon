package com.nnk.poseidon.services;

import com.nnk.poseidon.domain.User;
import com.nnk.poseidon.domain.dto.UserRegistrationDTO;
import com.nnk.poseidon.domain.dto.UserUpdateDTO;

import java.util.Optional;

public interface UserService {

    Iterable<User> getUsers();

    Optional<User> getUserById(Integer id);

    Optional<User> getUserByUsername(String username);

    User saveUser(UserRegistrationDTO userRegistrations);

    User updateUser(UserUpdateDTO userUpdates);

    void deleteUserById(Integer id);

    UserUpdateDTO getUpdateDTO(Integer id);
}
