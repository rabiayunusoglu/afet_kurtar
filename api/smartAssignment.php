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

// {
//     "subpartIDs" : [3,5,8,32],
//     "numberOfPeople" : 8
// }

// unset($array["key"]);

// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
 

$data = json_decode(file_get_contents("php://input"),true);

$subpartIDs = array_values($data["subpartIDs"]);
$numberOfPeople = $data["numberOfPeople"];


$url = "https://afetkurtar.site/api/personnelUser/search.php";

$body = '{
    "teamID": "0"
}';
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_HTTPHEADER, array("Content-Type: application/json"));
curl_setopt($ch, CURLOPT_POST, 1);
curl_setopt($ch, CURLOPT_POSTFIELDS, $body);
$personnelUsers = json_decode(curl_exec($ch),true)['records'];

$subparts = array();
$sumOfEmergencyLevels = 0;

foreach($subpartIDs as &$subpartID){
    $url = "https://afetkurtar.site/api/subpart/search.php";

    $body = '{
        "subpartID": '.$subpartID.'
    }';
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_HTTPHEADER, array("Content-Type: application/json"));
    curl_setopt($ch, CURLOPT_POST, 1);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $body);
    $subpart = json_decode(curl_exec($ch),true)['records'][0];
    array_push($subparts,$subpart);
    $sumOfEmergencyLevels += $subpart["emergencyLevel"];
}

$arrivalTimes = array();
$distances = array();

foreach ($personnelUsers as &$personnelUser) {
    foreach($subparts as &$subpart){
        $url = 'https://maps.googleapis.com/maps/api/directions/json?'.
        'origin='. $personnelUser["latitude"] .','. $personnelUser["longitude"] .
        '&destination='. $subpart["latitude"] .','. $subpart["longitude"] .
        '&key=AIzaSyCxLUKYaDqQEIIQGQGQmC0ipdS04IXRoRw&language=tr';
        $json = @file_get_contents($url);
        $directionsData=json_decode($json);
        $status = $directionsData->status;


        if($status=="OK")
        { 
            if (!array_key_exists($subpart["subpartID"],$arrivalTimes)){
                $arrivalTimes[$subpart["subpartID"]] = array();
            }
            if (!array_key_exists($subpart["subpartID"],$distances)){
                $distances[$subpart["subpartID"]] = array();
            }
            $arrivalTimes[$subpart["subpartID"]][$personnelUser["personnelID"]] = round(($directionsData->routes[0]->legs[0]->duration->value)/60);
            $distances[$subpart["subpartID"]][$personnelUser["personnelID"]] = round(($directionsData->routes[0]->legs[0]->distance->value)/1000);

        }
        // else{
        //     echo '|'.$subpart["subpartID"]. ': '.$status;
        // }
    }
}

foreach ($arrivalTimes as $subpartID => $personnelArrivalTimes) {
    asort($arrivalTimes[$subpartID]);
}
// foreach ($distances as $subpartID => $personneldistances) {
//     asort($distances[$subpartID]);
// }


$assignCount = 0;

$assignedPeople = array();

if(count($personnelUsers) < $numberOfPeople){
    $numberOfPeople = count($personnelUsers);
}

while($assignCount < $numberOfPeople){
    foreach ($subparts as &$subpart) {
        if (!array_key_exists($subpart["subpartID"],$assignedPeople)){
            $assignedPeople[$subpart["subpartID"]] = array();
        }
        if(count($assignedPeople[$subpart["subpartID"]]) < round(($subpart["emergencyLevel"] * $numberOfPeople) / $sumOfEmergencyLevels)){
            if(!empty($arrivalTimes[$subpart["subpartID"]])){
                $assignedPerson = array();
                $key = key($arrivalTimes[$subpart["subpartID"]]);
                $value = reset($arrivalTimes[$subpart["subpartID"]]);
                $assignedPerson["personnelID"] = $key;
                $assignedPerson["arrivalTime"] = $value;
                $assignedPerson["distance"] = $distances[$subpart["subpartID"]][$key];
                array_push($assignedPeople[$subpart["subpartID"]], $assignedPerson);
                foreach ($subparts as &$subpart) {
                    if(array_key_exists($key,$arrivalTimes[$subpart["subpartID"]])){
                        unset($arrivalTimes[$subpart["subpartID"]][$key]);
                    }
                }
                $assignCount++;
            }
        }
    }
    $isEmpty = 1;
    $isFull = 1;
    foreach($subparts as &$subpart) {
        if(!empty($arrivalTimes[$subpart["subpartID"]])){
            $isEmpty = 0;
        }
        if(count($assignedPeople[$subpart["subpartID"]]) < round(($subpart["emergencyLevel"] * $numberOfPeople) / $sumOfEmergencyLevels)){
            $isFull = 0;
        }
    }
    if($isEmpty == 1 || $isFull == 1){
        break;
    }
}

echo json_encode($assignedPeople);

?>



