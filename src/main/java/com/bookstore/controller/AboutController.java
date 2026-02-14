package com.bookstore.controller;

import com.bookstore.service.PageContentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    private final PageContentService pageContentService;

    public AboutController(PageContentService pageContentService) {
        this.pageContentService = pageContentService;
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("aboutContent", pageContentService.getPageContent("about"));
        return "about";
    }
}