package com.nnk.poseidon.it;

import com.nnk.poseidon.domain.User;
import com.nnk.poseidon.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should display the user list page with updated data from database")
    public void userIntegrationTest() throws Exception {
        User user = User.builder()
                .username("user")
                .password("Password123%")
                .fullname("Integration User")
                .role("USER")
                .build();

        // Save
        user = userRepository.save(user);
        assertNotNull(user.getId());
        assertEquals("user", user.getUsername());

        // Update
        user.setRole("ADMIN");
        user = userRepository.save(user);
        assertEquals("ADMIN", user.getRole());

        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"))
                .andExpect(content().string(containsString("Integration User")))
                .andExpect(content().string(containsString("ADMIN")));

        // Find
        List<User> listResult = userRepository.findAll();
        assertFalse(listResult.isEmpty());

        // Delete
        Integer id = user.getId();
        userRepository.delete(user);
        Optional<User> optionalUser = userRepository.findById(id);
        assertFalse(optionalUser.isPresent());
    }
}
