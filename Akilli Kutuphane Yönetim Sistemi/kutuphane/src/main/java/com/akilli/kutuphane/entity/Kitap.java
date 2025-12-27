package com.akilli.kutuphane.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "yazar_id", nullable = false)
    private Yazar yazar;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kategori_id", nullable = false)
    private Kategori kategori;

    
   
    @OneToMany(mappedBy = "kitap")
    @JsonIgnore
    private List<OduncAlma> oduncAlmalar;
}
