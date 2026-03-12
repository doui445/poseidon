package com.nnk.poseidon.controllers;

import com.nnk.poseidon.domain.dto.CurvePointRequest;
import com.nnk.poseidon.services.CurvePointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/curvepoint")
@RequiredArgsConstructor
public class CurvePointController {

    private final CurvePointService curvePointService;

    @GetMapping("/list")
    public String curvePointsPage(Model model) {
        model.addAttribute("curvePoints", curvePointService.getCurvePoints());
        return "curvepoint/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("curvePoint", new CurvePointRequest(null, null, null, null));
        return "curvepoint/add";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        CurvePointRequest request = curvePointService.getByIdAsRequest(id);
        model.addAttribute("curvePoint", request);
        model.addAttribute("id", id);
        return "curvepoint/update";
    }

    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("curvePoint") CurvePointRequest request,
                           BindingResult result) {
        if (!result.hasErrors()) {
            curvePointService.saveCurvePoint(request);
            return "redirect:/curvepoint/list";
        }
        return "curvepoint/add";
    }

    @PostMapping("/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id,
                                   @Valid @ModelAttribute("curvePoint") CurvePointRequest request,
                                   BindingResult result) {
        if (!result.hasErrors()) {
            curvePointService.updateCurvePoint(id, request);
            return "redirect:/curvepoint/list";
        }
        return "curvepoint/update";
    }

    @GetMapping("/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id) {
        curvePointService.deleteCurvePointById(id);
        return "redirect:/curvepoint/list";
    }
}
