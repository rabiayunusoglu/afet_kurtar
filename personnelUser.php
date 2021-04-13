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
                <div id="logout"><a class="nav-link" href="/cikis.php">Çıkış Yap</a></div>
            </div>
        </nav>
    </header>

    <?php
    $teamID = 0;
    $subpartID = 0;
    $subpart;
    $teamMembers = array();
    $volunteerTeamMembers = array();
    $statuses = array();

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

    // if($teamID == 0){
    //     // afete atanmamis
    // }


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
    




    // foreach ($response['records'] as $row) {
    //     echo "<div name=\"subpart\" subpart-id=\"" . $row['subpartID']."\" subpart-name=\"" . $row['subpartName']."\" latitude=\"" . $row['latitude']."\" longitude=\"" . $row['longitude']."\"></div>";
    // }
    // style="background-color: #383B3E;"
    echo '<div style="display: flex;">';
        echo '<div id="map" style="width:50vw; height:93.97vh;" latitude='. $subpart["latitude"] .' longitude='. $subpart["longitude"] .'></div>';
        echo '<div style="width:50vw; height:93.97vh;">';
            echo '<ul class="nav nav-tabs nav-flex" id="myTab" role="tablist" style="height:5vh;">';
                echo '<li class="nav-item" role="presentation" style="width:33.3%; margin:0px;">';
                    echo '<button class="nav-link flex-sm-fill text-sm-center active" style="width:100%;" id="pills-status-tab" data-bs-toggle="pill" data-bs-target="#pills-status"  data-toggle="tab" role="tab" aria-controls="pills-status" aria-selected="false">Durum</button>';
                echo '</li>';
                echo '<li class="nav-item" role="presentation" style="width:33.3%; margin:0px;">';
                    echo '<button class="nav-link flex-sm-fill text-sm-center" style="width:100%;" id="pills-equipment-tab" data-bs-toggle="pill" data-bs-target="#pills-equipment" data-toggle="tab"  role="tab" aria-controls="pills-equipment" aria-selected="false">Ekipman</button>';
                echo '</li>';
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
                        echo'<section id="cd-timeline" class="cd-container" style="width:50vw;">';
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
                    echo '<button class="btn btn-dark btn-sm" onclick="addStatus(' . $teamID . ', ' . $subpartID . ')" id="btn-chat" style="height:5vh;">Gönder</button>';
                    echo '</span>';
                    echo '</div>';
                    echo '</div>';

                    //////////////
                echo '</div>';











                echo '<div class="tab-pane fade" id="pills-equipment" role="tabpanel" aria-labelledby="pills-equipment-tab">';
                    /////////////////////////////////////////////////////////////////////////
                    //ekipman ekrani icerigi


                    echo '<div>ekipman ekrani</div>';
                    
                

                    //////////////
                echo '</div>';











                echo '<div class="tab-pane fade" id="pills-message" role="tabpanel" aria-labelledby="pills-message-tab">';
                    ///////////////////////////////////////////////////////////////////////////
                    //mesaj ekrani icerigi
                    echo '<div class="span9" id="content" style="height: 81.97vh;">';
                    


                    echo '<div class="message first">
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
                    </div>';
                    
                    







                    echo '</div>';
                    echo '<div class="panel-footer" style="background: #1A2226;box-shadow: 0 3px 6px rgba(0, 0, 0, 0.16), 0 -3px 6px rgba(0, 0, 0, 0.23);">';
                    echo '<div class="input-group" style="height:7vh; padding:1vh;">';
                    echo '<input id="status-input" type="text" class="form-control input-sm chat_input" style="height:5vh;" placeholder="Mesajınızı yazın..." />';
                    echo '<span class="input-group-btn">';
                    echo '<button class="btn btn-dark btn-sm" id="btn-chat" style="height:5vh;">Gönder</button>';
                    echo '</span>';
                    echo '</div>';
                    echo '</div>';
                    //////////////
                echo '</div>';
            echo '</div>';

        echo '</div>';
    echo '</div>';


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