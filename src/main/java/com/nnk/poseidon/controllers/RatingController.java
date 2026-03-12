package com.nnk.poseidon.controllers;

import com.nnk.poseidon.domain.dto.RatingRequest;
import com.nnk.poseidon.services.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rating")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping("/list")
    public String ratingsPage(Model model) {
        model.addAttribute("ratings", ratingService.getRatings());
        return "rating/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("rating", new RatingRequest(null, "", "", "", null));
        return "rating/add";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        RatingRequest request = ratingService.getByIdAsRequest(id);
        model.addAttribute("rating", request);
        model.addAttribute("id", id);
        return "rating/update";
    }

    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("rating") RatingRequest request,
                           BindingResult result) {
        if (!result.hasErrors()) {
            ratingService.saveRating(request);
            return "redirect:/rating/list";
        }
        return "rating/add";
    }

    @PostMapping("/update/{id}")
    public String updateRating(@PathVariable("id") Integer id,
                               @Valid @ModelAttribute("rating") RatingRequest request,
                               BindingResult result) {
        if (!result.hasErrors()) {
            ratingService.updateRating(id, request);
            return "redirect:/rating/list";
        }
        return "rating/update";
    }

    @GetMapping("/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id) {
        ratingService.deleteRatingById(id);
        return "redirect:/rating/list";
    }
}
