<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
 
// get database connection
include_once '../config/database.php';
 
// instantiate product object
include_once '../objects/disasterEvents.php';
 
$database = new Database();
$db = $database->getConnection();
 
$disasterEvents = new DisasterEvents($db);
 
// get posted data
$data = json_decode(file_get_contents("php://input"),true);
 
// make sure data is not empty
if(
    // isset($data["disasterType"]) &&
    // isset($data["emergencyLevel"]) &&
    // isset($data["latitudeStart"]) &&
    // isset($data["latitudeEnd"]) &&
    // isset($data["longitudeStart"]) &&
    // isset($data["longitudeEnd"]) &&
    // isset($data["disasterDate"]) &&
    // isset($data["disasterBase"])
    true
){
 
    // set user property values
    $disasterEvents->disasterID = isset($data["disasterID"]) ? $data["disasterID"] : "";
    $disasterEvents->disasterID = isset($data["disasterType"]) ? $data["disasterType"] : "";
    $disasterEvents->emergencyLevel = isset($data["emergencyLevel"]) ? $data["emergencyLevel"] : "";
    $disasterEvents->latitudeStart = isset($data["latitude"]) ? $data["latitude"] : "";
    $disasterEvents->longitudeStart = isset($data["longitude"]) ? $data["longitude"] : "";
    $disasterEvents->disasterDate = isset($data["disasterDate"]) ? $data["disasterDate"] : "";
    $disasterEvents->disasterBase = isset($data["disasterBase"]) ? $data["disasterBase"] : "";
    $disasterEvents->disasterName = isset($data["disasterName"]) ? $data["disasterName"] : "";
 
    // create the product
    if($disasterEvents->create()){
 
        // set response code - 201 created
        http_response_code(201);
 
        // tell the user
        echo json_encode(array("message" => "Disaster was created."));
    }
 
    // if unable to create the product, tell the user
    else{
 
        // set response code - 503 service unavailable
        http_response_code(503);
 
        // tell the user
        echo json_encode(array("message" => "Unable to create disaster."));
    }
}
 
// tell the user data is incomplete
else{
 
    // set response code - 400 bad request
    http_response_code(400);
 
    // tell the user
    echo json_encode(array("message" => "Unable to create disaster. Data is incomplete."));
}
?>