package com.nnk.poseidon.controllers;

import com.nnk.poseidon.configuration.SpringSecurityConfig;
import com.nnk.poseidon.domain.dto.RatingRequest;
import com.nnk.poseidon.services.RatingService;
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

@WebMvcTest(RatingController.class)
@Import(SpringSecurityConfig.class)
@WithMockUser
public class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RatingService ratingService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("GET /rating/list - Should return list view")
    void ratingsPage_shouldRenderRatingListView() throws Exception {
        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"))
                .andExpect(model().attributeExists("ratings"));
    }

    @Test
    @DisplayName("GET /rating/add - Should show add form with empty request")
    void getAddRating_shouldShowForm() throws Exception {
        mockMvc.perform(get("/rating/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(model().attributeExists("rating"));
    }

    @Test
    @DisplayName("GET /rating/update/{id} - Should show update form with prefilled request")
    void getUpdateRating_shouldShowForm() throws Exception {
        RatingRequest request = new RatingRequest(1, "moodysTest", "sandPTest", "fitchTest", 1);
        given(ratingService.getByIdAsRequest(1)).willReturn(request);
        mockMvc.perform(get("/rating/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"))
                .andExpect(model().attribute("rating", request));
    }

    @Test
    @DisplayName("POST /rating/validate - Success")
    void postValidate_shouldSaveRatingAndRedirectToList() throws Exception {
        mockMvc.perform(post("/rating/validate")
                        .with(csrf())
                        .param("moodysRating", "moodysTest")
                        .param("sandPRating", "sandPTest")
                        .param("fitchRating", "fitchTest")
                        .param("orderNumber", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
        verify(ratingService).saveRating(any(RatingRequest.class));
    }

    @Test
    @DisplayName("POST /rating/validate - Failure")
    void postValidate_shouldReturnToForm() throws Exception {
        mockMvc.perform(post("/rating/validate")
                        .with(csrf())
                        .param("moodysRating", "moodysTest")
                        .param("sandPRating", "")
                        .param("fitchRating", "")
                        .param("orderNumber", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(model().hasErrors());
        verify(ratingService, never()).saveRating(any(RatingRequest.class));
    }

    @Test
    @DisplayName("POST /rating/update/{id} - Success")
    void postUpdateRating_shouldUpdateRatingAndRedirectToList() throws Exception {
        mockMvc.perform(post("/rating/update/1")
                        .with(csrf())
                        .param("moodysRating", "UpdatedMoodys")
                        .param("sandPRating", "UpdatedSandP")
                        .param("fitchRating", "UpdatedFitch")
                        .param("orderNumber", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
        verify(ratingService).updateRating(anyInt(), any(RatingRequest.class));
    }

    @Test
    @DisplayName("GET /rating/delete/{id} - Success")
    void getDeleteRating_shouldDeleteRatingAndRedirectToList() throws Exception {
        mockMvc.perform(get("/rating/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
        verify(ratingService).deleteRatingById(anyInt());
    }
}
