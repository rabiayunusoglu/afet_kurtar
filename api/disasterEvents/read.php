<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
 
// database connection will be here

// include database and object files
include_once '../config/database.php';
include_once '../objects/disasterEvents.php';
 
// instantiate database and table object
$database = new Database();
$db = $database->getConnection();
 
// initialize object
$disasterEvents = new DisasterEvents($db);


$stmt = $disasterEvents->read();
$num = $stmt->rowCount();
 

if($num>0){
 

    $disasterEvents_arr=array();
    $disasterEvents_arr["records"]=array();
 
 
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);
 
        $disasterEvents_item=array(
            "disasterID" => $disasterID,
            "disasterType" => $disasterType,
            "emergencyLevel" => $emergencyLevel,
            "latitude" => $latitude,
            "longitude" => $longitude,
            "disasterDate" => $disasterDate,
            "disasterBase" => $disasterBase,
            "disasterName" => $disasterName,
        );
 
        array_push($disasterEvents_arr["records"], $disasterEvents_item);
    }
 
    // set response code - 200 OK
    http_response_code(200);
 
    echo json_encode($disasterEvents_arr);
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