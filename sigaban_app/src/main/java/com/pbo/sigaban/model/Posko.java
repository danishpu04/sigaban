package com.pbo.sigaban.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "posko")
public class Posko {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nama;
    private String alamat;
    private int kapasitasMaksimal;
    private String statusLogistik;
    private String noTelepon;

    @Transient
    private int kapasitasSaatIni;

    @Transient
    private String statusKepenuhan;

    @Transient
    private int persentase;

    public Posko() {
    }

    public Posko(String nama, String alamat, int kapasitasMaksimal, String statusLogistik, String noTelepon) {
        this.nama = nama;
        this.alamat = alamat;
        this.kapasitasMaksimal = kapasitasMaksimal;
        this.statusLogistik = statusLogistik;
        this.noTelepon = noTelepon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public int getKapasitasMaksimal() {
        return kapasitasMaksimal;
    }

    public void setKapasitasMaksimal(int kapasitasMaksimal) {
        this.kapasitasMaksimal = kapasitasMaksimal;
    }

    public String getStatusLogistik() {
        return statusLogistik;
    }

    public void setStatusLogistik(String statusLogistik) {
        this.statusLogistik = statusLogistik;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public int getKapasitasSaatIni() {
        return kapasitasSaatIni;
    }

    public void setKapasitasSaatIni(int kapasitasSaatIni) {
        this.kapasitasSaatIni = kapasitasSaatIni;
    }

    public String getStatusKepenuhan() {
        return statusKepenuhan;
    }

    public void setStatusKepenuhan(String statusKepenuhan) {
        this.statusKepenuhan = statusKepenuhan;
    }

    public int getPersentase() {
        return persentase;
    }

    public void setPersentase(int persentase) {
        this.persentase = persentase;
    }
}
