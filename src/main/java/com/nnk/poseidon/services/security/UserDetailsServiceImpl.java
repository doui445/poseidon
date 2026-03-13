package com.nnk.poseidon.services.security;

import com.nnk.poseidon.domain.User;
import com.nnk.poseidon.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Bridge service between the database and Spring Security.
 * <p>
 * Allows to use database user's information for authentication.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Retrieves a user from the database and converts it in {@link UserDetails} for Spring Security.
     *
     * @param username the user's login username.
     * @return A {@link UserDetails} object containing identifiers and roles.
     * @throws UsernameNotFoundException if the user does not exist in the database.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with this username: " + username));

        UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(user.getUsername());
        builder.password(user.getPassword());
        builder.roles(user.getRole());

        return builder.build();
    }
}
