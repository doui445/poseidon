package com.nnk.poseidon.repositories;

import com.nnk.poseidon.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Integer> {
}
