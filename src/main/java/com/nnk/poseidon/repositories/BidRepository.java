package com.nnk.poseidon.repositories;

import com.nnk.poseidon.domain.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Integer> {
}
