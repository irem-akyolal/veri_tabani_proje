package com.proje.kutuphane.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "kullanici")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Kullanici {

    public enum Rol {
        ogrenci,
        admin
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kullanici_id")
    private Long kullaniciId;

    @Column(name = "kullanici_ad", nullable = false, length = 50)
    private String kullaniciAd;

    @Column(name = "kullanici_soyad", nullable = false, length = 50)
    private String kullaniciSoyad;

    @Column(name = "kullanici_eposta", nullable = false, unique = true, length = 100)
    private String kullaniciEposta;

    @Column(name = "kullanici_sifre", nullable = false)
    private String kullaniciSifre;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Rol rol;

    @OneToMany(mappedBy = "kullanici")
    private List<OduncAlma> oduncAlmalar;
}
