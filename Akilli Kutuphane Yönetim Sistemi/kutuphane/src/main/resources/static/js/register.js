document.getElementById('registerForm').addEventListener('submit', function(event) {
    event.preventDefault();

    // HTML'deki id'ler ile eşleşen verileri alıyoruz
    const ad = document.getElementById('ad').value;
    const soyad = document.getElementById('soyad').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    // Java tarafındaki RegisterRequest sınıfıyla birebir aynı yapı
    const registerData = {
        ad: ad,
        soyad: soyad,
        email: email,
        password: password
    };

    console.log("Gönderilecek veri:", registerData);

    // AuthController içindeki @PostMapping("/register") adresine gidiyoruz
    fetch('/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(registerData)
    })
    .then(response => {
        // Backend HttpStatus.CREATED (201) dönerse
        if (response.ok || response.status === 201) {
            alert("Kayıt başarıyla tamamlandı!");
            window.location.href = 'login.html'; // Kayıttan sonra login'e yönlendir
        } else {
            alert("Kayıt sırasında bir hata oluştu! (Hata Kodu: " + response.status + ")");
        }
    })
    .catch(error => {
        console.error('Hata:', error);
        alert("Sunucuya bağlanılamadı.");
    });
});
