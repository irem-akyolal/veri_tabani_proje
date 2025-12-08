package com.proje.kutuphane.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "kategori")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Kategori {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kategori_id")
    private Long kategoriId;

    @Column(name = "kategori_ad", nullable = false, length = 50)
    private String kategoriAd;

    @OneToMany(mappedBy = "kategori")
    private List<Kitap> kitaplar;
}

