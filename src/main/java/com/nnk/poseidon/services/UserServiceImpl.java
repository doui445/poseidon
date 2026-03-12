package com.nnk.poseidon.services;

import com.nnk.poseidon.domain.User;
import com.nnk.poseidon.domain.dto.UserRegistrationDTO;
import com.nnk.poseidon.domain.dto.UserUpdateDTO;
import com.nnk.poseidon.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User saveUser(UserRegistrationDTO userRegistrations) {
        if (userRegistrations.username() != null && userRepository.findByUsername(userRegistrations.username()).isPresent()) {
            throw new IllegalArgumentException("User already exist");
        }
        User user = new User();
        user.setUsername(userRegistrations.username());
        if (!userRegistrations.password().matches(PASSWORD_PATTERN)) {
            throw new IllegalArgumentException("Password does not meet security requirements");
        }
        user.setFullname(userRegistrations.fullname());
        user.setPassword(passwordEncoder.encode(userRegistrations.password()));
        user.setRole(userRegistrations.role());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(UserUpdateDTO userUpdates) {
        User user = userRepository.findById(userUpdates.id())
                .orElseThrow(() -> new IllegalArgumentException("Invalid User id: " + userUpdates.id()));
        user.setUsername(userUpdates.username());
        user.setFullname(userUpdates.fullname());
        if (userUpdates.role() != null && !userUpdates.role().isBlank()) user.setRole(userUpdates.role());
        String newPassword = userUpdates.password();
        if (newPassword != null && !newPassword.isBlank()) {
            if (!newPassword.matches(PASSWORD_PATTERN)) {
                throw new IllegalArgumentException("Password does not meet security requirements");
            }
            user.setPassword(passwordEncoder.encode(newPassword));
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User Id:" + id));
        userRepository.delete(user);
    }

    @Override
    public UserUpdateDTO getUpdateDTO(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User Id:" + id));
        return new UserUpdateDTO(
                user.getId(),
                user.getUsername(),
                "",
                user.getFullname(),
                user.getRole()
        );
    }
}
