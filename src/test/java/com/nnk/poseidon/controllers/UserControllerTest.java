package com.nnk.poseidon.controllers;

import com.nnk.poseidon.configuration.SpringSecurityConfig;
import com.nnk.poseidon.domain.dto.UserRegistrationDTO;
import com.nnk.poseidon.domain.dto.UserUpdateDTO;
import com.nnk.poseidon.services.UserService;
import com.nnk.poseidon.services.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SpringSecurityConfig.class)
@WithMockUser(roles = "ADMIN")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("GET /user/list - Should return list view")
    void usersPage_shouldRenderUserListView() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    @DisplayName("GET /user/add - Should show add form with empty request")
    void getAddUser_shouldShowForm() throws Exception {
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @DisplayName("GET /user/update/{id} - Should show update form with prefilled request")
    void getUpdateUser_shouldShowForm() throws Exception {
        UserUpdateDTO updates = new UserUpdateDTO(1, "Test", "", "Test User", "USER");
        given(userService.getUpdateDTO(1)).willReturn(updates);
        mockMvc.perform(get("/user/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attribute("user", updates));
    }

    @Test
    @DisplayName("POST /user/validate - Success")
    void postValidate_shouldSaveUserAndRedirectToList() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .param("username", "Test")
                        .param("password", "Test123%")
                        .param("confirmPassword", "Test123%")
                        .param("fullname", "Test User")
                        .param("role", "USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));
        verify(userService).saveUser(any(UserRegistrationDTO.class));
    }

    @Test
    @DisplayName("POST /user/validate - Failure")
    void postValidate_shouldReturnToForm() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .param("username", "Test")
                        .param("password", "Test123%")
                        .param("confirmPassword", "AAA")
                        .param("fullname", "Test User")
                        .param("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().hasErrors());
        verify(userService, never()).saveUser(any(UserRegistrationDTO.class));
    }

    @Test
    @DisplayName("POST /user/update/{id} - Success")
    void postUpdateUser_shouldUpdateUserAndRedirectToList() throws Exception {
        mockMvc.perform(post("/user/update/1")
                        .with(csrf())
                        .param("id", "1")
                        .param("username", "Test")
                        .param("password", "")
                        .param("fullname", "Updated User")
                        .param("role", "ADMIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));
        verify(userService).updateUser(any(UserUpdateDTO.class));
    }

    @Test
    @DisplayName("GET /user/delete/{id} - Success")
    void getDeleteUser_shouldDeleteUserAndRedirectToList() throws Exception {
        mockMvc.perform(get("/user/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));
        verify(userService).deleteUserById(anyInt());
    }
}
