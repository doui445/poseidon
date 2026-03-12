package com.nnk.poseidon.service;

import com.nnk.poseidon.domain.User;
import com.nnk.poseidon.domain.dto.UserRegistrationDTO;
import com.nnk.poseidon.domain.dto.UserUpdateDTO;
import com.nnk.poseidon.repositories.UserRepository;
import com.nnk.poseidon.services.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private AutoCloseable mocks;

    private User user;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1)
                .username("test")
                .password("Password123%")
                .fullname("testUser")
                .role("USER")
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    @DisplayName("getUserById should return a user when user exists")
    void testGetUserByIdFound() {
        given(userRepository.findById(1)).willReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("test");
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("getUserById should return empty when user does not exist")
    void testGetUserByIdNotFound() {
        given(userRepository.findById(2)).willReturn(Optional.empty());

        Optional<User> result = userService.getUserById(2);

        assertThat(result).isEmpty();
        verify(userRepository, times(1)).findById(2);
    }

    @Test
    @DisplayName("saveUser should save and return the user")
    void testSaveUser() {
        given(userRepository.save(any())).willReturn(user);

        UserRegistrationDTO registrations = new UserRegistrationDTO(user.getUsername(), user.getPassword(), user.getPassword(), user.getFullname(), user.getRole());
        User savedUser = userService.saveUser(registrations);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("test");
        verify(userRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("updateUser should update and return the user")
    void testUpdateUser() {
        User newUser = user;
        newUser.setFullname("newFullname");

        given(userRepository.save(user)).willReturn(newUser);
        given(userRepository.findById(1)).willReturn(Optional.of(user));

        UserUpdateDTO updates = new UserUpdateDTO(user.getId(), user.getUsername(), user.getPassword(), "newFullname", user.getRole());
        User savedUser = userService.updateUser(updates);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getFullname()).isEqualTo("newFullname");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("deleteUserById should delete user by id")
    void testDeleteUserByIdById() {
        given(userRepository.findById(1)).willReturn(Optional.of(user));

        userService.deleteUserById(1);

        verify(userRepository, times(1)).delete(user);
    }
}
