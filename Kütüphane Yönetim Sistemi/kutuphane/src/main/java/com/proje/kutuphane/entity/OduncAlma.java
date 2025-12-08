package com.proje.kutuphane.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "odunc_alma")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OduncAlma {

    public enum OduncDurum {
        oduncte,
        iade_edildi,
        gecikmis,
        iptal_edildi
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "odunc_id")
    private Long oduncId;

    @Enumerated(EnumType.STRING)
    @Column(name = "durum", nullable = false)
    private OduncDurum durum;

    @Column(name = "odunc_tarihi", nullable = false)
    private LocalDate oduncTarihi;

    @Column(name = "planlanan_iade_tarihi", nullable = false)
    private LocalDate planlananIadeTarihi;

    @Column(name = "gercek_iade_tarihi")
    private LocalDate gercekIadeTarihi;

    @ManyToOne
    @JoinColumn(name = "kullanici_id", nullable = false)
    private Kullanici kullanici;

    @ManyToOne
    @JoinColumn(name = "kitap_id", nullable = false)
    private Kitap kitap;

    @OneToOne(mappedBy = "oduncAlma")
    private Ceza ceza;
}


