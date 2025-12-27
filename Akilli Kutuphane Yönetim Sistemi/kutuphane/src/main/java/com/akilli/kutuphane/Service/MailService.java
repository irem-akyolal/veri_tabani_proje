package com.akilli.kutuphane.Service;

import com.akilli.kutuphane.entity.Ceza;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendCezaMail(Ceza ceza) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(ceza.getOduncAlma().getKullanici().getKullaniciEposta());
        message.setSubject("Kütüphane Ceza Bildirimi");
        message.setText("Sayın " + ceza.getOduncAlma().getKullanici().getKullaniciAd() +
                        ", ödünç aldığınız kitap " +
                        ceza.getOduncAlma().getKitap().getKitapAd() +
                        " için ceza miktarınız: " + ceza.getCezaMiktari() + " TL'dir.");

        mailSender.send(message);
    }
}




