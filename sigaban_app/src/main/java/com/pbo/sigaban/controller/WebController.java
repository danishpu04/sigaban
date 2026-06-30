package com.pbo.sigaban.controller;

import com.pbo.sigaban.model.Warga;
import com.pbo.sigaban.model.Inventaris;
import com.pbo.sigaban.model.LogBantuan;
import com.pbo.sigaban.model.Posko;
import com.pbo.sigaban.repository.WargaRepository;
import com.pbo.sigaban.repository.InventarisRepository;
import com.pbo.sigaban.repository.LogBantuanRepository;
import com.pbo.sigaban.repository.PoskoRepository;
import com.pbo.sigaban.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.servlet.http.HttpSession;
import com.pbo.sigaban.model.User;
import com.pbo.sigaban.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class WebController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WargaRepository wargaRepository;

    @Autowired
    private InventarisRepository inventarisRepository;

    @Autowired
    private LogBantuanRepository logBantuanRepository;

    @Autowired
    private PoskoRepository poskoRepository;

    @GetMapping("/")
    public String landingPage(Model model) {
        List<Warga> listWarga = wargaRepository.findAll();
        
        // Calculate total affected (registered persons + family members)
        int totalAffected = listWarga.stream()
                .mapToInt(w -> (w.getFamily() != null ? w.getFamily() : 0) + 1)
                .sum();
                
        // Calculate active shelters
        long activeShelters = poskoRepository.count();
                
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
                               HttpSession session,
                               Model model) {
        User user = authService.authenticate(username, password);
        if (user != null) {
            session.setAttribute("loggedInUserId", user.getId());
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Username atau password salah!");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Long jumlahKeluarga = wargaRepository.count();
        Long sumFamily = wargaRepository.sumFamily();
        Long totalWarga = jumlahKeluarga + (sumFamily != null ? sumFamily : 0);
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
        
        // Bantuan Metrics
        List<Inventaris> inventarisList = inventarisRepository.findAll();
        int totalItemMasuk = inventarisList.stream().mapToInt(Inventaris::getJumlah).sum();
        long logKeluar = logBantuanRepository.findByTipeOrderByWaktuDesc("KELUAR").size();
        
        model.addAttribute("totalItemMasuk", totalItemMasuk);
        model.addAttribute("logKeluar", logKeluar);
        
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
        List<Posko> poskoList = poskoRepository.findAll();
        for (Posko p : poskoList) {
            long count = wargaRepository.sumOrangByEvacPoint(p.getNama());
            p.setKapasitasSaatIni((int) count);
            double ratio = p.getKapasitasMaksimal() > 0 ? (double) count / p.getKapasitasMaksimal() : 1.0;
            if (ratio >= 1.0) {
                p.setStatusKepenuhan("Penuh");
            } else if (ratio >= 0.8) {
                p.setStatusKepenuhan("Hampir Penuh");
            } else {
                p.setStatusKepenuhan("Tersedia");
            }
        }
        model.addAttribute("poskoList", poskoList);
        return "form_warga";
    }

    @PostMapping("/form-warga")
    public String saveWarga(@ModelAttribute Warga warga, @RequestParam(value = "source", required = false) String source, Model model) {
        // Validasi Kapasitas Posko
        java.util.Optional<Posko> optionalPosko = poskoRepository.findByNama(warga.getEvacPoint());
        if (optionalPosko.isPresent()) {
            Posko posko = optionalPosko.get();
            long count = wargaRepository.sumOrangByEvacPoint(posko.getNama());
            long additional = (warga.getFamily() != null ? warga.getFamily() : 0) + 1;
            
            if (warga.getId() != null) {
                Warga oldWarga = wargaRepository.findById(warga.getId()).orElse(null);
                if (oldWarga != null && oldWarga.getEvacPoint().equals(posko.getNama())) {
                    long oldCount = (oldWarga.getFamily() != null ? oldWarga.getFamily() : 0) + 1;
                    count -= oldCount;
                }
            }
            
            if (count + additional > posko.getKapasitasMaksimal()) {
                model.addAttribute("error", "posko_full");
                model.addAttribute("warga", warga);
                model.addAttribute("source", source);
                
                // Reload poskoList
                List<Posko> poskoList = poskoRepository.findAll();
                for (Posko p : poskoList) {
                    long pCount = wargaRepository.sumOrangByEvacPoint(p.getNama());
                    p.setKapasitasSaatIni((int) pCount);
                    double ratio = p.getKapasitasMaksimal() > 0 ? (double) pCount / p.getKapasitasMaksimal() : 1.0;
                    if (ratio >= 1.0) {
                        p.setStatusKepenuhan("Penuh");
                    } else if (ratio >= 0.8) {
                        p.setStatusKepenuhan("Hampir Penuh");
                    } else {
                        p.setStatusKepenuhan("Tersedia");
                    }
                }
                model.addAttribute("poskoList", poskoList);
                return "form_warga";
            }
        }

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
        List<Posko> poskoList = poskoRepository.findAll();
        for (Posko p : poskoList) {
            long count = wargaRepository.sumOrangByEvacPoint(p.getNama());
            p.setKapasitasSaatIni((int) count);
            double ratio = p.getKapasitasMaksimal() > 0 ? (double) count / p.getKapasitasMaksimal() : 1.0;
            if (ratio >= 1.0) {
                p.setStatusKepenuhan("Penuh");
            } else if (ratio >= 0.8) {
                p.setStatusKepenuhan("Hampir Penuh");
            } else {
                p.setStatusKepenuhan("Tersedia");
            }
        }
        model.addAttribute("poskoList", poskoList);
        // By default we assume editing is done by admins
        return "form_warga";
    }

    @GetMapping("/delete-warga/{id}")
    public String deleteWarga(@PathVariable Long id) {
        wargaRepository.deleteById(id);
        return "redirect:/data-warga?deleted=true";
    }

    @GetMapping("/bantuan")
    public String bantuan(Model model) {
        // Initialize Inventaris if empty
        if (inventarisRepository.count() == 0) {
            inventarisRepository.save(new Inventaris("Beras", 0, "Kg", "Low Stock"));
            inventarisRepository.save(new Inventaris("Air Bersih", 0, "Liter", "Low Stock"));
            inventarisRepository.save(new Inventaris("Obat-obatan", 0, "Paket", "Low Stock"));
            inventarisRepository.save(new Inventaris("Selimut", 0, "Helai", "Low Stock"));
        }

        List<Inventaris> inventarisList = inventarisRepository.findAll();
        List<LogBantuan> logMasuk = logBantuanRepository.findByTipeOrderByWaktuDesc("MASUK");
        List<LogBantuan> logKeluar = logBantuanRepository.findByTipeOrderByWaktuDesc("KELUAR");

        // Put inventaris into map for easier access in view
        for (Inventaris inv : inventarisList) {
            model.addAttribute("inv_" + inv.getNamaItem().replaceAll("\\s+", "").replace("-", ""), inv);
        }

        model.addAttribute("logMasuk", logMasuk);
        model.addAttribute("logKeluar", logKeluar);

        return "bantuan";
    }

    @GetMapping("/form-bantuan")
    public String formBantuan(Model model) {
        model.addAttribute("logBantuan", new LogBantuan());
        
        // Pass current inventaris so user can update them
        List<Inventaris> inventarisList = inventarisRepository.findAll();
        for (Inventaris inv : inventarisList) {
            model.addAttribute("inv_" + inv.getNamaItem().replaceAll("\\s+", "").replace("-", ""), inv);
        }

        // Fetch distinct poskos from repository
        List<String> poskoList = poskoRepository.findAll().stream()
                .map(Posko::getNama)
                .sorted()
                .collect(Collectors.toList());
        model.addAttribute("poskoList", poskoList);

        return "form_bantuan";
    }

    @PostMapping("/form-bantuan")
    public String saveBantuan(@ModelAttribute LogBantuan logBantuan,
                              @RequestParam(value = "berasQty", required = false) Integer berasQty,
                              @RequestParam(value = "airQty", required = false) Integer airQty,
                              @RequestParam(value = "obatQty", required = false) Integer obatQty,
                              @RequestParam(value = "selimutQty", required = false) Integer selimutQty) {
        
        // Auto-generate description
        StringBuilder desc = new StringBuilder();
        if (berasQty != null && berasQty > 0) desc.append(berasQty).append(" Kg Beras, ");
        if (airQty != null && airQty > 0) desc.append(airQty).append(" Liter Air, ");
        if (obatQty != null && obatQty > 0) desc.append(obatQty).append(" Paket Obat, ");
        if (selimutQty != null && selimutQty > 0) desc.append(selimutQty).append(" Helai Selimut, ");
        
        String finalDesc = desc.toString();
        if (finalDesc.endsWith(", ")) {
            finalDesc = finalDesc.substring(0, finalDesc.length() - 2);
        }
        if (finalDesc.isEmpty()) {
            finalDesc = "Tidak ada spesifikasi barang";
        }
        logBantuan.setDeskripsiItem(finalDesc);

        logBantuanRepository.save(logBantuan);

        // Update Inventaris automatically based on transaction type
        updateInventaris("Beras", berasQty, logBantuan.getTipe());
        updateInventaris("Air Bersih", airQty, logBantuan.getTipe());
        updateInventaris("Obat-obatan", obatQty, logBantuan.getTipe());
        updateInventaris("Selimut", selimutQty, logBantuan.getTipe());

        return "redirect:/bantuan?success=true";
    }

    private void updateInventaris(String namaItem, Integer qty, String tipe) {
        if (qty != null && qty > 0) {
            inventarisRepository.findByNamaItem(namaItem).ifPresent(inv -> {
                int currentQty = inv.getJumlah();
                if ("MASUK".equals(tipe)) {
                    inv.setJumlah(currentQty + qty);
                } else if ("KELUAR".equals(tipe)) {
                    inv.setJumlah(Math.max(0, currentQty - qty));
                }
                inv.setStatusKondisi(inv.getJumlah() > 100 ? "Sufficient" : "Low Stock");
                inventarisRepository.save(inv);
            });
        }
    }

    @GetMapping("/posko")
    public String posko(Model model) {
        List<Posko> poskoList = poskoRepository.findAll();
        for (Posko p : poskoList) {
            long count = wargaRepository.sumOrangByEvacPoint(p.getNama());
            p.setKapasitasSaatIni((int) count);
            double ratio = p.getKapasitasMaksimal() > 0 ? (double) count / p.getKapasitasMaksimal() : 1.0;
            if (ratio >= 1.0) {
                p.setStatusKepenuhan("Penuh");
                p.setPersentase(100);
            } else if (ratio >= 0.8) {
                p.setStatusKepenuhan("Hampir Penuh");
                p.setPersentase((int) (ratio * 100));
            } else {
                p.setStatusKepenuhan("Tersedia");
                p.setPersentase((int) (ratio * 100));
            }
        }
        model.addAttribute("poskoList", poskoList);
        return "posko";
    }

    @GetMapping("/form-posko")
    public String formPosko(Model model) {
        model.addAttribute("posko", new Posko());
        return "form_posko";
    }

    @PostMapping("/form-posko")
    public String savePosko(@ModelAttribute Posko posko) {
        poskoRepository.save(posko);
        return "redirect:/posko?success=true";
    }

    @GetMapping("/edit-posko/{id}")
    public String editPosko(@PathVariable Long id, Model model) {
        Posko posko = poskoRepository.findById(id).orElse(new Posko());
        model.addAttribute("posko", posko);
        return "form_posko";
    }

    @GetMapping("/delete-posko/{id}")
    public String deletePosko(@PathVariable Long id) {
        java.util.Optional<Posko> optionalPosko = poskoRepository.findById(id);
        if (optionalPosko.isPresent()) {
            Posko posko = optionalPosko.get();
            long count = wargaRepository.sumOrangByEvacPoint(posko.getNama());
            if (count > 0) {
                return "redirect:/posko?error=has_warga";
            }
            poskoRepository.deleteById(id);
        }
        return "redirect:/posko?success=deleted";
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
    public String pengaturan(HttpSession session, Model model) {
        Long loggedInUserId = (Long) session.getAttribute("loggedInUserId");
        if (loggedInUserId == null) {
            return "redirect:/login";
        }
        User currentUser = userRepository.findById(loggedInUserId).orElse(null);
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", userRepository.findAll());
        return "pengaturan";
    }

    @PostMapping("/update-account")
    public String updateAccount(@ModelAttribute User updatedUser, HttpSession session) {
        Long loggedInUserId = (Long) session.getAttribute("loggedInUserId");
        if (loggedInUserId != null) {
            User existing = userRepository.findById(loggedInUserId).orElse(null);
            if (existing != null) {
                existing.setFullName(updatedUser.getFullName());
                existing.setEmail(updatedUser.getEmail());
                existing.setPhone(updatedUser.getPhone());
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    existing.setPassword(updatedUser.getPassword());
                }
                userRepository.save(existing);
            }
        }
        return "redirect:/pengaturan?success=account";
    }

    @PostMapping("/add-user")
    public String addUser(@ModelAttribute User newUser) {
        userRepository.save(newUser);
        return "redirect:/pengaturan?success=adduser";
    }

    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session) {
        Long loggedInUserId = (Long) session.getAttribute("loggedInUserId");
        if (loggedInUserId != null && !loggedInUserId.equals(id)) {
            userRepository.deleteById(id);
        }
        return "redirect:/pengaturan?success=deleteuser";
    }
}
