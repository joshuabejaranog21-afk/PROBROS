package com.probros.admin.controller;

import com.probros.admin.service.ImageSlotService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ImageAdminController {

    private final ImageSlotService service;

    public ImageAdminController(ImageSlotService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("slotsByPage", service.getSlotsByPage());
        return "index";
    }

    @PostMapping("/upload/{slotId}")
    public String upload(@PathVariable String slotId,
                         @RequestParam("file") MultipartFile file,
                         RedirectAttributes attrs) {
        if (file.isEmpty()) {
            attrs.addFlashAttribute("error", "Selecciona un archivo primero.");
            return "redirect:/";
        }
        try {
            service.uploadImage(slotId, file);
            attrs.addFlashAttribute("success", "Imagen subida correctamente.");
        } catch (Exception e) {
            attrs.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/remove/{slotId}")
    public String remove(@PathVariable String slotId, RedirectAttributes attrs) {
        try {
            service.removeImage(slotId);
            attrs.addFlashAttribute("success", "Imagen eliminada.");
        } catch (Exception e) {
            attrs.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/";
    }
}
