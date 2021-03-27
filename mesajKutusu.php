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
                            <a class="nav-link active" href="/mesajKutusu.php">Bildirim Kutusu</a>
                        </li>
                    </ul>
                </div>
                <div id="logout"><a class="nav-link" href="/cikis.php">Çıkış Yap</a></div>
            </div>
        </nav>
    </header>

    <?php
            $options = ['http' => [
                'method' => 'POST',
                'header' => 'Content-type:application/json'
                // 'content' => $json
            ]];
            $context = stream_context_create($options);

            //The url you wish to send the POST request to
            $url = "http://afetkurtar.site/api/disasterEvents/read.php";
            $responseDisaster = json_decode(file_get_contents($url, false, $context), true);

            // foreach($responseDisaster['records'] as $row){
            //     $disasterMap[$row['disasterID']] = $row["disasterName"];
            // }

            //The url you wish to send the POST request to
            $url = "http://afetkurtar.site/api/subpart/read.php";
            $responseSubpart = json_decode(file_get_contents($url, false, $context), true);

            // foreach($responseSubpart['records'] as $row){
            //     $subpartMap[$row['subpartID']] = $row["subpartName"];
            // }

            //The url you wish to send the POST request to
            $url = "http://afetkurtar.site/api/status/read.php";
            $responseStatus = json_decode(file_get_contents($url, false, $context), true);

            echo '<ul class="nav nav-tabs">';

            echo '<div class="container-fluid-mesaj-kutusu">';
            echo '<div class="row-fluid-mesaj-kutusu">';
            echo '<div class="span9" id="content">';
            echo '<div class="header-mesaj-kutusu">Afet</div>';
            echo '<div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">';
            foreach($responseDisaster['records'] as $row){
                echo '<a class="nav-link" id="disaster-' . $row["disasterID"] . '-tab" data-toggle="tab" href="#disaster-' . $row["disasterID"] . '" role="tab" aria-controls="disaster-' . $row["disasterID"] . '" aria-selected="false">' . $row["disasterName"] . '</a>';
            }
            echo '</div>';
            echo '</div>';
            echo '</div>';
            echo '</div>';

            echo '<div class="vl"></div>';

            echo '<div class="tab-content" id="v-pills-tabContent">';
            foreach($responseDisaster['records'] as $row){
                echo '<div class="tab-pane fade" id="disaster-' . $row["disasterID"] . '" role="tabpanel" aria-labelledby="disaster' . $row["disasterID"] . '-tab">';
                
                echo '<ul class="nav nav-pills">';

                echo '<div class="container-fluid-mesaj-kutusu">';
                echo '<div class="row-fluid-mesaj-kutusu">';
                echo '<div class="span9" id="content">';
                echo '<div class="header-mesaj-kutusu">Bölge</div>';
                echo '<div class="nav flex-column nav-pills" id="v-pills-tab-subpart" role="tablist" aria-orientation="vertical">';
                foreach($responseSubpart['records'] as $rowSubpart){
                    if ($row["disasterID"] == $rowSubpart["disasterID"]) {
                        echo '<a class="nav-link" id="subpart-' . $rowSubpart["subpartID"] . '-tab" data-toggle="tab" href="#subpart-' . $rowSubpart["subpartID"] . '" role="tab" aria-controls="subpart-' . $rowSubpart["subpartID"] . '" aria-selected="false">' . $rowSubpart["subpartName"] . '</a>';
                    }
                }
                echo '</div>';
                echo '</div>';
                echo '</div>';
                echo '</div>';

                echo '<div class="vl"></div>';

                echo '<div class="tab-content" id="v-pills-tabContent">';
                foreach($responseSubpart['records'] as $rowSubpart){
                    if ($row["disasterID"] == $rowSubpart["disasterID"]) {
                        echo '<div class="tab-pane fade" id="subpart-' . $rowSubpart["subpartID"] . '" role="tabpanel" aria-labelledby="subpart' . $rowSubpart["subpartID"] . '-tab">';
                        // echo $rowSubpart["subpartName"];
                        echo "<table class=\"table table-dark\">
                        <tr>
                        <th>Bildiren Takım</th>
                        <th>Durum</th>
                        <th>Tarih</th>
                        </tr>";

                        foreach($responseStatus['records'] as $rowStatus)
                        {
                            if ($rowStatus["subpartID"] == $rowSubpart["subpartID"]) {
                                echo "<tr>";
                                echo "<td class=\"td-element\">" . $rowStatus['teamID'] . "</td>";
                                echo "<td class=\"td-element\">" . $rowStatus['statusMessage'] . "</td>";
                                echo "<td class=\"td-element\">" . $rowStatus['statusTime'] . "</td>";
                                echo "</tr>";
                            }
                            
                        }
                        echo "</table>";
                        echo '</div>';
                    }
                    
                }
                echo '</div>';
                echo '</ul>';
                echo '</div>';
            }
            echo '</div>';
            echo '</ul>';
        ?>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/js/bootstrap.bundle.min.js" integrity="sha384-b5kHyXgcpbZJO/tY9Ul7kGkf1S0CWuKcCD38l8YkeH8z8QjE0GmW1gYU5S9FOnJ0" crossorigin="anonymous"></script>
    <script src="js/script.js"></script>
</body>

</html>