package com.nnk.poseidon.services;

import com.nnk.poseidon.domain.Rating;
import com.nnk.poseidon.domain.dto.RatingRequest;

import java.util.List;
import java.util.Optional;

public interface RatingService {

    List<Rating> getRatings();

    Optional<Rating> getRatingById(Integer id);

    Rating saveRating(RatingRequest request);

    Rating updateRating(Integer id, RatingRequest request);

    void deleteRatingById(Integer id);

    RatingRequest getByIdAsRequest(Integer id);
}
