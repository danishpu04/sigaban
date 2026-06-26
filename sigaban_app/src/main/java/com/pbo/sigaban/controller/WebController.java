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
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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
    public String dashboard(Model model) {
        Long totalWarga = wargaRepository.count();
        Long jumlahKeluarga = wargaRepository.sumFamily();
        Long wilayahTerdampak = wargaRepository.countDistinctEvacPoint();
        List<Warga> laporanWarga = wargaRepository.findTop3ByOrderByIdDesc();

        List<Warga> allWarga = wargaRepository.findAll();
        int total = allWarga.size();
        int jaktim = 0, jaksel = 0, jakut = 0, jakbar = 0, pusat = 0;
        
        if (total > 0) {
            for (Warga w : allWarga) {
                if (w.getAddress() == null) continue;
                String addr = w.getAddress().toLowerCase();
                if (addr.contains("timur") || addr.contains("jaktim")) jaktim++;
                else if (addr.contains("selatan") || addr.contains("jaksel")) jaksel++;
                else if (addr.contains("utara") || addr.contains("jakut")) jakut++;
                else if (addr.contains("barat") || addr.contains("jakbar")) jakbar++;
                else if (addr.contains("pusat")) pusat++;
            }
        }
        
        int pctJaktim = total > 0 ? (jaktim * 100 / total) : 0;
        int pctJaksel = total > 0 ? (jaksel * 100 / total) : 0;
        int pctJakut = total > 0 ? (jakut * 100 / total) : 0;
        int pctJakbar = total > 0 ? (jakbar * 100 / total) : 0;
        int pctPusat = total > 0 ? (pusat * 100 / total) : 0;

        model.addAttribute("totalWarga", totalWarga);
        model.addAttribute("jumlahKeluarga", jumlahKeluarga);
        model.addAttribute("wilayahTerdampak", wilayahTerdampak);
        model.addAttribute("laporanWarga", laporanWarga);
        
        model.addAttribute("pctJaktim", pctJaktim);
        model.addAttribute("pctJaksel", pctJaksel);
        model.addAttribute("pctJakut", pctJakut);
        model.addAttribute("pctJakbar", pctJakbar);
        model.addAttribute("pctPusat", pctPusat);
        
        return "dashboard";
    }

    @GetMapping("/export-warga")
    public void exportWarga(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"data_warga.csv\"");
        
        PrintWriter writer = response.getWriter();
        writer.println("ID,NIK,Nama,No. Telepon,Jumlah Keluarga,Alamat,Titik Evakuasi,Kondisi Kesehatan");
        
        List<Warga> wargaList = wargaRepository.findAll();
        for (Warga w : wargaList) {
            writer.printf("%d,\"%s\",\"%s\",\"%s\",%d,\"%s\",\"%s\",\"%s\"\n",
                    w.getId(),
                    w.getNik() != null ? w.getNik() : "",
                    w.getNama() != null ? w.getNama() : "",
                    w.getPhone() != null ? w.getPhone() : "",
                    w.getFamily() != null ? w.getFamily() : 0,
                    w.getAddress() != null ? w.getAddress().replace("\"", "\"\"") : "",
                    w.getEvacPoint() != null ? w.getEvacPoint() : "",
                    w.getHealth() != null ? w.getHealth() : ""
            );
        }
    }

    @GetMapping("/data-warga")
    public String dataWarga(Model model) {
        List<Warga> listWarga = wargaRepository.findAll();
        model.addAttribute("wargaList", listWarga);
        return "detail_warga";
    }

    @GetMapping("/form-warga")
    public String formWarga(Model model, @RequestParam(value = "source", required = false) String source) {
        model.addAttribute("warga", new Warga());
        model.addAttribute("source", source);
        return "form_warga";
    }

    @PostMapping("/form-warga")
    public String saveWarga(@ModelAttribute Warga warga, @RequestParam(value = "source", required = false) String source) {
        wargaRepository.save(warga);
        
        // Redirect to Data Warga if it's admin (from source param)
        if ("admin".equals(source)) {
            return "redirect:/data-warga?success=true";
        }
        
        // Setelah warga umum melapor, kembalikan ke landing page
        return "redirect:/?success=true";
    }

    @GetMapping("/edit-warga/{id}")
    public String editWarga(@PathVariable Long id, Model model, @RequestParam(value = "source", defaultValue = "admin") String source) {
        Warga warga = wargaRepository.findById(id).orElse(new Warga());
        model.addAttribute("warga", warga);
        model.addAttribute("source", source);
        // By default we assume editing is done by admins
        return "form_warga";
    }

    @GetMapping("/delete-warga/{id}")
    public String deleteWarga(@PathVariable Long id) {
        wargaRepository.deleteById(id);
        return "redirect:/data-warga?deleted=true";
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
