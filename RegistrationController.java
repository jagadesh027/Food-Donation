package com.fooddonation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fooddonation.model.User;
import com.fooddonation.repository.UserRepository;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user) {
        userRepository.save(user); // Save user without password encoding
        return "redirect:/login"; // Redirect to login page after registration
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // This will render login.html
    }
    @PostMapping("/login")
public String login(@RequestParam String username, @RequestParam String password) {
    // Custom authentication logic
    return "redirect:/role";
}



}
