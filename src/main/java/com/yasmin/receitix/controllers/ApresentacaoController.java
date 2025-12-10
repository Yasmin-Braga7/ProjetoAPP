package com.yasmin.receitix.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin("*")
public class ApresentacaoController {

    @GetMapping("/")
    public String home() {
        return "forward:/index.html";
    }
}
