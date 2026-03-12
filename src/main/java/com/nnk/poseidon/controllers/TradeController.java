package com.nnk.poseidon.controllers;

import com.nnk.poseidon.domain.dto.TradeRequest;
import com.nnk.poseidon.services.TradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/trade")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @GetMapping("/list")
    public String tradesPage(Model model) {
        model.addAttribute("trades", tradeService.getTrades());
        return "trade/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("trade", new TradeRequest(null, "", "", null));
        return "trade/add";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        TradeRequest request = tradeService.getByIdAsRequest(id);
        model.addAttribute("trade", request);
        model.addAttribute("id", id);
        return "trade/update";
    }

    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("trade") TradeRequest request,
                           BindingResult result) {
        if (!result.hasErrors()) {
            tradeService.saveTrade(request);
            return "redirect:/trade/list";
        }
        return "trade/add";
    }

    @PostMapping("/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id,
                              @Valid @ModelAttribute("trade") TradeRequest request,
                              BindingResult result) {
        if (!result.hasErrors()) {
            tradeService.updateTrade(id, request);
            return "redirect:/trade/list";
        }
        return "trade/update";
    }

    @GetMapping("/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id) {
        tradeService.deleteTradeById(id);
        return "redirect:/trade/list";
    }
}
