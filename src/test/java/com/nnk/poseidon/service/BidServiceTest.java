package com.nnk.poseidon.service;

import com.nnk.poseidon.domain.Bid;
import com.nnk.poseidon.domain.dto.BidRequest;
import com.nnk.poseidon.repositories.BidRepository;
import com.nnk.poseidon.services.BidServiceImpl;
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

public class BidServiceTest {

    @Mock
    private BidRepository bidRepository;

    @InjectMocks
    private BidServiceImpl bidService;

    private AutoCloseable mocks;

    private Bid bid;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        bid = Bid.builder()
                .id(1)
                .account("account")
                .type("type")
                .bidQuantity(10.0)
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    @DisplayName("getBidById should return a bid when bid exists")
    void testGetBidByIdFound() {
        given(bidRepository.findById(1)).willReturn(Optional.of(bid));

        Optional<Bid> result = bidService.getBidById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getAccount()).isEqualTo("account");
        verify(bidRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("getBidById should return empty when bid does not exist")
    void testGetBidByIdNotFound() {
        given(bidRepository.findById(2)).willReturn(Optional.empty());

        Optional<Bid> result = bidService.getBidById(2);

        assertThat(result).isEmpty();
        verify(bidRepository, times(1)).findById(2);
    }

    @Test
    @DisplayName("saveBid should save and return the bid")
    void testSaveBid() {
        given(bidRepository.save(any())).willReturn(bid);

        BidRequest request = new BidRequest(bid.getId(), bid.getAccount(), bid.getType(), bid.getBidQuantity());
        Bid savedBid = bidService.saveBid(request);

        assertThat(savedBid).isNotNull();
        assertThat(savedBid.getAccount()).isEqualTo("account");
        verify(bidRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("updateBid should update and return the bid")
    void testUpdateBid() {
        Bid newBid = bid;
        newBid.setAccount("newAccount");

        given(bidRepository.save(bid)).willReturn(newBid);
        given(bidRepository.findById(1)).willReturn(Optional.of(bid));

        BidRequest request = new BidRequest(bid.getId(), "newAccount", bid.getType(), bid.getBidQuantity());
        Bid savedBid = bidService.updateBid(1, request);

        assertThat(savedBid).isNotNull();
        assertThat(savedBid.getAccount()).isEqualTo("newAccount");
        verify(bidRepository, times(1)).save(bid);
    }

    @Test
    @DisplayName("deleteBidById should delete bid by id")
    void testDeleteBidByIdById() {
        given(bidRepository.findById(1)).willReturn(Optional.of(bid));

        bidService.deleteBidById(1);

        verify(bidRepository, times(1)).delete(bid);
    }
}
