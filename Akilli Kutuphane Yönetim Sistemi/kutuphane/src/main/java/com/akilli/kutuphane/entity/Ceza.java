package com.akilli.kutuphane.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ceza")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ceza {

    public enum OdemeDurumu {
        odenmis,
        bekliyor
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ceza_id")
    private Long cezaId;

    @Column(name = "ceza_miktari", nullable = false)
    private double cezaMiktari;

    @Enumerated(EnumType.STRING)
    @Column(name = "odeme_durumu", nullable = false)
    private OdemeDurumu odemeDurumu;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "odunc_id", nullable = false)
    private OduncAlma oduncAlma;
}
