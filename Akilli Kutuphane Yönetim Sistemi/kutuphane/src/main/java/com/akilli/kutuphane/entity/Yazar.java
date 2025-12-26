package com.akilli.kutuphane.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "yazar")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Yazar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "yazar_id")
    private Long yazarId;

    @Column(name = "yazar_ad", nullable = false, length = 50)
    private String yazarAd;

    @Column(name = "yazar_soyad", nullable = false, length = 50)
    private String yazarSoyad;

    
    @OneToMany(mappedBy = "yazar")
    @JsonIgnore // sonsuz bir döngü olmasın diye
    private List<Kitap> kitaplar;
}
