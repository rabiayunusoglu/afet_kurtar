<?php
// session_start();
// if (session_id() == '') {
//     header("location: https://afetkurtar.site/");
// } else if ($_SESSION["userType"] == "volunteerUser") {
//     header("location: https://afetkurtar.site/volunteerUser.php");
// } else if ($_SESSION["userType"] == "personnelUser") {
//     header("location: https://afetkurtar.site/personnelUser.php");
// } else if ($_SESSION["userType"] != "authorizedUser") {
//     header("location: https://afetkurtar.site/");
// }

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
 

$assignments = json_decode(file_get_contents("php://input"),true);


// $url = "https://afetkurtar.site/api/personnelUser/search.php";

// $body = '{
//     "teamID": "0"
// }';
// $ch = curl_init();
// curl_setopt($ch, CURLOPT_URL, $url);
// curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
// curl_setopt($ch, CURLOPT_HTTPHEADER, array("Content-Type: application/json"));
// curl_setopt($ch, CURLOPT_POST, 1);
// curl_setopt($ch, CURLOPT_POSTFIELDS, $body);
// $personnelUsers = json_decode(curl_exec($ch),true)['records'];

foreach ($assignments as $subpartID => $personnelList) {
    foreach ($personnelList as &$personnel) {
        $url = "https://afetkurtar.site/api/team/search.php";
        $body = '{
            "assignedSubpartID": '. $subpartID .'
        }';
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array("Content-Type: application/json"));
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $body);
        $response = json_decode(curl_exec($ch),true);

        if (array_key_exists("records",$response)){
            //takim var
            $url = "https://afetkurtar.site/api/personnelUser/update.php";
            $body = '{
                "teamID": '. $response["records"][0]["teamID"] .',
                "personnelRole": "Normal",
                "personnelID": '. $personnel["personnelID"] .'
            }';
            $ch = curl_init();
            curl_setopt($ch, CURLOPT_URL, $url);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
            curl_setopt($ch, CURLOPT_HTTPHEADER, array("Content-Type: application/json"));
            curl_setopt($ch, CURLOPT_POST, 1);
            curl_setopt($ch, CURLOPT_POSTFIELDS, $body);
            $response = json_decode(curl_exec($ch),true);

        }
        else{
            $url = "https://afetkurtar.site/api/team/create.php";
            $body = '{
                "assignedSubpartID": '. $subpartID .'
            }';
            $ch = curl_init();
            curl_setopt($ch, CURLOPT_URL, $url);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
            curl_setopt($ch, CURLOPT_HTTPHEADER, array("Content-Type: application/json"));
            curl_setopt($ch, CURLOPT_POST, 1);
            curl_setopt($ch, CURLOPT_POSTFIELDS, $body);
            $response = json_decode(curl_exec($ch),true);
            if(array_key_exists("id",$response)){
                $url = "https://afetkurtar.site/api/personnelUser/update.php";
                $body = '{
                    "teamID": '. $response["id"] .',
                    "personnelRole": "Normal",
                    "personnelID": '. $personnel["personnelID"] .'
                }';
                $ch = curl_init();
                curl_setopt($ch, CURLOPT_URL, $url);
                curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
                curl_setopt($ch, CURLOPT_HTTPHEADER, array("Content-Type: application/json"));
                curl_setopt($ch, CURLOPT_POST, 1);
                curl_setopt($ch, CURLOPT_POSTFIELDS, $body);
                $response = json_decode(curl_exec($ch),true);
            }
        }
    }
}

http_response_code(201);
 
// tell the user
echo json_encode(array("message" => "personnels were assigned."));

?>



