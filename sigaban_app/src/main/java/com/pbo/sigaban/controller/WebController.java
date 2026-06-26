package com.pbo.sigaban.controller;

import com.pbo.sigaban.model.Warga;
import com.pbo.sigaban.repository.WargaRepository;
import com.pbo.sigaban.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class WebController {

    @Autowired
    private AuthService authService;

    @Autowired
    private WargaRepository wargaRepository;

    @GetMapping("/")
    public String landingPage(Model model) {
        List<Warga> listWarga = wargaRepository.findAll();
        
        // Calculate total affected (registered persons + family members)
        int totalAffected = listWarga.stream()
                .mapToInt(w -> (w.getFamily() != null ? w.getFamily() : 0) + 1)
                .sum();
                
        // Calculate active shelters (unique non-empty evac points)
        long activeShelters = listWarga.stream()
                .map(Warga::getEvacPoint)
                .filter(ep -> ep != null && !ep.trim().isEmpty())
                .distinct()
                .count();
                
        // Calculate dynamic logistics percentage
        long logisticPercentage = Math.min(98, Math.max(70, 70 + activeShelters * 5));
        
        model.addAttribute("totalAffected", totalAffected);
        model.addAttribute("activeShelters", activeShelters);
        model.addAttribute("logisticPercentage", logisticPercentage);
        
        return "landing_page";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String username, 
                               @RequestParam("password") String password,
                               Model model) {
        if (authService.authenticate(username, password)) {
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Username atau password salah!");
            return "login";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/data-warga")
    public String dataWarga(Model model) {
        List<Warga> listWarga = wargaRepository.findAll();
        model.addAttribute("wargaList", listWarga);
        return "detail_warga";
    }

    @GetMapping("/form-warga")
    public String formWarga(Model model) {
        model.addAttribute("warga", new Warga());
        return "form_warga";
    }

    @PostMapping("/form-warga")
    public String saveWarga(@ModelAttribute Warga warga) {
        wargaRepository.save(warga);
        return "redirect:/data-warga";
    }

    @GetMapping("/bantuan")
    public String bantuan() {
        return "bantuan";
    }

    @GetMapping("/posko")
    public String posko() {
        return "posko";
    }

    @GetMapping("/ai-insight")
    public String aiInsight() {
        return "ai_insight";
    }

    @GetMapping("/laporan")
    public String laporan() {
        return "laporan";
    }

    @GetMapping("/pengaturan")
    public String pengaturan() {
        return "pengaturan";
    }
}
