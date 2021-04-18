<?php
    session_start();
    if(session_id() == ''){
        header("location: https://afetkurtar.site/");
    }
    else if($_SESSION["userType"] == "volunteerUser"){
        header("location: https://afetkurtar.site/volunteerUser.php");
    }
    else if($_SESSION["userType"] == "authorizedUser"){
        header("location: https://afetkurtar.site/authorizedUser.php");
    }
    else if($_SESSION["userType"] != "personnelUser"){
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
    $personnel;
    $teamID = 0;
    $team;
    $subpartID = 0;
    $subpart;
    $teamMembers = array();
    $volunteerTeamMembers = array();
    $statuses = array();
    $messages = array();

    //The url you wish to send the POST request to
    $url = "https://afetkurtar.site/api/personnelUser/search.php";


    $body = '{
        "personnelID":'.$_SESSION["userID"].'
        }';
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array("Content-Type: application/json"));
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $body);
        $response = json_decode(curl_exec($ch),true);

    if (array_key_exists("records",$response)){
        $personnel = $response["records"][0];
        $teamID = $response["records"][0]["teamID"];
    }
    else{
        $url = "https://afetkurtar.site/api/volunteerUser/search.php";

        $body = '{
            "volunteerID":'.$_SESSION["userID"].'
            }';
            $ch = curl_init();
            curl_setopt($ch, CURLOPT_URL, $url);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
            curl_setopt($ch, CURLOPT_HTTPHEADER, array("Content-Type: application/json"));
            curl_setopt($ch, CURLOPT_POST, 1);
            curl_setopt($ch, CURLOPT_POSTFIELDS, $body);
            $response = json_decode(curl_exec($ch),true);
            if (array_key_exists("records",$response)){
                $teamID = $response["records"][0]["assignedTeamID"];
            }
    }

    if($teamID == 0){
        echo '<div class="container main-container" >';
        echo '<div class="row">';
        echo '<div class="col-lg-12 col-md-8"></div>';
        echo '<div class="col-lg-12 col-md-8 form-box">';
        echo '<form onsubmit="return false;" id="no-team-form">';
        echo '<label for="teamNeedManPower" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Henüz Bir Arama Kurtarma Çalışmasına Atanmadınız</label>';
        echo '<div id="teamNeedManPower" style="font-size: 16px; margin-bottom:20px; color: #ECF0F5;">Bir takıma atandığınızda bildirim ile bilgilendirileceksiniz.</div>';
        echo '</form>';
        echo '</div>';
        echo '</div>';
        echo '</div>';  
    }
    else{


    //The url you wish to send the POST request to
    $url = "https://afetkurtar.site/api/team/search.php";

    $body = '{
        "teamID":'.$teamID.'
      }';
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_HTTPHEADER, array("Content-Type: application/json"));
    curl_setopt($ch, CURLOPT_POST, 1);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $body);
    $response = json_decode(curl_exec($ch),true);
    if (array_key_exists("records",$response)){
        $team = $response["records"][0];
        $subpartID = $response["records"][0]["assignedSubpartID"];

        $url = "https://afetkurtar.site/api/subpart/search.php";

        $body = '{
            "subpartID":'.$subpartID.'
        }';
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array("Content-Type: application/json"));
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $body);
        $response = json_decode(curl_exec($ch),true);
        if (array_key_exists("records",$response)){
            $subpart = $response["records"][0];
        }
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
    $response = json_decode(curl_exec($ch),true);
    if (array_key_exists("records",$response)){
        $teamMembers = $response["records"];
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
    $response = json_decode(curl_exec($ch),true);
    if (array_key_exists("records",$response)){
        $volunteerTeamMembers = $response["records"];
    }

    $url = "https://afetkurtar.site/api/status/search.php";

    $body = '{
        "subpartID":'.$subpartID.'
      }';
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_HTTPHEADER, array("Content-Type: application/json"));
    curl_setopt($ch, CURLOPT_POST, 1);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $body);
    $response = json_decode(curl_exec($ch),true);
    if (array_key_exists("records",$response)){
        $statuses = $response["records"];
    }

    $url = "https://afetkurtar.site/api/message/search.php";

    $body = '{
        "teamID":'.$teamID.'
      }';
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_HTTPHEADER, array("Content-Type: application/json"));
    curl_setopt($ch, CURLOPT_POST, 1);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $body);
    $response = json_decode(curl_exec($ch),true);
    if (array_key_exists("records",$response)){
        $messages = $response["records"];
    }

    $latitude = 0.0;
    $longitude = 0.0;

    if (array_key_exists("latitude",$personnel)){
        $latitude = $personnel["latitude"];
    }

    if (array_key_exists("longitude",$personnel)){
        $longitude = $personnel["longitude"];
    }

    /*foreach($teamMembers as $member){
        echo '<div id='. $member['personnelID'] .' user-name="'. $member['personnelName'] .'"></div>';
    }

    foreach($volunteerTeamMembers as $member){
        echo '<div id='. $member['volunteerID'] .' user-name="'. $member['volunteerName'] .'"></div>';
    }*/



    // foreach ($response['records'] as $row) {
    //     echo "<div name=\"subpart\" subpart-id=\"" . $row['subpartID']."\" subpart-name=\"" . $row['subpartName']."\" latitude=\"" . $row['latitude']."\" longitude=\"" . $row['longitude']."\"></div>";
    // }
    // style="background-color: #383B3E;"
    echo '<div style="display: flex;">';
        echo '<div id="map" style="width:50vw; height:93.97vh;" latitude='. $latitude .' longitude='. $longitude .' team-id='. $teamID .' user-id='. $personnel["personnelID"] .'></div>';
        echo '<div style="width:50vw; height:93.97vh;">';
            echo '<ul class="nav nav-tabs nav-flex" id="myTab" role="tablist" style="height:5vh;">';
                echo '<li class="nav-item" role="presentation" style="width:33.3%; margin:0px;">';
                    echo '<button class="nav-link flex-sm-fill text-sm-center active" style="width:100%;" id="pills-status-tab" data-bs-toggle="pill" data-bs-target="#pills-status"  data-toggle="tab" role="tab" aria-controls="pills-status" aria-selected="false">Durum</button>';
                echo '</li>';
                if($personnel["personnelRole"] == "Kaptan"){
                    echo '<li class="nav-item" role="presentation" style="width:33.3%; margin:0px;">';
                        echo '<button class="nav-link flex-sm-fill text-sm-center" style="width:100%;" id="pills-equipment-tab" data-bs-toggle="pill" data-bs-target="#pills-equipment" data-toggle="tab"  role="tab" aria-controls="pills-equipment" aria-selected="false">Takım Yönetimi</button>';
                    echo '</li>';
                }
                echo '<li class="nav-item" role="presentation" style="width:33.3%; margin:0px;">';
                    echo '<button class="nav-link flex-sm-fill text-sm-center" style="width:100%;" id="pills-message-tab" data-bs-toggle="pill" data-bs-target="#pills-message" data-toggle="tab"  role="tab" aria-controls="pills-message" aria-selected="false">Mesajlaşma</button>';
                echo '</li>';
            echo '</ul>';
        

            echo '<div class="tab-content" id="pills-tabContent">';
                echo '<div class="tab-pane fade show active" id="pills-status" role="tabpanel" aria-labelledby="pills-status-tab">';
                    /////////////////////////////////////////////////////////////////
                    //durum ekrani icerigi

                    echo '<div class="span9 status-content" id="content" team-id="'.$teamID.'" status-length="'.count($statuses).'" style="height: 81.97vh;">';

                    if (!empty($statuses)) {
                        echo'<section id="cd-timeline" class="cd-container" style="width:48vw;">';
                    }

                    foreach($statuses as $rowStatus)
                    {
                        echo '<div class="cd-timeline-block">';
                        echo '<div class="cd-timeline-img cd-picture">';
                        echo '</div>';
                        echo '<div class="cd-timeline-content">';
                        echo '<h2>'. $rowStatus['statusTime'] .'</h2>';
                        echo '<div class="timeline-content-info">';
                        echo '<span class="timeline-content-info-date">';
                        echo '<i class="fa fa-calendar-o" aria-hidden="true"></i>';
                        echo 'Takım '. $rowStatus['teamID'];
                        echo '</span>';
                        echo '</div>';
                        echo '<p>'. $rowStatus['statusMessage'] .'</p>';
                        echo '</div> <!-- cd-timeline-content -->';
                        echo '</div> <!-- cd-timeline-block -->';
                    }

                    if (!empty($statuses)) {
                        echo '</section> <!-- cd-timeline -->';
                    }

                    echo '</div>';

                    echo '<div class="panel-footer" style="background: #1A2226;box-shadow: 0 3px 6px rgba(0, 0, 0, 0.16), 0 -3px 6px rgba(0, 0, 0, 0.23);">';
                    echo '<div class="input-group" style="height:7vh; padding:1vh;">';
                    echo '<input id="status-input" type="text" class="form-control input-sm chat_input" style="height:5vh;" placeholder="Yeni durum oluşturun..." />';
                    echo '<span class="input-group-btn">';
                    echo '<button class="btn btn-dark btn-sm" onclick="addStatus(' . $teamID . ', ' . $subpartID . ')" id="btn-status" style="height:5vh;">Gönder</button>';
                    echo '</span>';
                    echo '</div>';
                    echo '</div>';

                    //////////////
                echo '</div>';










                if($personnel["personnelRole"] == "Kaptan"){
                echo '<div class="tab-pane fade" id="pills-equipment" role="tabpanel" aria-labelledby="pills-equipment-tab">';
                    /////////////////////////////////////////////////////////////////////////
                    //ekipman ekrani icerigi
                    echo '<div class="span9 team-management-content" id="content" team-id="'.$teamID.'" style="height: 88.97vh;">';


                    echo '<div class="container main-container" style="margin-left:5vw; margin-top:0px; width:40vw;">';
                    echo '<div class="row">';
                    echo '<div class="col-lg-12 col-md-8"></div>';
                    echo '<div class="col-lg-12 col-md-8 form-box" style="margin-top:30px;">';
                    echo '<form onsubmit="return false;" id="smart-assignment-form">';


                        
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


                        echo '<div class="form-group">';
                        echo '<label for="equipment-table" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Ekipman</label>';

                        echo '<select name="equipmentTable" class="form-control" id="equipment-table">';
                        foreach ($equipments as &$equipment) {
                            echo '<option value="'. $equipment["equipmentID"] .'">'. $equipment["equipmentName"] .'</option>';
                        }
                        
                        echo '</select>';
                        echo '</div>';



                        echo '<label for="equipment-request-quantity" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Miktar</label>';
                        echo '<input type="text" class="form-control" id="equipment-request-quantity" placeholder="Miktar giriniz...">';


                        echo '<input type="button" onclick="addEquipmentRequest('. $teamID .')" class="btn btn-success" id="registerBtn" value="Ekipman İsteği Gönder"></input>';

                    echo '</form>';
                    echo '</div>';
                    echo '</div>';
                    echo '</div>';    
                    


                    echo '<div class="container main-container" style="margin-left:5vw; margin-top:0px; width:40vw;">';
                    echo '<div class="row">';
                    echo '<div class="col-lg-12 col-md-8"></div>';
                    echo '<div class="col-lg-12 col-md-8 form-box" style="margin-top:30px;">';
                    echo '<form onsubmit="return false;" id="team-management-form">';




                        echo '<div class="form-group">';

                        echo '<label for="manpower-request-quantity" style="font-size: 20px; font-weight:bold; color: #ECF0F5">İnsan İş Gücü İhtiyacı</label>';
                        echo '<input type="text" class="form-control" id="manpower-request-quantity" value="'. $team["needManPower"] .'">';

                        echo '<div class="form-group row">';
                        echo '<div class="col-lg-6 col-md-4">';
                        echo '<label for="missing-person" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Kayıp İnsan Sayısı</label>';
                        echo '<input type="text" class="form-control" id="missing-person" value="'.$subpart["missingPerson"].'">';
                        echo '</div>';
                        echo '<div class="col-lg-6 col-md-4">';
                        echo '<label for="rescued-person" style="font-size: 20px; font-weight:bold; color: #ECF0F5">Kurtarılan İnsan Sayısı</label>';
                        echo '<input type="text" class="form-control" id="rescued-person" value="'.$subpart["rescuedPerson"].'">';
                        echo '</div>';
                        echo '</div>';

                        echo '<input type="button" onclick="editTeamManagement('. $teamID .','. $subpartID .')" class="btn btn-primary" id="registerBtn" value="Güncelle"></input>';

                    echo '</form>';
                    echo '</div>';
                    echo '</div>';
                    echo '</div>';    
                
                    echo '</div>';
                    //////////////
                    echo '</div>';
                echo '</div>';
                }











                echo '<div class="tab-pane fade" id="pills-message" role="tabpanel" aria-labelledby="pills-message-tab">';
                    ///////////////////////////////////////////////////////////////////////////
                    //mesaj ekrani icerigi
                    echo '<div class="span9 message-content" id="content" team-id="'.$teamID.'" user-id="'.$_SESSION["userID"].'" message-length="'.count($messages).'" style="height: 81.97vh;">';
                    
                    /*if (!empty($messages)) {
                        echo'<section id="cd-timeline" class="cd-container" style="width:50vw;">';
                    }*/

                    array_multisort($messageID, SORT_ASC, $messages);

                    foreach($messages as $message){
                        $messageTime = date_create($message["messageTime"]);

                        if($message["userID"] == $_SESSION["userID"]){
                            echo '<div class="message first">
                            '. $message['messageData'] .'
                            <sub>'. date_format($messageTime, 'H:i') .'</sub>
                            </div>';
                        }

                        else{
                            echo '<div class="message">
                            <b>' . $message['messageName'] . '</b><br>
                            '. $message['messageData'] .'
                            <sub>'. date_format($messageTime, 'H:i') .'</sub>
                            </div>';
                        }
                    }

                    /*echo '<div class="message first">
                    Uh, what is this guys problem, Mr. Stark? 🤔
                    <sub>07.32</sub>
                    </div>';
                    echo '<div class="message">
                    <b>Tarik</b><br>
                    Uh, what is this guys problem, Mr. Stark? 🤔
                    <sub>07.34</sub>
                    </div>';
                    echo '<div class="message first">
                    Uh, what is this guys problem, Mr. Stark? 🤔
                    <sub>07.36</sub>
                    </div>';*/

                    echo '</div>';
                    echo '<div class="panel-footer" style="background: #1A2226;box-shadow: 0 3px 6px rgba(0, 0, 0, 0.16), 0 -3px 6px rgba(0, 0, 0, 0.23);">';
                    echo '<div class="input-group" style="height:7vh; padding:1vh;">';
                    echo '<input id="message-input" type="text" class="form-control input-sm chat_input" style="height:5vh;" placeholder="Mesajınızı yazın..." />';
                    echo '<span class="input-group-btn">';
                    echo '<button class="btn btn-dark btn-sm" id="btn-chat" onclick="addMessage(' . $teamID . ', ' . $_SESSION["userID"] . ', \'' . $_SESSION["userName"] . '\')" style="height:5vh;">Gönder</button>';
                    echo '</span>';
                    echo '</div>';
                    echo '</div>';
                    //////////////
                echo '</div>';
            echo '</div>';

        echo '</div>';
    echo '</div>';

    }
    ?>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/js/bootstrap.bundle.min.js" integrity="sha384-b5kHyXgcpbZJO/tY9Ul7kGkf1S0CWuKcCD38l8YkeH8z8QjE0GmW1gYU5S9FOnJ0" crossorigin="anonymous"></script>
    <script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.24/js/jquery.dataTables.js"></script>
    <script src="js/script.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-slider/10.0.0/bootstrap-slider.min.js"></script>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCxLUKYaDqQEIIQGQGQmC0ipdS04IXRoRw&callback=initMapForPersonnel&libraries=&v=weekly" async></script>
    <script src="https://gitcdn.github.io/bootstrap-toggle/2.2.2/js/bootstrap-toggle.min.js"></script>

</body>
</html>