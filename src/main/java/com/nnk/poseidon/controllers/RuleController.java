package com.nnk.poseidon.controllers;

import com.nnk.poseidon.domain.dto.RuleRequest;
import com.nnk.poseidon.services.RuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rule")
@RequiredArgsConstructor
public class RuleController {

    private final RuleService ruleService;

    @GetMapping("/list")
    public String home(Model model) {
        model.addAttribute("rules", ruleService.getRules());
        return "rule/list";
    }

    @GetMapping("/add")
    public String addRuleNameForm(Model model) {
        model.addAttribute("rule", new RuleRequest(null, "", "", "", "", "", ""));
        return "rule/add";
    }

    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("rule") RuleRequest request,
                           BindingResult result, Model model) {
        if (!result.hasErrors()) {
            ruleService.saveRule(request);
            return "redirect:/rule/list";
        }
        return "rule/add";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        RuleRequest request = ruleService.getByIdAsRequest(id);
        model.addAttribute("rule", request);
        model.addAttribute("id", id);
        return "rule/update";
    }

    @PostMapping("/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id,
                                 @Valid @ModelAttribute("rule") RuleRequest request,
                                 BindingResult result, Model model) {
        if (!result.hasErrors()) {
            ruleService.updateRule(id, request);
            return "redirect:/rule/list";
        }
        return "rule/update";
    }

    @GetMapping("/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        ruleService.deleteRuleById(id);
        return "redirect:/rule/list";
    }
}
