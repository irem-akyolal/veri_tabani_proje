document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Sayfanın yenilenmesini engelle

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    // Backend'deki LoginRequest nesnesiyle birebir aynı yapı
    const loginData = {
        email: email,
        password: password
    };

    console.log("Giriş yapılıyor...");

    fetch('/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(loginData)
    })
    .then(response => {
        if (response.ok) {
            return response.json(); // Başarılıysa JSON (Token) döner
        } else {
            // Backend'den 401 UNAUTHORIZED gelirse catch'e düşer
            throw new Error("E-posta veya şifre hatalı!");
        }
    })
    .then(data => {
        // data.token: AuthController içindeki LoginResponse sınıfındaki field
        if (data.token) {
            console.log("Token başarıyla alındı.");
            
            // 1. Token'ı tarayıcı hafızasına kaydet (Bearer token olarak kullanılacak)
            localStorage.setItem('jwtToken', data.token);
            
            // 2. Kullanıcıyı ana sayfaya yönlendir
            window.location.href = 'index.html'; 
        }
    })
    .catch(error => {
        console.error('Hata:', error);
        alert(error.message);
    });
});