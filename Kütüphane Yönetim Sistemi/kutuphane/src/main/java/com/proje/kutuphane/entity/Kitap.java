package com.proje.kutuphane.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "kitap")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Kitap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kitap_id")
    private Long kitapId;

    @Column(name = "kitap_ad", nullable = false)
    private String kitapAd;

    @Column(name = "toplam_stok", nullable = false)
    private int toplamStok;

    @Column(name = "mevcut_stok", nullable = false)
    private int mevcutStok;

    @ManyToOne
    @JoinColumn(name = "yazar_id", nullable = false)
    private Yazar yazar;

    @ManyToOne
    @JoinColumn(name = "kategori_id", nullable = false)
    private Kategori kategori;

    @OneToMany(mappedBy = "kitap")
    private List<OduncAlma> oduncAlmalar;
}

