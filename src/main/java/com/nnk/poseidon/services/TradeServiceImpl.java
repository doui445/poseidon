package com.nnk.poseidon.services;

import com.nnk.poseidon.domain.Trade;
import com.nnk.poseidon.domain.dto.TradeRequest;
import com.nnk.poseidon.repositories.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TradeServiceImpl implements TradeService {

    private final TradeRepository tradeRepository;

    @Override
    public List<Trade> getTrades() {
        return tradeRepository.findAll();
    }

    @Override
    public Optional<Trade> getTradeById(Integer id) {
        return tradeRepository.findById(id);
    }

    @Override
    public Trade saveTrade(TradeRequest request) {
        if (request.id() != null && tradeRepository.findById(request.id()).isPresent()) {
            throw new IllegalArgumentException("Trade already exist");
        }
        Trade trade = new Trade();
        trade.setAccount(request.account());
        trade.setType(request.type());
        trade.setBuyQuantity(request.buyQuantity());
        trade.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
        return tradeRepository.save(trade);
    }

    @Override
    public Trade updateTrade(Integer id, TradeRequest request) {
        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Trade Id:" + id));
        trade.setAccount(request.account());
        trade.setType(request.type());
        trade.setBuyQuantity(request.buyQuantity());
        return tradeRepository.save(trade);
    }

    @Override
    public void deleteTradeById(Integer id) {
        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Trade Id:" + id));
        tradeRepository.delete(trade);
    }

    @Override
    public TradeRequest getByIdAsRequest(Integer id) {
        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Trade Id:" + id));
        return new TradeRequest(
                trade.getId(),
                trade.getAccount(),
                trade.getType(),
                trade.getBuyQuantity()
        );
    }
}
