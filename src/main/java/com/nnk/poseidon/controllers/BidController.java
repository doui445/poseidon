package com.nnk.poseidon.controllers;

import com.nnk.poseidon.domain.dto.BidRequest;
import com.nnk.poseidon.services.BidService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bid")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @GetMapping("/list")
    public String bidsPage(Model model) {
        model.addAttribute("bids", bidService.getBids());
        return "bid/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("bid", new BidRequest(null, "", "", null));
        return "bid/add";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        BidRequest request = bidService.getByIdAsRequest(id);
        model.addAttribute("bid", request);
        model.addAttribute("id", id);
        return "bid/update";
    }

    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("bid") BidRequest request,
                           BindingResult result) {
        if (!result.hasErrors()) {
            bidService.saveBid(request);
            return "redirect:/bid/list";
        }
        return "bid/add";
    }

    @PostMapping("/update/{id}")
    public String updateBid(@PathVariable("id") Integer id,
                            @Valid @ModelAttribute("bid") BidRequest request,
                            BindingResult result) {
        if (!result.hasErrors()) {
            bidService.updateBid(id, request);
            return "redirect:/bid/list";
        }
        return "bid/update";
    }

    @GetMapping("/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id) {
        bidService.deleteBidById(id);
        return "redirect:/bid/list";
    }
}
