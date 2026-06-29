package com.pbo.sigaban.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventaris")
public class Inventaris {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String namaItem; // Beras, Air Bersih, Obat-obatan, Selimut
    private Integer jumlah;
    private String satuan; // Kg, Liter, Paket, Helai
    private String statusKondisi; // Sufficient, Low Stock, dll

    public Inventaris() {
    }

    public Inventaris(String namaItem, Integer jumlah, String satuan, String statusKondisi) {
        this.namaItem = namaItem;
        this.jumlah = jumlah;
        this.satuan = satuan;
        this.statusKondisi = statusKondisi;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamaItem() {
        return namaItem;
    }

    public void setNamaItem(String namaItem) {
        this.namaItem = namaItem;
    }

    public Integer getJumlah() {
        return jumlah;
    }

    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getStatusKondisi() {
        return statusKondisi;
    }

    public void setStatusKondisi(String statusKondisi) {
        this.statusKondisi = statusKondisi;
    }
}
