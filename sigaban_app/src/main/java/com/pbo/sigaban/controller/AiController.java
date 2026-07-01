package com.pbo.sigaban.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.beans.factory.annotation.Autowired;
import com.pbo.sigaban.repository.WargaRepository;
import com.pbo.sigaban.repository.PoskoRepository;
import com.pbo.sigaban.model.Warga;
import com.pbo.sigaban.model.Posko;


@RestController
@RequestMapping("/api/chat")
public class AiController {
    @Autowired
    private WargaRepository wargaRepository;
    
    @Autowired
    private PoskoRepository poskoRepository;


    private final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";
    
    @Value("${groq.api.key}")
    private String API_KEY;

    @PostMapping
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("reply", "Pesan tidak boleh kosong."));
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "openai/gpt-oss-20b");
        
        List<Map<String, String>> messages = new ArrayList<>();
        

        StringBuilder dbContext = new StringBuilder();
        dbContext.append("\n\nINFORMASI DATABASE SAAT INI (Konteks untuk menjawab pertanyaan):\n");
        dbContext.append("- Data Warga:\n");
        List<Warga> wargasList = wargaRepository.findAll();
        for (Warga w : wargasList) {
            dbContext.append(String.format("  * ID: %d, NIK: %s, Nama: %s, Jumlah Keluarga: %d, Alamat: %s, Posko Pengungsian: %s, Kondisi Kesehatan: %s\n", 
                w.getId(), w.getNik(), w.getNama(), w.getFamily(), w.getAddress(), w.getEvacPoint(), w.getHealth()));
        }
        dbContext.append("- Data Posko:\\n");
        List<Posko> poskoList = poskoRepository.findAll();
        for (Posko p : poskoList) {
            dbContext.append(String.format("  * Nama Posko: %s, Alamat: %s, Kapasitas Maksimal: %d, Status Logistik: %s, Telepon: %s\\n", 
                p.getNama(), p.getAlamat(), p.getKapasitasMaksimal(), p.getStatusLogistik(), p.getNoTelepon()));
        }

        // System prompt to set context
        Map<String, String> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", "Anda adalah asisten AI ramah bernama SIGABAN Assistant. Tugas Anda adalah membantu pengelola dan warga dalam mitigasi bencana banjir, logistik, pengungsian, dan memberikan saran praktis terkait cuaca. Jawablah dengan bahasa Indonesia yang singkat, jelas, dan empatik. Gunakan informasi database berikut untuk menjawab jika relevan:" + dbContext.toString());
        messages.add(systemMsg);
        
        // User message
        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);
        messages.add(userMsg);

        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 512);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(GROQ_API_URL, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> firstChoice = choices.get(0);
                    Map<String, String> message = (Map<String, String>) firstChoice.get("message");
                    String replyContent = message.get("content");
                    return ResponseEntity.ok(Collections.singletonMap("reply", replyContent));
                }
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("reply", "Maaf, terjadi kesalahan saat memproses data. Coba lagi nanti."));
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            String errorMsg = "API Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString();
            System.err.println(errorMsg);
            return ResponseEntity.ok(Collections.singletonMap("reply", errorMsg));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Collections.singletonMap("reply", "System Error: " + e.getMessage()));
        }
    }
}
