<?php
session_start();
if (session_id() == '') {
    header("location: https://afetkurtar.site/");
} else if ($_SESSION["userType"] == "volunteerUser") {
    header("location: https://afetkurtar.site/volunteerUser.php");
} else if ($_SESSION["userType"] == "personnelUser") {
    header("location: https://afetkurtar.site/personnelUser.php");
} else if ($_SESSION["userType"] != "authorizedUser") {
    header("location: https://afetkurtar.site/");
}
?>

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta content="width=device-width, inital-scale=1">
    <meta name="google-signin-client_id" content="984932517689-26548cubtdd33uu95vqc4t3ciq1u2os0.apps.googleusercontent.com">
    <title>Afet Kurtar</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-BmbxuPwQa2lc/FVzBcNJ7UAyJxM6wuqIj61tLrc4wSX0szH/Ev+nYRRuWlolflfl" crossorigin="anonymous">
    <link rel="stylesheet" href="css/styles.css">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Newsreader&display=swap" rel="stylesheet">
    <script src="https://apis.google.com/js/platform.js" async defer></script>
</head>

<body>
    <header>
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <div class="container-fluid">
                <a class="navbar-brand" href="#">Afet Kurtar</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <a class="nav-link" aria-current="page" href="/authorizedUser.php">Aktif Afetler</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/personelKaydi.php">Personel Kaydı</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/ihbarlar.php">İhbarlar</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="gonulluIstekleri.php">Gönüllü İstekleri</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/mesajKutusu.php">Bildirim Kutusu</a>
                        </li>
                    </ul>
                </div>
                <div id="logout"><a class="nav-link" href="/cikis.php">Çıkış Yap</a></div>
            </div>
        </nav>
    </header>

    <div class="container main-container">
        <div class="row">
            <div class="col-lg-4 col-md-2"></div>
            <div class="col-lg-4 col-md-8 form-box">
                <form onsubmit="return false;" id="personnelForm">
                    <div class="form-group">
                        <label for="personnelEmail" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Personel Emaili</label>
                        <input type="email" class="form-control" id="personnelEmail" aria-describedby="emailHelp" placeholder="Personelin email adresini giriniz...">
                    </div>
                    <div class="form-group">
                        <label for="personnelUsername" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Personel Adı Soyadı</label>
                        <input type="text" class="form-control" id="personnelUsername" placeholder="Personelin adı soyadını giriniz...">
                    </div>
                    <div class="form-group">
                        <label for="personnelInstitution" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Personel Kurumu</label>
                        <input type="text" class="form-control" id="personnelInstitution" placeholder="Personelin çalıştığı kurumu giriniz...">
                    </div>
                    <div class="form-group">
                        <label for="personnelAddress" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Personel Adresi</label>
                        <input type="text" class="form-control" id="personnelAddress" placeholder="Personelin adresini giriniz...">
                    </div>
                    <input type="button" onclick="registerPersonnel()" class="btn btn-primary" id="registerBtn" value="Kaydı Gerçekleştir"></input>
                </form>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/js/bootstrap.bundle.min.js" integrity="sha384-b5kHyXgcpbZJO/tY9Ul7kGkf1S0CWuKcCD38l8YkeH8z8QjE0GmW1gYU5S9FOnJ0" crossorigin="anonymous"></script>
    <script src="js/script.js"></script>
</body>

</html>