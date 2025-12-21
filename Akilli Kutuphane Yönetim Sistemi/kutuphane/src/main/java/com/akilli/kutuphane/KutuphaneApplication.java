package com.akilli.kutuphane;

import com.akilli.kutuphane.Service.KullaniciService;
import com.akilli.kutuphane.entity.Kullanici; // bunları yeni ekledim

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KutuphaneApplication {

	public static void main(String[] args) {
		SpringApplication.run(KutuphaneApplication.class, args);

	
	}

// bunu da yeni ekledim otomatik admin oluşsun diye
		@Bean
      CommandLineRunner createInitialAdmin(KullaniciService service) {
    return args -> {
        if (!service.existsByEposta("admin@kutuphane.com")) {
            Kullanici admin = new Kullanici();
            admin.setKullaniciAd("Admin");
            admin.setKullaniciSoyad("Sistem");
            admin.setKullaniciEposta("admin@kutuphane.com");
            admin.setKullaniciSifre("admin123"); // Service zaten bunu encode edecek
            admin.setRol(Kullanici.Rol.admin);
            service.register(admin);
            System.out.println("Sistem Admini oluşturuldu: admin@kutuphane.com / admin123");
        }
    };
}

}
