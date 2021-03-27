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
                            <a class="nav-link" href="/mesajKutusu.php">Mesaj Kutusu</a>
                        </li>
                    </ul>
                </div>
                <div id="logout"><a class="nav-link" href="/cikis.php">Çıkış Yap</a></div>
            </div>
        </nav>
    </header>

    <?php
    $disasterID = 0;
    $latitude = 0.0;
    $longitude = 0.0;
    $disasterType = "";

    if (isset($_GET["id"])) {
        //The url you wish to send the POST request to
        $url = "https://afetkurtar.site/api/disasterEvents/search.php";

        $body = '{
            "disasterID":'.$_GET["id"].'
          }';
          $ch = curl_init();
          curl_setopt($ch, CURLOPT_URL, $url);
          curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
          curl_setopt($ch, CURLOPT_HTTPHEADER, array("Content-Type: application/json"));
          curl_setopt($ch, CURLOPT_POST, 1);
          curl_setopt($ch, CURLOPT_POSTFIELDS, $body);
          $response = json_decode(curl_exec($ch),true);

        foreach ($response['records'] as $row) {
            $disasterID = $row['disasterID'];
            $disasterType = $row['disasterType'];
            $latitude = $row['latitude'];
            $longitude= $row['longitude'];
        }
    }
    //echo "<div id=\"map\" style=\"width:600px;height:100vh\" disaster-id=\"" . $row['disasterID']."\" disaster-type=\"" . $row['disasterType']."\" latitude=\"" . $row['latitude']."\" longitude=\"" . $row['longitude']."\"></div>";

    //The url you wish to send the POST request to
    $url = "https://afetkurtar.site/api/subpart/search.php";

    $body = '{
        "disasterID":'.$_GET["id"].'
      }';
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_HTTPHEADER, array("Content-Type: application/json"));
    curl_setopt($ch, CURLOPT_POST, 1);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $body);
    $response = json_decode(curl_exec($ch),true);

    foreach ($response['records'] as $row) {
        echo "<div name=\"subpart\" subpart-id=\"" . $row['subpartID']."\" subpart-name=\"" . $row['subpartName']."\" latitude=\"" . $row['latitude']."\" longitude=\"" . $row['longitude']."\"></div>";
    }

    echo '<ul class="nav nav-tabs">';

    echo "<div id=\"map\" style=\"width:600px;height:100vh\" disaster-id=\"" . $row['disasterID']."\" disaster-type=\"" . $row['disasterType']."\" latitude=\"" . $row['latitude']."\" longitude=\"" . $row['longitude']."\"></div>";

    echo '<div class="container-fluid-edit-disaster">';
    echo '<div class="row-fluid-edit-disaster">';
    echo '<div class="span9" id="content">';
    echo '<div class="header-edit-disaster">Bölge</div>';

    echo '<div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">';
    foreach($response['records'] as $row){
        echo '<a class="nav-link" id="subpart-' . $row["subpartID"] . '-tab" data-toggle="tab" href="#subpart-' . $row["subpartID"] . '" role="tab" aria-controls="subpart-' . $row["subpartID"] . '" aria-selected="false">' . $row["subpartName"] . '</a>';
    }
    echo '</div>';

    echo '</div>';
    echo '</div>';
    echo '</div>';

    echo '<div class="vl"></div>';

    echo '<div class="tab-content" id="v-pills-tabContent">';
    foreach($response['records'] as $row){
        echo '<div class="tab-pane fade" id="subpart-' . $row["subpartID"] . '" role="tabpanel" aria-labelledby="subpart' . $row["subpartID"] . '-tab">';
        
        echo $row["subpartName"];
        echo '</div>';
    }
    echo '</div>';

    echo '</ul>';
    ?>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/js/bootstrap.bundle.min.js" integrity="sha384-b5kHyXgcpbZJO/tY9Ul7kGkf1S0CWuKcCD38l8YkeH8z8QjE0GmW1gYU5S9FOnJ0" crossorigin="anonymous"></script>
    <script src="js/script.js"></script>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCxLUKYaDqQEIIQGQGQmC0ipdS04IXRoRw&callback=initMap&libraries=&v=weekly" async></script>
</body>
</html>