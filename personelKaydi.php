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
                            <a class="nav-link" aria-current="page" href="/authorizedUser.php">Aktif Afetler</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active" href="/personelKaydi.php">Personeller</a>
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

    <!-- <input type="button" id="disasterCreate" class="btn btn-primary" onclick="addDisaster()" value="Afet Oluştur"></input> -->

    <?php

    //The url you wish to send the POST request to
    $url = "http://afetkurtar.site/api/personnelUser/read.php";

    $options = ['http' => [
        'method' => 'POST',
        'header' => 'Content-type:application/json'
        // 'content' => $json
    ]];

    $context = stream_context_create($options);
    $response = json_decode(file_get_contents($url, false, $context), true);

    echo "<table id=\"disaster-table\" class=\"table table-dark table-striped table-bordered\" style=\"text-align:center\">
            <thead>
            <tr>
            <th>Personel Adı</th>
            <th>Personel Maili</th>
            <th>Personel Takımı</th>
            <th>Personel Konumu</th>
            <th>Personel Kurumu</th>
            <th>Detay</th>
            </tr>
            <thead>
            <tbody>";



    foreach ($response['records'] as $row) {
        echo "<tr>";
        echo "<td class=\"td-element\">" . $row['personnelName'] . "</td>";
        echo "<td class=\"td-element\">" . $row['personnelEmail'] . "</td>";
        echo "<td class=\"td-element\">" . $row['teamID'] . "</td>";
        echo "<td><img style=\"height: 100px;\" src=\"https://maps.googleapis.com/maps/api/staticmap?center=" . $row['latitude'] . "," . $row['longitude'] . "&zoom=12&size=200x150&key=AIzaSyCxLUKYaDqQEIIQGQGQmC0ipdS04IXRoRw\"/></td>";
        echo "<td class=\"td-element\">" . $row['institution'] . "</td>";
        echo "<td class=\"td-element\"></input><input type=\"button\" style=\"margin-left:20px;\" value=\"Sil\" onclick=\"deletePersonnel(this.id)\" class=\"btn btn-danger\" id=\"" . $row['personnelID'] . "\"\></input></td>";
        echo "</tr>";
    }
    echo "<tbody>";
    echo "</table>";

    ?>
    <a href="../personelKayitFormu.php" class="float">
        <i class="fa fa-plus my-float fa-2x"></i>
    </a>
    <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/js/bootstrap.bundle.min.js" integrity="sha384-b5kHyXgcpbZJO/tY9Ul7kGkf1S0CWuKcCD38l8YkeH8z8QjE0GmW1gYU5S9FOnJ0" crossorigin="anonymous"></script>
    <script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.24/js/jquery.dataTables.js"></script>
    <script src="js/script.js"></script>
</body>

</html>