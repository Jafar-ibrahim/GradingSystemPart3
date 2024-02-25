package com.example.gradingsystempart3.Controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Log
@Controller
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
