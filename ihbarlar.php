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
                            <a class="nav-link active" href="/ihbarlar.php">İhbarlar</a>
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

    <?php

        //<img src="https://maps.googleapis.com/maps/api/staticmap?center=40.714728,-73.998672&zoom=12&size=600x400&key=AIzaSyCSHwrddoGGXBr4TpzN8HqAUQ8wjXSDIPY"/>

            function getaddress($lat,$lng)
            {
                $url = 'https://maps.googleapis.com/maps/api/geocode/json?latlng='.trim($lat).','.trim($lng).'&sensor=false&key=AIzaSyCxLUKYaDqQEIIQGQGQmC0ipdS04IXRoRw';
                $json = @file_get_contents($url);
                $data=json_decode($json);
                $status = $data->status;
                if($status=="OK")
                {
                    return $data->results[0]->formatted_address;
                }
                else
                {
                    return false;
                }
            }
            //The url you wish to send the POST request to
            $url = "http://afetkurtar.site/api/notice/read.php";

            $options = ['http' => [
                'method' => 'POST',
                'header' => 'Content-type:application/json'
                // 'content' => $json
            ]];

            $context = stream_context_create($options);
            $response = json_decode(file_get_contents($url, false, $context), true);

            echo "<table class=\"table table-dark\">
            <tr>
            <th>İhbar Türü</th>
            <th>Mesaj</th>
            <th>İhbar Konumu</th>
            <th>Konum Fotoğrafı</th>
            <th>İhbar Fotoğrafı</th>
            </tr>";

            

            foreach($response['records'] as $row)
            {
                $address = getaddress($row['latitude'], $row['longitude']);

                echo "<tr>";
                echo "<td class=\"td-element\">" . $row['type'] . "</td>";
                echo "<td class=\"td-element\">" . $row['message'] . "</td>";
                echo "<td class=\"td-element\">" . $address . "</td>";
                echo "<td><img style=\"height: 150px;\" src=\"https://maps.googleapis.com/maps/api/staticmap?center=" . $row['latitude'] . "," . $row['longitude'] . "&zoom=12&size=200x200&key=AIzaSyCxLUKYaDqQEIIQGQGQmC0ipdS04IXRoRw\"/></td>";
                echo "<td><img class=\"img-responsive\" style=\"height: 150px;\" src=\"" . $row['imageURL'] . "\"/></td>";
                echo "</tr>";
            }
            echo "</table>";

        ?>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/js/bootstrap.bundle.min.js" integrity="sha384-b5kHyXgcpbZJO/tY9Ul7kGkf1S0CWuKcCD38l8YkeH8z8QjE0GmW1gYU5S9FOnJ0" crossorigin="anonymous"></script>
    <script src="js/script.js"></script>
</body>

</html>