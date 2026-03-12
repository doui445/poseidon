package com.nnk.poseidon.it;

import com.nnk.poseidon.domain.Rating;
import com.nnk.poseidon.repositories.RatingRepository;
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
public class RatingTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RatingRepository ratingRepository;

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Should display the rating list page with updated data from database")
    public void ratingIntegrationTest() throws Exception {
        Rating rating = Rating.builder()
                .moodysRating("Integration Moodys Rating")
                .sandPRating("Sand P Rating")
                .fitchRating("Fitch Rating")
                .orderNumber(10)
                .build();

        // Save
        rating = ratingRepository.save(rating);
        assertNotNull(rating.getId());
        assertEquals(10, (int) rating.getOrderNumber());

        // Update
        rating.setOrderNumber(20);
        rating = ratingRepository.save(rating);
        assertEquals(20, (int) rating.getOrderNumber());

        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"))
                .andExpect(model().attributeExists("ratings"))
                .andExpect(content().string(containsString("Integration Moodys Rating")))
                .andExpect(content().string(containsString("20")));

        // Find
        List<Rating> listResult = ratingRepository.findAll();
        assertFalse(listResult.isEmpty());

        // Delete
        Integer id = rating.getId();
        ratingRepository.delete(rating);
        Optional<Rating> optionalRating = ratingRepository.findById(id);
        assertFalse(optionalRating.isPresent());
    }
}
