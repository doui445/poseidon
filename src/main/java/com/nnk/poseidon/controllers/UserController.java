package com.nnk.poseidon.controllers;

import com.nnk.poseidon.domain.dto.UserRegistrationDTO;
import com.nnk.poseidon.domain.dto.UserUpdateDTO;
import com.nnk.poseidon.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/list")
    public String usersPage(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "user/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new UserRegistrationDTO("", "", "", "", ""));
        return "user/add";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        UserUpdateDTO updates = userService.getUpdateDTO(id);
        model.addAttribute("user", updates);
        model.addAttribute("id", id);
        return "user/update";
    }

    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("user") UserRegistrationDTO registrations,
                           BindingResult result) {
        if (!registrations.passwordsMatch()) {
            result.rejectValue("confirmPassword", "error.user", "Passwords do not match");
        }
        if (!result.hasErrors()) {
            userService.saveUser(registrations);
            return "redirect:/user/list";
        }
        return "user/add";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Integer id,
                             @Valid @ModelAttribute("user") UserUpdateDTO updates,
                             BindingResult result) {
        if (!id.equals(updates.id())) {
            throw new IllegalArgumentException("ID mismatch");
        }
        if (!result.hasErrors()) {
            userService.updateUser(updates);
            return "redirect:/user/list";
        }
        return "user/update";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUserById(id);
        return "redirect:/user/list";
    }
}
