<?php
session_start();
if (session_id() == '') {
    header("location: https://afetkurtar.site/");
} else if ($_SESSION["userType"] == "authorizedUser") {
    header("location: https://afetkurtar.site/authorizedUser.php");
} else if ($_SESSION["userType"] == "personnelUser") {
    header("location: https://afetkurtar.site/personnelUser.php");
} else if ($_SESSION["userType"] != "volunteerUser") {
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
                            <a class="nav-link active" aria-current="page" href="/volunteerUser.php">Afet Arama Kurtarma Katılım İsteği</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/gonulluIhbarlar.php">İhbarda Bulun</a>
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
                <form onsubmit="return false;" id="noticeForm">
                    <div class="form-group">
                        <label for="noticeType" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Afet İhbar Türü</label>
                        <select name= "noticeType" class="form-control" id="noticeType">
                            <option>Deprem</option>
                            <option>Yangın</option>
                            <option>Sel</option>
                            <option>Heyelan</option>
                            <option>Çığ</option>
                        </select>
                    </div>
                    <div class="form-group" action="" method="post" enctype="multipart/form-data">
                        <label for="noticeAddress" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Afet İhbar Konumu</label>
                        <input type="text" name= "noticeAddress" class="form-control" id="noticeAddress" placeholder="Afet adresini giriniz..." required>
                    </div>
                    <div class="form-group">
                        <label for="noticeMessage" style="font-size: 20px; font-weight:bold; color: #ECF0F5">İhbar Mesajı</label>
                        <input type="text" name= "noticeMessage" class="form-control" id="noticeMessage" placeholder="İhbar mesajını giriniz..." required>
                    </div>
                    <div class="form-group">
                        <label for="noticeImage" style="font-size: 20px; font-weight:bold; color: #ECF0F5">İhbar Fotoğrafı</label>
                        <input type="file" name= "noticeImage" class="form-control" id="noticeImage" placeholder="İhbar fotoğrafını yükleyiniz..." required>
                    </div>
                    <input type="submit" onclick="sendNotice()" class="btn btn-primary" id="registerBtn">İhbarı Gönder</button>
                </form>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/js/bootstrap.bundle.min.js" integrity="sha384-b5kHyXgcpbZJO/tY9Ul7kGkf1S0CWuKcCD38l8YkeH8z8QjE0GmW1gYU5S9FOnJ0" crossorigin="anonymous"></script>
    <script src="js/script.js"></script>
</body>

</html>