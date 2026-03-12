package com.nnk.poseidon.services;

import com.nnk.poseidon.domain.Rating;
import com.nnk.poseidon.domain.dto.RatingRequest;
import com.nnk.poseidon.repositories.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    @Override
    public List<Rating> getRatings() {
        return ratingRepository.findAll();
    }

    @Override
    public Optional<Rating> getRatingById(Integer id) {
        return ratingRepository.findById(id);
    }

    @Override
    public Rating saveRating(RatingRequest request) {
        if (request.id() != null && ratingRepository.findById(request.id()).isPresent()) {
            throw new IllegalArgumentException("Rating already exist");
        }
        Rating rating = new Rating();
        rating.setMoodysRating(request.moodysRating());
        rating.setSandPRating(request.sandPRating());
        rating.setFitchRating(request.fitchRating());
        rating.setOrderNumber(request.orderNumber());
        return ratingRepository.save(rating);
    }

    @Override
    public Rating updateRating(Integer id, RatingRequest request) {
        Rating rating = getRatingById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Rating Id:" + id));
        rating.setMoodysRating(request.moodysRating());
        rating.setSandPRating(request.sandPRating());
        rating.setFitchRating(request.fitchRating());
        rating.setOrderNumber(request.orderNumber());
        return ratingRepository.save(rating);
    }

    @Override
    public void deleteRatingById(Integer id) {
        Rating rating = getRatingById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Rating Id:" + id));
        ratingRepository.delete(rating);
    }

    @Override
    public RatingRequest getByIdAsRequest(Integer id) {
        Rating rating = getRatingById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Rating Id:" + id));
        return new RatingRequest(
                rating.getId(),
                rating.getMoodysRating(),
                rating.getSandPRating(),
                rating.getFitchRating(),
                rating.getOrderNumber()
        );
    }
}
