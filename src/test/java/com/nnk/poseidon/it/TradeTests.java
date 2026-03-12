package com.nnk.poseidon.it;

import com.nnk.poseidon.domain.Trade;
import com.nnk.poseidon.repositories.TradeRepository;
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
public class TradeTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TradeRepository tradeRepository;

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Should display the trade list page with updated data from database")
    public void tradeTest() throws Exception {
        Trade trade = Trade.builder()
                .account("Integration Trade Account")
                .type("Type")
                .buyQuantity(10.0)
                .build();

        // Save
        trade = tradeRepository.save(trade);
        assertNotNull(trade.getId());
        assertEquals("Integration Trade Account", trade.getAccount());

        // Update
        trade.setAccount("Trade Account Update");
        trade = tradeRepository.save(trade);
        assertEquals("Trade Account Update", trade.getAccount());

        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attributeExists("trades"))
                .andExpect(content().string(containsString("Trade Account Update")))
                .andExpect(content().string(containsString("Type")));

        // Find
        List<Trade> listResult = tradeRepository.findAll();
        assertFalse(listResult.isEmpty());

        // Delete
        Integer id = trade.getId();
        tradeRepository.delete(trade);
        Optional<Trade> optionalTrade = tradeRepository.findById(id);
        assertFalse(optionalTrade.isPresent());
    }
}
