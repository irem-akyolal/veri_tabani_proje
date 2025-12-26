package com.akilli.kutuphane.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonIgnore // sonsuz bir döngü olmasın diye
    private List<Kitap> kitaplar;
}
