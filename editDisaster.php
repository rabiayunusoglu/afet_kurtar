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
    <link href="https://gitcdn.github.io/bootstrap-toggle/2.2.2/css/bootstrap-toggle.min.css" rel="stylesheet">
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

    <?php
    $disasterID = 0;
    $latitude = 0.0;
    $longitude = 0.0;
    $disasterType = "";
    $disasterBase = "";
    $disasterName = "";
    $subparts = array();

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

        foreach ($response['records'] as &$row) {
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
    $subparts = $response["records"];

    foreach ($response['records'] as &$row) {
        echo "<div name=\"subpart\" subpart-id=\"" . $row['subpartID']."\" subpart-name=\"" . $row['subpartName']."\" latitude=\"" . $row['latitude']."\" longitude=\"" . $row['longitude']."\"></div>";
    }

    echo '<ul class="nav nav-tabs">';

    echo "<div id=\"map\" style=\"width:50vw;height:93.97vh\" disaster-id=\"" . $disasterID . "\" disaster-type=\"" . $disasterType . "\" disaster-base=\"" . $disasterBase . "\" disaster-name=\"" . $disasterName . "\" latitude=\"" . $latitude . "\" longitude=\"" . $longitude . "\"></div>";

    echo '<div class="container-fluid-edit-disaster" style="background-color: #383B3E; width:11vw;">';
    echo '<div class="row-fluid-edit-disaster">';
    echo '<div class="span9" id="content">';
    echo '<div class="header-edit-disaster" style="padding-left:10px; padding-top:10px; padding-bottom:5px; padding-right:10px;">Bölge</div>';

    echo '<div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">';
    foreach($response['records'] as &$row){
        echo '<a class="nav-link" id="subpart-' . $row["subpartID"] . '-tab" subpart-id="'. $row["subpartID"] .'" data-toggle="tab" href="#subpart-' . $row["subpartID"] . '" role="tab" aria-controls="subpart-' . $row["subpartID"] . '" aria-selected="false">' . $row["subpartName"] . '</a>';
    }
    echo '<a class="nav-link" id="subpart-add-tab" data-toggle="tab" href="#subpart-add" role="tab" aria-controls="subpart-add" aria-selected="false"> <i class="fa fa-plus  fa-1x"></i>  Ekle</a>';
    echo '<a class="nav-link" id="smart-assignment-tab" data-toggle="tab" href="#smart-assignment" role="tab" aria-controls="smart-assignment" aria-selected="false"> <i class="fa fa-users fa-1x" aria-hidden="true"></i>  Akıllı Atama </a>';
    echo '</div>';

    echo '</div>';
    echo '</div>';
    echo '</div>';

    echo '<div class="vl"></div>';

    echo '<div class="tab-content" id="v-pills-tabContent" >';
    foreach($response['records'] as &$row){
        

        echo '<div class="tab-pane fade" id="subpart-' . $row["subpartID"] . '" role="tabpanel" aria-labelledby="subpart-' . $row["subpartID"] . '-tab">';

        echo '<div style="height: 93.97vh;">';
        echo '<div class="container-fluid-edit-disaster">';
        echo '<div class="row-fluid-edit-disaster">';
        echo '<div class="span9" id="content">';
        /////
        $checkedString = "";
        if($row["isOpenForVolunteers"] == 1){
            $checkedString = " checked";
        }
        

        echo '<div class="container main-container" style="margin-left:1vw; margin-top:0px; width:37vw;">';
        echo '<div class="row">';
        echo '<div class="col-lg-12 col-md-8"></div>';
        echo '<div class="col-lg-12 col-md-8 form-box" style="margin-top:30px;">';
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
        //echo '<div><input type="checkbox" data-toggle="toggle" data-on="Açık" data-off="Kapalı" data-onstyle="success" data-offstyle="danger" id="subpartIsOpenForVolunteers'.$row["subpartID"].'" '.$checkedString.'></div>';
        echo '</div>';
        //echo '</div>';
        //echo '<div class="form-group">';
        echo '<div class="col-lg-6 col-md-4">';
        echo '<label for="emergencyLevel'.$row["subpartID"].'" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Aciliyet Seviyesi</label>';
        echo '<div><input type="text" id="emergencyLevel'.$row["subpartID"].'" onchange="updateSubpartEmergency('.$row["subpartID"].',this.value)" name="emergencyLevel" data-slider-min="1" data-slider-max="10" data-slider-step="1" data-slider-value="'.$row["emergencyLevel"].'">';
        echo '<span id="emergencyLevelValue'.$row["subpartID"].'" style="color:white; margin-left:3px;">'.$row["emergencyLevel"].'</span></div>';
        echo '</div>';
        echo '</div>';
        echo '<input type="button" onclick="updateSubpart('.$row["subpartID"].', \''.$row["status"].'\')" class="btn btn-primary" id="registerBtn" style="margin-right:0px;" value="Güncelle"></input>';
        echo '<input type="button" onclick="deleteSubpart('.$row["subpartID"].')" class="btn btn-danger" id="registerBtn" style="margin-left:10px;" value="Kaldır"></input>';
        echo '<input type="button" onclick="kisiEkle('.$row["subpartID"].','.$_GET["id"].')" class="btn btn-success" id="registerBtn" style="margin-left:10px;" value="Kişi Ekle"></input>';
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
        $team;

        if((array_key_exists("records",$responseTeam))){
            $team = $responseTeam['records'][0];
            $teamID = $responseTeam['records'][0]["teamID"];
        }
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


        if(array_key_exists("records",$personnelUsers)){
            echo "<table id=\"personnel-table\" class=\"table table-dark table-striped table-bordered\" style=\"text-align:center\">
                <thead>
                <tr>
                <th>Personel İsmi</th>
                <th>Personel Emaili</th>
                <th>Personel Rolü</th>
                <th>İşlem</th>
                </tr>
                <thead>
                <tbody>";
        }

        foreach ($personnelUsers['records'] as &$personnel) {
            echo "<tr>";
            echo "<td class=\"td-element\">" . $personnel['personnelName'] . "</td>";
            echo "<td class=\"td-element\">" . $personnel['personnelEmail'] . "</td>";
            echo "<td class=\"td-element\">" . $personnel['personnelRole'] . "</td>";
            //echo "<td class=\"td-element\">" . $personnel['institution'] . "</td>";
            echo "<td class=\"td-element\"><input type=\"button\" value=\"Çıkar\" onclick=\"removePersonnelFromTeam(" . $personnel['personnelID']. ")\" class=\"btn btn-danger\" ></input></td>";
            echo "</tr>";
        }
        if((array_key_exists("records",$personnelUsers))){
            echo "<tbody>";
            echo "</table>";
        }




        $url = "https://afetkurtar.site/api/volunteerUser/search.php";

        $body = '{
            "assignedTeamID":'.$teamID.'
        }';
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array("Content-Type: application/json"));
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $body);
        $volunteerUsers = json_decode(curl_exec($ch),true);


        if((array_key_exists("records",$volunteerUsers))){
            echo "<table id=\"personnel-table\" class=\"table table-dark table-striped table-bordered\" style=\"text-align:center\">
                <thead>
                <tr>
                <th>Gönüllü İsmi</th>
                <th>Telefon Numarası</th>
                <th>Gönüllü Rolü</th>
                <th>İşlem</th>
                </tr>
                <thead>
                <tbody>";
        }

        foreach ($volunteerUsers['records'] as &$volunteer) {
            echo "<tr>";
            echo "<td class=\"td-element\">" . $volunteer['volunteerName'] . "</td>";
            echo "<td class=\"td-element\">" . $volunteer['tel'] . "</td>";
            echo "<td class=\"td-element\">" . $volunteer['role'] . "</td>";
            //echo "<td class=\"td-element\">" . $personnel['institution'] . "</td>";
            echo "<td class=\"td-element\"><input type=\"button\" value=\"Çıkar\" onclick=\"removeVolunteerFromTeam(" . $volunteer['volunteerID']. ")\" class=\"btn btn-danger\" ></input></td>";
            echo "</tr>";
        }
        if((array_key_exists("records",$volunteerUsers))){
            echo "<tbody>";
            echo "</table>";
        }

        $urlEquipments = "http://afetkurtar.site/api/equipment/read.php";

        $optionsEquipments = ['http' => [
            'method' => 'POST',
            'header' => 'Content-type:application/json'
            // 'content' => $json
        ]];

        $contextEquipments = stream_context_create($optionsEquipments);
        $responseEquipments = json_decode(file_get_contents($urlEquipments, false, $contextEquipments), true);
        $equipments;
        if((array_key_exists("records",$responseEquipments))){
            $equipments= $responseEquipments["records"];
        }
        



        $url = "https://afetkurtar.site/api/equipmentRequest/search.php";

        $body = '{
            "teamRequestID":'.$teamID.'
        }';
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array("Content-Type: application/json"));
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $body);
        $equipmentRequests = json_decode(curl_exec($ch),true);


        if((array_key_exists("records",$equipmentRequests))){
            echo "<table id=\"personnel-table\" class=\"table table-dark table-striped table-bordered\" style=\"text-align:center\">
                <thead>
                <tr>
                <th>Ekipman Adı</th>
                <th>Miktar</th>
                <th>İşlem</th>
                </tr>
                <thead>
                <tbody>";
        }
        foreach ($equipmentRequests['records'] as &$equipmentRequest) {
            $equipmentName = "";
            foreach ($equipments as &$equipment) {
                if($equipment["equipmentID"] == $equipmentRequest["equipmentID"]){
                    $equipmentName = $equipment["equipmentName"];
                }
            }
            echo "<tr>";
            echo "<td class=\"td-element\">" . $equipmentName. "</td>";
            echo "<td class=\"td-element\">" . $equipmentRequest["quantity"] . "</td>";
            echo "<td class=\"td-element\"><input type=\"button\" value=\"Sil\" onclick=\"removeEquipmentRequest(" . $equipmentRequest['equipmentRequestID']. ")\" class=\"btn btn-danger\" ></input></td>";
            echo "</tr>";
        }
        if((array_key_exists("records",$equipmentRequests))){
            echo "<tbody>";
            echo "</table>";
        }

        if(!is_null($team)){
            if($team["needManPower"] > 0){
                echo '<label for="teamNeedManPower" style="font-size: 20px; font-weight:bold; color: #ECF0F5">İnsan İş Gücü İhtiyacı</label>';
                echo '<div id="teamNeedManPower" style="font-size: 16px; margin-bottom:20px; color: #ECF0F5;">'. $team["needManPower"] .'</div>';
            }
        }




        echo '</div>';
        echo '</div>';
        echo '</div>';
        

        /////
        echo '</div>';

        echo '</div>';
        echo '</div>';
        echo '</div>';
        echo '</div>';
    }
    /////
    echo '<div class="tab-pane fade" id="subpart-add" role="tabpanel" aria-labelledby="subpart-add-tab">';
    echo '<div class="container main-container" style="margin-left:1vw; margin-top:0px; width:37vw;">';
    echo '<div class="row">';
    echo '<div class="col-lg-12 col-md-8"></div>';
    echo '<div class="col-lg-12 col-md-8 form-box" style="margin-top:30px;">';
    echo '<form onsubmit="return false;" id="addSubpartForm">';
    echo '<div class="form-group">';
    echo '<label for="subpartName" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Bölge Adı</label>';
    echo '<input type="email" class="form-control" id="subpartName" placeholder="Bölge adını giriniz...">';
    echo '</div>';
    echo '<div class="form-group row">';
    echo '<div class="col-lg-6 col-md-4">';
    echo '<label for="subpartLatitude" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Enlem</label>';
    echo '<input type="text" class="form-control" id="subpartLatitude" placeholder="Enlemi giriniz...">';
    echo '</div>';
    echo '<div class="col-lg-6 col-md-4">';
    echo '<label for="subpartLongitude" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Boylam</label>';
    echo '<input type="text" class="form-control" id="subpartLongitude" placeholder="Boylamı giriniz...">';
    echo '</div>';
    echo '</div>';
    echo '<div class="form-group">';
    echo '<label for="subpartMissingPerson" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Kayıp İnsan Sayısı</label>';
    echo '<input type="text" class="form-control" id="subpartMissingPerson" placeholder="Tahmin edilen kayıp insan sayısını giriniz...">';
    echo '</div>';
    echo '<div class="form-group row">';
    echo '<div class="col-lg-6 col-md-4">';
    echo '<label for="subpartIsOpenForVolunteers" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Gönüllü Katılım</label>';
    echo '<div><input type="checkbox" id="subpartIsOpenForVolunteers"></div>';
    //echo '<div><input type="checkbox" data-toggle="toggle" id="subpartIsOpenForVolunteers" data-on="Açık" data-off="Kapalı" data-onstyle="success" data-offstyle="danger"></div>';
    echo '</div>';
    echo '<div class="col-lg-6 col-md-4">';
    echo '<label id="emergencyLabel" for="emergencyLevel" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Aciliyet Seviyesi</label>';
    echo '<input id="emergencyLevel" onchange="updateEmergency(this.value)" name="emergencyLevel" type="text" data-slider-min="1" data-slider-max="10" data-slider-step="1" data-slider-value="5">';
    echo '<span id="emergencyLevelValue" style="color:white; margin-left:3px;">5</span>';
    echo '</div>';
    echo '</div>';
    echo '<input type="button" onclick="addSubpart()" class="btn btn-primary" id="registerBtn" value="Bölgeyi Ekle"></input>';
    echo '</form>';
    echo '</div>';
    echo '</div>';
    echo '</div>';
    echo '</div>';
    //////////////////////////////////////////
    echo '<div class="tab-pane fade" id="smart-assignment" role="tabpanel" aria-labelledby="smart-assignment-tab">';
    echo '<div class="container main-container" style="margin-left:1vw; margin-top:0px; width:37vw;">';
    echo '<div class="row">';
    echo '<div class="col-lg-12 col-md-8"></div>';
    echo '<div class="col-lg-12 col-md-8 form-box" style="margin-top:30px;">';
    echo '<form onsubmit="return false;" id="smart-assignment-form">';

    echo '<label for="assignment-table" style="font-size: 24px; font-weight:bold; color: #ECF0F5">Akıllı Atama</label>';

        if(count($subparts) != 0){
            echo '<table id="assignment-table" class="table table-dark table-striped table-bordered" style="text-align:center">
                <tbody>';
        }

        foreach ($subparts as &$subpart) {
            echo "<tr>";
            echo '<td class="td-element" style="width: 80%">' . $subpart["subpartName"] . '</td>';
            echo '<td class="td-element style="width: 20%""> <input type="checkbox" class="subpart-toggle" data-toggle="toggle" data-onstyle="success" data-offstyle="danger" subpart-id="'.$subpart["subpartID"].'" subpart-name="'.$subpart["subpartName"].'" id="subpartAssignment'.$subpart["subpartID"].'"> </td>';
            //echo '<td class="td-element style="width: 20%""> <label class="switch"> <input type="checkbox"   id="subpartAssignment'.$row["subpartID"].'"> <span class="slider round"></span> </label> </td>';
            echo "</tr>";
        }

        if(count($subparts) != 0){
            echo "<tbody>";
            echo '<thead>
                <tr>
                <th>Bölge Adı</th>
                <th>İşlem</th>
                </tr>
                <thead>';
            echo "</table>";
        }


        echo '<label for="smart-assignment-people" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Kişi Sayısı</label>';
        echo '<input type="text" class="form-control" id="smart-assignment-people" placeholder="Atanacak maksimum kişi sayısını giriniz...">';


        echo '<input type="button" onclick="doSmartAssignment()" class="btn btn-success" id="registerBtn" style="margin-left:10px;" value="Akıllı Atama Gerçekleştir"></input>';

    echo '</form>';
    echo '</div>';
    echo '</div>';
    echo '</div>';    

    echo '</div>';




    //////////////////////////////////////////
    echo '</div>';

    echo '</ul>';

    echo '<script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>';
    echo '<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/js/bootstrap.bundle.min.js" integrity="sha384-b5kHyXgcpbZJO/tY9Ul7kGkf1S0CWuKcCD38l8YkeH8z8QjE0GmW1gYU5S9FOnJ0" crossorigin="anonymous"></script>';
    echo '<script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.24/js/jquery.dataTables.js"></script>';
    echo '<script src="js/script.js"></script>';
    echo '<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-slider/10.0.0/bootstrap-slider.min.js"></script>';
    echo '<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCxLUKYaDqQEIIQGQGQmC0ipdS04IXRoRw&callback=initMap&libraries=&v=weekly" async></script>';
    echo '<script src="https://gitcdn.github.io/bootstrap-toggle/2.2.2/js/bootstrap-toggle.min.js"></script>';

    foreach($response['records'] as &$row){
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

    echo '<script>';
    echo '$(".subpart-toggle").bootstrapToggle({';
    echo 'on: \'Açık\',';
    echo 'off: \'Kapalı\',';
    echo 'width: \'100%\'';
    echo '});';
    echo '</script>';

    // echo '<script>';
    // echo '$(function() {';
    // echo '$("#subpartIsOpenForVolunteers").bootstrapToggle({';
    // echo 'on: \'Açık\',';
    // echo 'off: \'Kapalı\'';
    // echo '});';
    // echo '})';
    // echo '</script>';

    ?>

    <div class="modal"><!-- Place at bottom of page --></div>

</body>
</html>

