package com.nnk.poseidon.service.security;

import com.nnk.poseidon.domain.User;
import com.nnk.poseidon.repositories.UserRepository;
import com.nnk.poseidon.services.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

public class UserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private AutoCloseable mocks;

    private User user;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        user = User.builder()
                .username("username")
                .password("password")
                .role("USER")
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    @DisplayName("Load User - Success")
    void givenExistingUsername_whenLoadUserByUsername_thenReturnUserDetails() {
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.ofNullable(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(user.getUsername());
        assertThat(userDetails.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    @DisplayName("Load User - Not Found")
    void givenNonExistingUsername_whenLoadUserByUsername_thenThrowException() {
        given(userRepository.findByUsername("nonExistentUsername")).willReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("nonExistentUsername")
        );
    }
}
