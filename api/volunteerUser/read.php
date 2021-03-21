<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
 
// database connection will be here

// include database and object files
include_once '../config/database.php';
include_once '../objects/volunteerUser.php';
 
// instantiate database and table object
$database = new Database();
$db = $database->getConnection();
 
// initialize object
$volunteerUser = new VolunteerUser($db);

$stmt = $volunteerUser->read();
$num = $stmt->rowCount();
 

if($num>0){
 

    $volunteerUser_arr=array();
    $volunteerUser_arr["records"]=array();
 
 
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);
 
        $volunteerUser_item=array(
            "volunteerID" => $volunteerID,
            "volunteerName" => $volunteerName,
            "address" => $address,
            "isExperienced" => $isExperienced,
            "haveFirstAidCert" => $haveFirstAidCert,
            "requestedSubpart" => $requestedSubpart,
            "responseSubpart" => $responseSubpart,
            "assignedTeamID" => $assignedTeamID,
            "role" => $role,
            "latitude" => $latitude,
            "longitude" => $longitude,
            "locationTime" => $locationTime,
            "tc" => $tc,
"tel" => $tel,
            "birthDate" => $birthDate,
        );
 
        array_push($volunteerUser_arr["records"], $volunteerUser_item);
    }
 
    // set response code - 200 OK
    http_response_code(200);
 
    echo json_encode($volunteerUser_arr);
}

else{
 
    // set response code - 404 Not found
    http_response_code(404);
 
    // tell the user no items found
    echo json_encode(
        array("message" => "No items found.")
    );
}

?>