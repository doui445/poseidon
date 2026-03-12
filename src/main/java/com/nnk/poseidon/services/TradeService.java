package com.nnk.poseidon.services;

import com.nnk.poseidon.domain.Trade;
import com.nnk.poseidon.domain.dto.TradeRequest;

import java.util.List;
import java.util.Optional;

public interface TradeService {
    List<Trade> getTrades();

    Optional<Trade> getTradeById(Integer id);

    Trade saveTrade(TradeRequest request);

    Trade updateTrade(Integer id, TradeRequest request);

    void deleteTradeById(Integer id);

    TradeRequest getByIdAsRequest(Integer id);
}
