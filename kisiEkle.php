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
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">    
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.24/css/jquery.dataTables.css">
    <link rel="stylesheet" href="css/styles.css">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Newsreader&display=swap" rel="stylesheet">
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet">
    <script src="https://apis.google.com/js/platform.js" async defer></script>
    

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
                    
                    </ul>
                </div>
                <div id="logout"><a class="nav-link" href="/cikis.php">Çıkış Yap</a></div>
            </div>
        </nav>
    </header>

    <!-- <input type="button" id="disasterCreate" class="btn btn-primary" onclick="addDisaster()" value="Afet Oluştur"></input> -->

    <?php

    //The url you wish to send the POST request to
    $url = "https://afetkurtar.site/api/personnelUser/search.php";

        $body = '{
            "teamID": 0
        }';
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array("Content-Type: application/json"));
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $body);
        $response = json_decode(curl_exec($ch),true);

    echo "<table id=\"disaster-table\" class=\"table table-dark table-striped table-bordered\" style=\"text-align:center\">
            <thead>
            <tr>
            <th>Personel Adı</th>
            <th>Email</th>
            <th>Rol</th>
            <th>İşlem</th>
            </tr>
            <thead>
            <tbody>";



    foreach ($response['records'] as $row) {
        echo "<tr>";
        echo "<td class=\"td-element\">" . $row['personnelName'] . "</td>";
        echo "<td class=\"td-element\">" . $row['personnelEmail'] . "</td>";
        echo "<td class=\"td-element\">" . $row['personnelRole'] . "</td>";

        echo '<td class="td-element">';
            // echo '<div class="btn-group">';
            // echo '<button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">';
            // echo 'Ekle';
            // echo '</button>';
            // echo '<div class="dropdown-menu">';
            // echo '<a class="personDropItem" id="kaptan'. $row["personnelID"] .'" personnel-id="'. $row["personnelID"] .'" role="Kaptan" href="#">Kaptan</a>';
            // echo '<a class="personDropItem" id="normal'. $row["personnelID"] .'" personnel-id="'. $row["personnelID"] .'" role="Normal" href="#">Normal</a>';
            // echo '</div>';
            // echo '</div>';
            echo '<input type="button" value="Kaptan" onclick="addPersonnelToSubpart(' . $row['personnelID'] . ','.$_GET["id"].','.$_GET["disasterID"].', \'Kaptan\')" class="btn btn-success" id="kaptan-' . $row['personnelID'] . '"></input>';
            echo '<input type="button" value="Normal" onclick="addPersonnelToSubpart(' . $row['personnelID'] . ','.$_GET["id"].','.$_GET["disasterID"].', \'Normal\')" class="btn btn-primary" id="normal-' . $row['personnelID'] . '" style="margin-left:20px;"></input>';
        echo '</td>';
        echo "</tr>";
    }

    echo "<tbody>";
    echo "</table>";

    ?>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
    <script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.24/js/jquery.dataTables.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/js/bootstrap.bundle.min.js" integrity="sha384-JEW9xMcG8R+pH31jmWH6WWP0WintQrMb4s7ZOdauHnUtxwoG2vI5DkLtS3qm9Ekf" crossorigin="anonymous"></script>
    <script src="js/script.js"></script>


    <?php
        echo '<script>';
        echo '$(".personDropItem").click(function(e) {';
        echo 'e.preventDefault();';
        echo 'var role = document.getElementById(e.target.id).getAttribute("role");';
        echo 'var personnelID = document.getElementById(e.target.id).getAttribute("personnel-id");';
        echo 'addPersonnelToSubpart(personnelID,'.$_GET["id"].','.$_GET["disasterID"].', role);';
        echo '});';
        echo '</script>';
    ?>
</body>

</html>