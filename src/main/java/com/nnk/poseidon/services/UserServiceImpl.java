package com.nnk.poseidon.services;

import com.nnk.poseidon.domain.User;
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
    public User saveUser(User user) {
        if (user.getId() != null) {
            User existingUser = userRepository.findById(user.getId()).orElse(null);
            if (existingUser != null) {
                if (user.getPassword() == null || user.getPassword().isEmpty()) {
                    user.setPassword(existingUser.getPassword());
                } else {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                }
            }
        }
        return userRepository.save(user);
    }

    @Override
    public void updateUser(UserUpdateDTO userUpdates) {
        User user = userRepository.findById(userUpdates.id())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userUpdates.id()));

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
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}
