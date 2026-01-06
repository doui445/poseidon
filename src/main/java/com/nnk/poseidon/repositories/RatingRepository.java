package com.nnk.poseidon.repositories;

import com.nnk.poseidon.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
}
