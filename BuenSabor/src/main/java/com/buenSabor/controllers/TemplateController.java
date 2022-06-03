package com.buenSabor.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TemplateController {
    @Value("$TEST-74348839-fe10-495f-af97-8cf12e55ce3e")
    private String mercadoPagoSamplePublicKey;

    @GetMapping
    public String renderMainPage(Model model) {
        model.addAttribute("publicKey", mercadoPagoSamplePublicKey);
        return "index";
    }
}