package com.nnk.poseidon.services;

import com.nnk.poseidon.domain.Bid;
import com.nnk.poseidon.domain.dto.BidRequest;

import java.util.List;
import java.util.Optional;

public interface BidService {

    List<Bid> getBids();

    Optional<Bid> getBidById(Integer id);

    Bid saveBid(BidRequest request);

    Bid updateBid(Integer id, BidRequest request);

    void deleteBidById(Integer id);

    BidRequest getByIdAsRequest(Integer id);
}
