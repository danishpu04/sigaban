package com.pbo.sigaban.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "warga")
public class Warga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nik;
    private String nama;
    private String phone;
    private Integer family;
    private String address;
    private String evacPoint;
    private String health;

    public Warga() {
    }

    public Warga(String nik, String nama, String phone, Integer family, String address, String evacPoint, String health) {
        this.nik = nik;
        this.nama = nama;
        this.phone = phone;
        this.family = family;
        this.address = address;
        this.evacPoint = evacPoint;
        this.health = health;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getFamily() {
        return family;
    }

    public void setFamily(Integer family) {
        this.family = family;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEvacPoint() {
        return evacPoint;
    }

    public void setEvacPoint(String evacPoint) {
        this.evacPoint = evacPoint;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }
}
