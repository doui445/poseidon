package com.nnk.poseidon.it.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // Setup mockMVC
public class SpringSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Access Login page - Authorized")
    void givenNoAuthentication_whenAccessLoginPage_thenAuthorized() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Access Protected page - Unauthorized")
    void givenNoAuthentication_whenAccessProtectedPage_thenRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/bidlist/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Access Admin page - Authorized")
    void givenAdminAuthentication_whenAccessAdminPage_thenAuthorized() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Access Admin page - Unauthorized")
    void givenUserAuthentication_whenAccessAdminPage_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect(status().isForbidden());
    }
}
