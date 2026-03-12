package com.nnk.poseidon.controllers;

import com.nnk.poseidon.domain.dto.CurvePointRequest;
import com.nnk.poseidon.services.CurvePointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/curvepoint")
@RequiredArgsConstructor
public class CurvePointController {

    private final CurvePointService curvePointService;

    @GetMapping("/list")
    public String home(Model model) {
        model.addAttribute("curvePoints", curvePointService.getCurvePoints());
        return "curvepoint/list";
    }

    @GetMapping("/add")
    public String addCurvePointForm(Model model) {
        model.addAttribute("curvePoint", new CurvePointRequest(null,null, null, null));
        return "curvepoint/add";
    }

    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("curvePoint") CurvePointRequest request,
                           BindingResult result, Model model) {
        if (!result.hasErrors()) {
            curvePointService.saveCurvePoint(request);
            return "redirect:/curvepoint/list";
        }
        return "curvepoint/add";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        CurvePointRequest request = curvePointService.getByIdAsRequest(id);
        model.addAttribute("curvePoint", request);
        model.addAttribute("id", id);
        return "curvepoint/update";
    }

    @PostMapping("/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id,
                                   @Valid @ModelAttribute("curvePoint") CurvePointRequest request,
                                   BindingResult result, Model model) {
        if (!result.hasErrors()) {
            curvePointService.updateCurvePoint(id, request);
            return "redirect:/curvepoint/list";
        }
        return "curvepoint/update";
    }

    @GetMapping("/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        curvePointService.deleteCurvePointById(id);
        return "redirect:/curvepoint/list";
    }
}
