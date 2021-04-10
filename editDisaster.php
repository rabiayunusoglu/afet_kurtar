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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-slider/10.0.0/css/bootstrap-slider.min.css">
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
                <div id="logout"><a class="nav-link" href="/cikis.php">Çıkış Yap</a></div>
            </div>
        </nav>
    </header>

    <?php
    $disasterID = 0;
    $latitude = 0.0;
    $longitude = 0.0;
    $disasterType = "";
    $disasterBase = "";
    $disasterName = "";

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
            $disasterName = $row['disasterName'];
            $disasterBase = $row['disasterBase'];

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

    echo "<div id=\"map\" style=\"width:50vw;height:93.97vh\" disaster-id=\"" . $disasterID . "\" disaster-type=\"" . $disasterType . "\" disaster-base=\"" . $disasterBase . "\" disaster-name=\"" . $disasterName . "\" latitude=\"" . $latitude . "\" longitude=\"" . $longitude . "\"></div>";

    echo '<div class="container-fluid-edit-disaster">';
    echo '<div class="row-fluid-edit-disaster">';
    echo '<div class="span9" id="content">';
    echo '<div class="header-edit-disaster">Bölge</div>';

    echo '<div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">';
    foreach($response['records'] as $row){
        echo '<a class="nav-link" id="subpart-' . $row["subpartID"] . '-tab" subpart-id="'. $row["subpartID"] .'" data-toggle="tab" href="#subpart-' . $row["subpartID"] . '" role="tab" aria-controls="subpart-' . $row["subpartID"] . '" aria-selected="false">' . $row["subpartName"] . '</a>';
    }
    echo '<a class="nav-link" id="subpart-add-tab" data-toggle="tab" href="#subpart-add" role="tab" aria-controls="subpart-add" aria-selected="false"> <i class="fa fa-plus my-float fa-1x">Ekle</i> </a>';
    echo '</div>';

    echo '</div>';
    echo '</div>';
    echo '</div>';

    echo '<div class="vl"></div>';

    echo '<div class="tab-content" id="v-pills-tabContent">';
    foreach($response['records'] as $row){
        echo '<div class="tab-pane fade" id="subpart-' . $row["subpartID"] . '" role="tabpanel" aria-labelledby="subpart-' . $row["subpartID"] . '-tab">';
        /////
        $checkedString = "";
        if($row["isOpenForVolunteers"] == 1){
            $checkedString = " checked";
        }
        

        echo '<div class="container main-container" style="margin-left:0px;">';
        echo '<div class="row">';
        echo '<div class="col-lg-12 col-md-8"></div>';
        echo '<div class="col-lg-12 col-md-8 form-box">';
        echo '<form onsubmit="return false;" id="editSubpartForm">';
        echo '<div class="form-group">';
        echo '<label for="subpartName'.$row["subpartID"].'" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Bölge Adı</label>';
        echo '<input type="email" class="form-control" id="subpartName'.$row["subpartID"].'" value="'.$row["subpartName"].'">';
        echo '</div>';
        echo '<div class="form-group row">';
        echo '<div class="col-lg-6 col-md-4">';
        echo '<label for="subpartLatitude'.$row["subpartID"].'" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Enlem</label>';
        echo '<input type="text" class="form-control" id="subpartLatitude'.$row["subpartID"].'" value="'.$row["latitude"].'">';
        echo '</div>';
        //echo '</div>';
        //echo '<div class="form-group">';
        echo '<div class="col-lg-6 col-md-4">';
        echo '<label for="subpartLongitude'.$row["subpartID"].'" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Boylam</label>';
        echo '<input type="text" class="form-control" id="subpartLongitude'.$row["subpartID"].'" value="'.$row["longitude"].'">';
        echo '</div>';
        echo '</div>';
        echo '<div class="form-group row">';
        echo '<div class="col-lg-6 col-md-4">';
        echo '<label for="subpartMissingPerson'.$row["subpartID"].'" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Kayıp İnsan Sayısı</label>';
        echo '<input type="text" class="form-control" id="subpartMissingPerson'.$row["subpartID"].'" value="'.$row["missingPerson"].'">';
        echo '</div>';
        //echo '</div>';
        //echo '<div class="form-group">';
        echo '<div class="col-lg-6 col-md-4">';
        echo '<label for="subpartRescuedPerson'.$row["subpartID"].'" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Kurtarılan İnsan Sayısı</label>';
        echo '<input type="text" class="form-control" id="subpartRescuedPerson'.$row["subpartID"].'" value="'.$row["rescuedPerson"].'">';
        echo '</div>';
        echo '</div>';
        echo '<div class="form-group row">';
        echo '<div class="col-lg-6 col-md-4">';
        echo '<label for="subpartIsOpenForVolunteers'.$row["subpartID"].'" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Gönüllü Katılım</label>';
        echo '<div><input type="checkbox" id="subpartIsOpenForVolunteers'.$row["subpartID"].'" '.$checkedString.'></div>';
        echo '</div>';
        //echo '</div>';
        //echo '<div class="form-group">';
        echo '<div class="col-lg-6 col-md-4">';
        echo '<label for="emergencyLevel'.$row["subpartID"].'" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Aciliyet Seviyesi</label>';
        echo '<div><input type="text" id="emergencyLevel'.$row["subpartID"].'" onchange="updateSubpartEmergency('.$row["subpartID"].',this.value)" name="emergencyLevel" data-slider-min="1" data-slider-max="10" data-slider-step="1" data-slider-value="'.$row["emergencyLevel"].'">';
        echo '<span id="emergencyLevelValue'.$row["subpartID"].'" style="color:white;">'.$row["emergencyLevel"].'</span></div>';
        echo '</div>';
        echo '</div>';
        echo '<input type="button" onclick="updateSubpart('.$row["subpartID"].', \''.$row["status"].'\')" class="btn btn-primary" id="registerBtn" style="margin-right:5px;" value="Güncelle"></input>';
        echo '<input type="button" onclick="deleteSubpart('.$row["subpartID"].')" class="btn btn-danger" id="registerBtn" style="margin-left:5px;" value="Kaldır"></input>';
        echo '</form>';

        

        $url = "https://afetkurtar.site/api/team/search.php";

        $body = '{
            "assignedSubpartID":'.$row["subpartID"].'
        }';
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array("Content-Type: application/json"));
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $body);
        $responseTeam = json_decode(curl_exec($ch),true);
        $teamID = -1;

        if(count($responseTeam['records']) != 0)
            $teamID = $responseTeam['records'][0]["teamID"];

        $url = "https://afetkurtar.site/api/personnelUser/search.php";

        $body = '{
            "teamID":'.$teamID.'
        }';
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array("Content-Type: application/json"));
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $body);
        $personnelUsers = json_decode(curl_exec($ch),true);

        echo "<table id=\"personnel-table\" class=\"table table-dark table-striped table-bordered\" style=\"text-align:center\">
            <thead>
            <tr>
            <th>Personel İsmi</th>
            <th>Personel Emaili</th>
            <th>Personel Rolü</th>
            <th>Detay</th>
            </tr>
            <thead>
            <tbody>";
        
        foreach ($personnelUsers['records'] as $personnel) {
            echo "<tr>";
            echo "<td class=\"td-element\">" . $personnel['personnelName'] . "</td>";
            echo "<td class=\"td-element\">" . $personnel['personnelEmail'] . "</td>";
            echo "<td class=\"td-element\">" . $personnel['personnelRole'] . "</td>";
            //echo "<td class=\"td-element\">" . $personnel['institution'] . "</td>";
            echo "<td class=\"td-element\"><input type=\"button\" value=\"Çıkar\" onclick=\"removePersonnel(" . $personnel['personnelID']. ")\" class=\"btn btn-danger\" id=\"" . $row['disasterID'] . "\"\></input></td>";
            echo "</tr>";
        }
        echo "<tbody>";
        echo "</table>";

        echo '</div>';
        echo '</div>';
        echo '</div>';
        

        /////
        echo '</div>';
    }
    /////
    echo '<div class="tab-pane fade" id="subpart-add" role="tabpanel" aria-labelledby="subpart-add-tab">';
    echo '<div class="container main-container" style="margin-left:0px; margin-top:0px;">';
    echo '<div class="row">';
    echo '<div class="col-lg-12 col-md-8"></div>';
    echo '<div class="col-lg-12 col-md-8 form-box">';
    echo '<form onsubmit="return false;" id="addSubpartForm">';
    echo '<div class="form-group">';
    echo '<label for="subpartName" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Bölge Adı</label>';
    echo '<input type="email" class="form-control" id="subpartName" placeholder="Bölge adını giriniz...">';
    echo '</div>';
    echo '<div class="form-group">';
    echo '<label for="subpartLatitude" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Enlem</label>';
    echo '<input type="text" class="form-control" id="subpartLatitude" placeholder="Enlemi giriniz...">';
    echo '</div>';
    echo '<div class="form-group">';
    echo '<label for="subpartLongitude" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Boylam</label>';
    echo '<input type="text" class="form-control" id="subpartLongitude" placeholder="Boylamı giriniz...">';
    echo '</div>';
    echo '<div class="form-group">';
    echo '<label for="subpartMissingPerson" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Kayıp İnsan Sayısı</label>';
    echo '<input type="text" class="form-control" id="subpartMissingPerson" placeholder="Tahmin edilen kayıp insan sayısını giriniz...">';
    echo '</div>';
    echo '<div class="form-group">';
    echo '<label for="subpartIsOpenForVolunteers" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Gönüllü Katılım</label>';
    echo '<div><input type="checkbox" id="subpartIsOpenForVolunteers"></div>';
    echo '</div>';
    echo '<div class="form-group">';
    echo '<label id="emergencyLabel" for="emergencyLevel" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Aciliyet Seviyesi</label>';
    echo '<input id="emergencyLevel" onchange="updateEmergency(this.value)" name="emergencyLevel" type="text" data-slider-min="1" data-slider-max="10" data-slider-step="1" data-slider-value="5">';
    echo '<span id="emergencyLevelValue" style="color:white;">5</span>';
    echo '</div>';
    echo '<input type="button" onclick="addSubpart()" class="btn btn-primary" id="registerBtn" value="Bölgeyi Ekle"></input>';
    echo '</form>';
    echo '</div>';
    echo '</div>';
    echo '</div>';
    echo '</div>';
    /////
    echo '</div>';

    echo '</ul>';

    echo '<script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>';
    echo '<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/js/bootstrap.bundle.min.js" integrity="sha384-b5kHyXgcpbZJO/tY9Ul7kGkf1S0CWuKcCD38l8YkeH8z8QjE0GmW1gYU5S9FOnJ0" crossorigin="anonymous"></script>';
    echo '<script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.24/js/jquery.dataTables.js"></script>';
    echo '<script src="js/script.js"></script>';
    echo '<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-slider/10.0.0/bootstrap-slider.min.js"></script>';
    echo '<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCxLUKYaDqQEIIQGQGQmC0ipdS04IXRoRw&callback=initMap&libraries=&v=weekly" async></script>';
    

    foreach($response['records'] as $row){
        echo '<script>';
        echo 'var slider = new Slider("#emergencyLevel'.$row["subpartID"].'", {';
        echo 'tooltip: "always"';
        echo '});';
        echo '</script>';
    }

    echo '<script>';
        echo 'var slider = new Slider("#emergencyLevel", {';
        echo 'tooltip: \'always\'';
        echo '});';
        echo '</script>';


    ?>


</body>
</html>