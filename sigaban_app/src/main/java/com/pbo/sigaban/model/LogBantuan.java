package com.pbo.sigaban.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_bantuan")
public class LogBantuan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipe; // MASUK, KELUAR
    private String pihakTerkait; // Donatur (jika MASUK), Tujuan (jika KELUAR)
    private String labelPihak; // Gov Agency, Private Org, dll
    private String deskripsiItem; // 500 Kg Beras, dsb.
    private LocalDateTime waktu; // Waktu kejadian
    private String status; // Logged, Processing, Delivered, In Transit

    public LogBantuan() {
        this.waktu = LocalDateTime.now();
    }

    public LogBantuan(String tipe, String pihakTerkait, String labelPihak, String deskripsiItem, String status) {
        this.tipe = tipe;
        this.pihakTerkait = pihakTerkait;
        this.labelPihak = labelPihak;
        this.deskripsiItem = deskripsiItem;
        this.waktu = LocalDateTime.now();
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getPihakTerkait() {
        return pihakTerkait;
    }

    public void setPihakTerkait(String pihakTerkait) {
        this.pihakTerkait = pihakTerkait;
    }

    public String getLabelPihak() {
        return labelPihak;
    }

    public void setLabelPihak(String labelPihak) {
        this.labelPihak = labelPihak;
    }

    public String getDeskripsiItem() {
        return deskripsiItem;
    }

    public void setDeskripsiItem(String deskripsiItem) {
        this.deskripsiItem = deskripsiItem;
    }

    public LocalDateTime getWaktu() {
        return waktu;
    }

    public void setWaktu(LocalDateTime waktu) {
        this.waktu = waktu;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
