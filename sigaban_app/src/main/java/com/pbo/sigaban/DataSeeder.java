package com.pbo.sigaban;

import com.pbo.sigaban.model.User;
import com.pbo.sigaban.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private com.pbo.sigaban.repository.WargaRepository wargaRepository;

    @Autowired
    private com.pbo.sigaban.repository.PoskoRepository poskoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Seed default admin user if not exists
        if (userRepository.count() == 0) {
            User admin = new User("admin", "admin123", "Administrator", "ADMIN", "admin@sigaban.gov", "081234567890");
            userRepository.save(admin);
            
            System.out.println("=========================================================");
            System.out.println("Default Admin User Created:");
            System.out.println("Username: admin");
            System.out.println("Password: admin123");
            System.out.println("=========================================================");
        }

        // Seed default warga if not exists
        if (wargaRepository.count() == 0) {
            com.pbo.sigaban.model.Warga warga1 = new com.pbo.sigaban.model.Warga();
            warga1.setNik("3174012345678901");
            warga1.setNama("Budi Santoso");
            warga1.setPhone("081234567890");
            warga1.setFamily(3);
            warga1.setAddress("Jl. Bantaran Sungai Ciliwung No. 45, RT 02/RW 04, Kampung Melayu, Jakarta Timur");
            warga1.setEvacPoint("GOR Jatinegara");
            warga1.setHealth("kritis");
            wargaRepository.save(warga1);

            com.pbo.sigaban.model.Warga warga2 = new com.pbo.sigaban.model.Warga();
            warga2.setNik("3174019876543210");
            warga2.setNama("Ani Wijaya");
            warga2.setPhone("081987654321");
            warga2.setFamily(2);
            warga2.setAddress("Jl. Otista Raya No. 12, RT 05/RW 01, Bidara Cina, Jakarta Timur");
            warga2.setEvacPoint("Posko Utama Balai Kota");
            warga2.setHealth("sehat");
            wargaRepository.save(warga2);

            System.out.println("Default Warga Data Seeded.");
        }

        // Seed Poskos
        String[][] poskoData = {
            {"Posko GOR Jakarta Utara", "Jl. Yos Sudarso No.22", "500", "Aman", "0812-3456-7890"},
            {"Balai Warga RW 04", "Kel. Pluit, Penjaringan", "200", "Menipis", "0812-9876-5432"},
            {"SDN 01 Kapuk Muara", "Jl. Kapuk Raya", "300", "Kosong", "0855-1122-3344"},
            {"GOR Jatinegara", "Jl. Jatinegara Timur", "400", "Aman", "0811-2233-4455"},
            {"Posko Utama Balai Kota", "Jl. Medan Merdeka Selatan", "1000", "Aman", "021-1234567"},
            {"Posko Pengungsian SMPN 10", "Alamat Belum Tersedia", "350", "Aman", "-"},
            {"SDN 01 Jakarta", "Alamat Belum Tersedia", "250", "Aman", "-"},
            {"Posko Balai Desa", "Alamat Belum Tersedia", "150", "Aman", "-"}
        };

        for (String[] p : poskoData) {
            if (poskoRepository.findByNama(p[0]).isEmpty()) {
                poskoRepository.save(new com.pbo.sigaban.model.Posko(p[0], p[1], Integer.parseInt(p[2]), p[3], p[4]));
            }
        }
        System.out.println("Posko Data Seeded/Checked.");
    }
}
