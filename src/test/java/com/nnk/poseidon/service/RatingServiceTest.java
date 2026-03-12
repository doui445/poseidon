package com.nnk.poseidon.service;

import com.nnk.poseidon.domain.Rating;
import com.nnk.poseidon.domain.dto.RatingRequest;
import com.nnk.poseidon.repositories.RatingRepository;
import com.nnk.poseidon.services.RatingServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RatingServiceTest {
    
    @Mock
    private RatingRepository ratingRepository;
    
    @InjectMocks
    private RatingServiceImpl ratingService;
    
    private AutoCloseable mocks;
    
    private Rating rating;
    
    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        
        rating = Rating.builder()
                .id(1)
                .moodysRating("moody's")
                .sandPRating("sandP")
                .fitchRating("fitch")
                .orderNumber(1)
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    @DisplayName("getRatingById should return a rating when rating exists")
    void testGetRatingByIdFound() {
        given(ratingRepository.findById(1)).willReturn(Optional.of(rating));

        Optional<Rating> result = ratingService.getRatingById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getMoodysRating()).isEqualTo("moody's");
        verify(ratingRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("getRatingById should return empty when rating does not exist")
    void testGetRatingByIdNotFound() {
        given(ratingRepository.findById(2)).willReturn(Optional.empty());

        Optional<Rating> result = ratingService.getRatingById(2);

        assertThat(result).isEmpty();
        verify(ratingRepository, times(1)).findById(2);
    }

    @Test
    @DisplayName("saveRating should save and return the rating")
    void testSaveRating() {
        given(ratingRepository.save(any())).willReturn(rating);

        RatingRequest request = new RatingRequest(rating.getId(), rating.getMoodysRating(), rating.getSandPRating(), rating.getFitchRating(), rating.getOrderNumber());
        Rating savedRating = ratingService.saveRating(request);

        assertThat(savedRating).isNotNull();
        assertThat(savedRating.getMoodysRating()).isEqualTo("moody's");
        verify(ratingRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("updateRating should update and return the rating")
    void testUpdateRating() {
        Rating newRating = rating;
        newRating.setMoodysRating("great !");

        given(ratingRepository.save(rating)).willReturn(newRating);
        given(ratingRepository.findById(1)).willReturn(Optional.of(rating));

        RatingRequest request = new RatingRequest(rating.getId(), "great !", rating.getSandPRating(), rating.getFitchRating(), rating.getOrderNumber());
        Rating savedRating = ratingService.updateRating(1, request);

        assertThat(savedRating).isNotNull();
        assertThat(savedRating.getMoodysRating()).isEqualTo("great !");
        verify(ratingRepository, times(1)).save(rating);
    }

    @Test
    @DisplayName("deleteRatingById should delete rating by id")
    void testDeleteRatingByIdById() {
        given(ratingRepository.findById(1)).willReturn(Optional.of(rating));

        ratingService.deleteRatingById(1);

        verify(ratingRepository, times(1)).delete(rating);
    }
}
