package com.bookstore.controller;

import com.bookstore.service.PageContentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactController {

    private final PageContentService pageContentService;

    public ContactController(PageContentService pageContentService) {
        this.pageContentService = pageContentService;
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("contactContent", pageContentService.getPageContent("contact"));
        return "contact";
    }

    @PostMapping("/contact/send")
    public String sendMessage(@RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message) {
        
        System.out.println("Message received from: " + name + " (" + email + ")");
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);

        return "redirect:/contact?success";
    }
}
