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
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.24/css/jquery.dataTables.css">
    <link rel="stylesheet" href="css/styles.css">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Newsreader&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-slider/10.0.0/css/bootstrap-slider.min.css">
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet">
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
                            <a class="nav-link" href="/authorizedUser.php">Aktif Afetler</a>
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
                <?php
                echo '<div id="user-profile"><b>'.$_SESSION["userName"].'</b></div>';
                ?>
                <div id="logout"><a class="nav-link" href="/cikis.php">Çıkış Yap</a></div>
            </div>
        </nav>
    </header>

    <div class="container main-container">
        <div class="row">
            <div class="col-lg-4 col-md-2"></div>
            <div class="col-lg-4 col-md-8 form-box">
                <form onsubmit="return false;" id="disasterForm">
                    <div class="form-group">
                        <label for="disasterType" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Afet Türü</label>
                        <select name="disasterType" class="form-control" id="disasterType">
                            <option>Deprem</option>
                            <option>Yangın</option>
                            <option>Sel</option>
                            <option>Heyelan</option>
                            <option>Çığ</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="disasterName" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Afet İsmi</label>
                        <input type="text" name="disasterName" class="form-control" id="disasterName" placeholder="Afetin kaydedileceği ismi giriniz..." required>
                    </div>
                    <div class="form-group">
                        <label id="emergencyLabel" for="emergencyLevel" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Aciliyet Seviyesi</label>
                        <input id="emergencyLevel" onchange="updateEmergency(this.value)" name="emergencyLevel" type="text" data-slider-min="1" data-slider-max="10" data-slider-step="1" data-slider-value="5">
                        <span id="emergencyLevelValue" style="color:white;">5</span>
                    </div>
                    <div class="form-group">
                        <label for="disasterCity" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Afetin Gerçekleştiği Şehir</label>
                        <select name="disasterCity" class="form-control" id="disasterCity">
                            <option value="1">Adana</option>
                            <option value="2">Adıyaman</option>
                            <option value="3">Afyonkarahisar</option>
                            <option value="4">Ağrı</option>
                            <option value="5">Amasya</option>
                            <option value="6">Ankara</option>
                            <option value="7">Antalya</option>
                            <option value="8">Artvin</option>
                            <option value="9">Aydın</option>
                            <option value="10">Balıkesir</option>
                            <option value="11">Bilecik</option>
                            <option value="12">Bingöl</option>
                            <option value="13">Bitlis</option>
                            <option value="14">Bolu</option>
                            <option value="15">Burdur</option>
                            <option value="16">Bursa</option>
                            <option value="17">Çanakkale</option>
                            <option value="18">Çankırı</option>
                            <option value="19">Çorum</option>
                            <option value="20">Denizli</option>
                            <option value="21">Diyarbakır</option>
                            <option value="22">Edirne</option>
                            <option value="23">Elazığ</option>
                            <option value="24">Erzincan</option>
                            <option value="25">Erzurum</option>
                            <option value="26">Eskişehir</option>
                            <option value="27">Gaziantep</option>
                            <option value="28">Giresun</option>
                            <option value="29">Gümüşhane</option>
                            <option value="30">Hakkâri</option>
                            <option value="31">Hatay</option>
                            <option value="32">Isparta</option>
                            <option value="33">Mersin</option>
                            <option value="34">İstanbul</option>
                            <option value="35">İzmir</option>
                            <option value="36">Kars</option>
                            <option value="37">Kastamonu</option>
                            <option value="38">Kayseri</option>
                            <option value="39">Kırklareli</option>
                            <option value="40">Kırşehir</option>
                            <option value="41">Kocaeli</option>
                            <option value="42">Konya</option>
                            <option value="43">Kütahya</option>
                            <option value="44">Malatya</option>
                            <option value="45">Manisa</option>
                            <option value="46">Kahramanmaraş</option>
                            <option value="47">Mardin</option>
                            <option value="48">Muğla</option>
                            <option value="49">Muş</option>
                            <option value="50">Nevşehir</option>
                            <option value="51">Niğde</option>
                            <option value="52">Ordu</option>
                            <option value="53">Rize</option>
                            <option value="54">Sakarya</option>
                            <option value="55">Samsun</option>
                            <option value="56">Siirt</option>
                            <option value="57">Sinop</option>
                            <option value="58">Sivas</option>
                            <option value="59">Tekirdağ</option>
                            <option value="60">Tokat</option>
                            <option value="61">Trabzon</option>
                            <option value="62">Tunceli</option>
                            <option value="63">Şanlıurfa</option>
                            <option value="64">Uşak</option>
                            <option value="65">Van</option>
                            <option value="66">Yozgat</option>
                            <option value="67">Zonguldak</option>
                            <option value="68">Aksaray</option>
                            <option value="69">Bayburt</option>
                            <option value="70">Karaman</option>
                            <option value="71">Kırıkkale</option>
                            <option value="72">Batman</option>
                            <option value="73">Şırnak</option>
                            <option value="74">Bartın</option>
                            <option value="75">Ardahan</option>
                            <option value="76">Iğdır</option>
                            <option value="77">Yalova</option>
                            <option value="78">Karabük</option>
                            <option value="79">Kilis</option>
                            <option value="80">Osmaniye</option>
                            <option value="81">Düzce</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="emergencyAddress" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Afet Üssü Konumu</label>
                        <input type="text" name="emergencyAddress" class="form-control" id="emergencyAddress" placeholder="Afet üssünün adresini giriniz..." required>
                    </div>
                    <input type="submit" onclick="sendDisaster()" class="btn btn-primary" id="registerBtn" value="Afet Kaydını Oluştur"></input>
                </form>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/js/bootstrap.bundle.min.js" integrity="sha384-b5kHyXgcpbZJO/tY9Ul7kGkf1S0CWuKcCD38l8YkeH8z8QjE0GmW1gYU5S9FOnJ0" crossorigin="anonymous"></script>
    <script src="js/script.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-slider/10.0.0/bootstrap-slider.min.js"></script>
    <script>
        // Basic Slider
        var slider = new Slider("#emergencyLevel", {
            tooltip: 'always'
        });
    </script>
</body>

</html>