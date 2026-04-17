package com.portfolio.controller;

import com.portfolio.model.Portfolio;
import com.portfolio.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    // Home - Display Portfolio
    @GetMapping("/")
    public String home(Model model) {
        Portfolio portfolio = portfolioService.getPortfolio();
        model.addAttribute("portfolio", portfolio);
        return portfolio == null ? "setup" : "index";
    }

    // Show create/edit form
    @GetMapping("/edit")
    public String editForm(Model model) {
        Portfolio portfolio = portfolioService.getPortfolio();
        if (portfolio == null) portfolio = new Portfolio();
        model.addAttribute("portfolio", portfolio);
        return "form";
    }

    // Save portfolio
    @PostMapping("/save")
    public String savePortfolio(
            @ModelAttribute Portfolio portfolio,
            @RequestParam(value = "photoFile", required = false) MultipartFile photo,
            @RequestParam(value = "resumeFile", required = false) MultipartFile resume,
            Model model) {

        try {
            portfolioService.savePortfolio(portfolio, photo, resume);
            return "redirect:/";
        } catch (IOException e) {
            model.addAttribute("error", "File upload failed: " + e.getMessage());
            model.addAttribute("portfolio", portfolio);
            return "form";
        }
    }

    // API endpoint - Get portfolio as JSON
    @GetMapping("/api/portfolio")
    @ResponseBody
    public Portfolio getPortfolioJson() {
        return portfolioService.getPortfolio();
    }
}
