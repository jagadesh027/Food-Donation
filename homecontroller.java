package com.fooddonation.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class homecontroller {
    // Route for the home page (index.html)
    @GetMapping("/index")
    public String home() {
        return "index";  // Thymeleaf will resolve this as src/main/resources/templates/index.html
    }
    @GetMapping("/role")
    public String showRolePage() {
        return "role"; // This should render src/main/resources/templates/role.html
    }

    @GetMapping("/greetings")
    public String greetingsPage() {
        return "greetings"; // Thymeleaf template name for the receiver page (receiver.html)
    }
}


