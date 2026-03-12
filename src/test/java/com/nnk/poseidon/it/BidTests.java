package com.nnk.poseidon.it;

import com.nnk.poseidon.domain.Bid;
import com.nnk.poseidon.repositories.BidRepository;
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
public class BidTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BidRepository bidRepository;

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Should display the bid list page with updated data from database")
    public void bidIntegrationTest() throws Exception {
        Bid bid = Bid.builder()
                .account("Integration Account")
                .type("Type")
                .bidQuantity(10.0)
                .build();

        // Save
        bid = bidRepository.save(bid);
        assertNotNull(bid.getId());
        assertEquals(10d, bid.getBidQuantity(), 10d);

        // Update
        bid.setBidQuantity(20d);
        bid = bidRepository.save(bid);
        assertEquals(20d, bid.getBidQuantity(), 20d);

        mockMvc.perform(get("/bid/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bid/list"))
                .andExpect(model().attributeExists("bids"))
                .andExpect(content().string(containsString("Integration Account")))
                .andExpect(content().string(containsString("20")));

        // Find
        List<Bid> listResult = bidRepository.findAll();
        assertFalse(listResult.isEmpty());

        // Delete
        Integer id = bid.getId();
        bidRepository.delete(bid);
        Optional<Bid> optionalBid = bidRepository.findById(id);
        assertFalse(optionalBid.isPresent());
    }
}
