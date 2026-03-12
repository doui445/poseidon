package com.nnk.poseidon.services;

import com.nnk.poseidon.domain.Bid;
import com.nnk.poseidon.domain.dto.BidRequest;
import com.nnk.poseidon.repositories.BidRepository;
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
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;

    @Override
    public List<Bid> getBids() {
        return bidRepository.findAll();
    }

    @Override
    public Optional<Bid> getBidById(Integer id) {
        return bidRepository.findById(id);
    }

    @Override
    public Bid saveBid(BidRequest request) {
        if (request.id() != null && bidRepository.findById(request.id()).isPresent()) {
            throw new IllegalArgumentException("Bid already exist");
        }
        Bid bid = new Bid();
        bid.setAccount(request.account());
        bid.setType(request.type());
        bid.setBidQuantity(request.bidQuantity());
        bid.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
        return bidRepository.save(bid);
    }

    @Override
    public Bid updateBid(Integer id, BidRequest request) {
        Bid bid = bidRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Bid Id:" + id));
        bid.setAccount(request.account());
        bid.setType(request.type());
        bid.setBidQuantity(request.bidQuantity());
        return bidRepository.save(bid);
    }

    @Override
    public void deleteBidById(Integer id) {
        Bid bid = bidRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Bid Id:" + id));
        bidRepository.delete(bid);
    }

    @Override
    public BidRequest getByIdAsRequest(Integer id) {
        Bid bid = bidRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Bid Id:" + id));
        return new BidRequest(
                bid.getId(),
                bid.getAccount(),
                bid.getType(),
                bid.getBidQuantity()
        );
    }
}
