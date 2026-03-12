package com.nnk.poseidon.service;

import com.nnk.poseidon.domain.Trade;
import com.nnk.poseidon.domain.dto.TradeRequest;
import com.nnk.poseidon.repositories.TradeRepository;
import com.nnk.poseidon.services.TradeServiceImpl;
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

public class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    private TradeServiceImpl tradeService;

    private AutoCloseable mocks;

    private Trade trade;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        trade = Trade.builder()
                .id(1)
                .account("account")
                .type("type")
                .buyQuantity(10.0)
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    @DisplayName("getTradeById should return a trade when trade exists")
    void testGetTradeByIdFound() {
        given(tradeRepository.findById(1)).willReturn(Optional.of(trade));

        Optional<Trade> result = tradeService.getTradeById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getAccount()).isEqualTo("account");
        verify(tradeRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("getTradeById should return empty when trade does not exist")
    void testGetTradeByIdNotFound() {
        given(tradeRepository.findById(2)).willReturn(Optional.empty());

        Optional<Trade> result = tradeService.getTradeById(2);

        assertThat(result).isEmpty();
        verify(tradeRepository, times(1)).findById(2);
    }

    @Test
    @DisplayName("saveTrade should save and return the trade")
    void testSaveTrade() {
        given(tradeRepository.save(any())).willReturn(trade);

        TradeRequest request = new TradeRequest(trade.getId(), trade.getAccount(), trade.getType(), trade.getBuyQuantity());
        Trade savedTrade = tradeService.saveTrade(request);

        assertThat(savedTrade).isNotNull();
        assertThat(savedTrade.getAccount()).isEqualTo("account");
        verify(tradeRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("updateTrade should update and return the trade")
    void testUpdateTrade() {
        Trade newTrade = trade;
        newTrade.setAccount("newAccount");

        given(tradeRepository.save(trade)).willReturn(newTrade);
        given(tradeRepository.findById(1)).willReturn(Optional.of(trade));

        TradeRequest request = new TradeRequest(trade.getId(), "newAccount", trade.getType(), trade.getBuyQuantity());
        Trade savedTrade = tradeService.updateTrade(1, request);

        assertThat(savedTrade).isNotNull();
        assertThat(savedTrade.getAccount()).isEqualTo("newAccount");
        verify(tradeRepository, times(1)).save(trade);
    }

    @Test
    @DisplayName("deleteTradeById should delete trade by id")
    void testDeleteTradeByIdById() {
        given(tradeRepository.findById(1)).willReturn(Optional.of(trade));

        tradeService.deleteTradeById(1);

        verify(tradeRepository, times(1)).delete(trade);
    }
}
