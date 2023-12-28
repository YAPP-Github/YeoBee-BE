package com.example.yeobee.common.swagger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1/usage")
class SwaggerController {

    @GetMapping
    public String api() {
        return "redirect:/swagger-ui/index.html";
    }
}
