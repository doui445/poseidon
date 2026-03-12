package com.nnk.poseidon.controllers;

import com.nnk.poseidon.domain.dto.BidRequest;
import com.nnk.poseidon.services.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/bid")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @GetMapping("/list")
    public String home(Model model) {
        model.addAttribute("bids", bidService.getBids());
        return "bid/list";
    }

    @GetMapping("/add")
    public String addBidForm(Model model) {
        model.addAttribute("bid", new BidRequest(null,"", "", null));
        return "bid/add";
    }

    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("bid") BidRequest request,
                           BindingResult result, Model model) {
        if (!result.hasErrors()) {
            bidService.saveBid(request);
            return "redirect:/bid/list";
        }
        return "bid/add";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        BidRequest request = bidService.getByIdAsRequest(id);
        model.addAttribute("bid", request);
        model.addAttribute("id", id);
        return "bid/update";
    }

    @PostMapping("/update/{id}")
    public String updateBid(@PathVariable("id") Integer id,
                                @Valid @ModelAttribute("bid") BidRequest request,
                                BindingResult result, Model model) {
        if (!result.hasErrors()) {
            bidService.updateBid(id, request);
            return "redirect:/bid/list";
        }
        return "bid/update";
    }

    @GetMapping("/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        bidService.deleteBidById(id);
        return "redirect:/bid/list";
    }
}
