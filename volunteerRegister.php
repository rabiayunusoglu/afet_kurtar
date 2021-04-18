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
                <div id="logout"><a class="nav-link" href="/cikis.php">Çıkış Yap</a></div>
            </div>
        </nav>
    </header>

    <div class="container main-container">
        <div class="row">
            <div class="col-lg-4 col-md-2"></div>
            <div class="col-lg-4 col-md-8 form-box">
                <form onsubmit="return false;" id="volunteerForm">
                    <div class="form-group">
                        <label for="volunteerUsername" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Gönüllü Adı Soyadı</label>
                        <input type="text" class="form-control" id="volunteerUsername" placeholder="Adınızı ve soyadınızı giriniz...">
                    </div>
                    <div class="form-group">
                        <label for="volunteerAddress" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Gönüllü Adresi</label>
                        <input type="text" class="form-control" id="volunteerAddress" placeholder="Adresinizi giriniz...">
                    </div>
                    <div class="form-group">
                        <label for="volunteerTc" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Gönüllü Kimlik Numarası</label>
                        <input type="text" class="form-control" id="volunteerTc" placeholder="Kimlik numaranızı giriniz...">
                    </div>
                    <div class="form-group">
                        <label for="volunteerTel" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Gönüllü Telefon Numarası</label>
                        <input type="text" class="form-control" id="volunteerTel" placeholder="Telefon numaranızı giriniz...">
                    </div>
                    <?php
                    echo '<div class="form-group row">';
                        echo '<div class="col-lg-6 col-md-4">';
                        echo '<label for="isExperienced" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Arama-Kurtarma Tecrübesi</label>';
                        echo '<div><input type="checkbox" id="isExperienced" ></div>';
                        echo '</div>';
                        echo '<div class="col-lg-6 col-md-4">';
                        echo '<label for="haveCert" style="font-size: 20px; font-weight:bold; color: #ECF0F5">İlk Yardım Sertifikası</label>';
                        echo '<div><input type="checkbox" id="haveCert"></div>';
                        echo '</div>';
                    echo '</div>';
                    echo '<input type="button" onclick="registerVolunteer(\''. $_GET["email"] .'\')" class="btn btn-primary" id="registerBtn" value="Kayıt Ol"></input>'
                    ?>
                </form>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/js/bootstrap.bundle.min.js" integrity="sha384-b5kHyXgcpbZJO/tY9Ul7kGkf1S0CWuKcCD38l8YkeH8z8QjE0GmW1gYU5S9FOnJ0" crossorigin="anonymous"></script>
    <script src="js/script.js"></script>
</body>

</html>