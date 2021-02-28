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
    !empty($data->disasterType) &&
    !empty($data->emergencyLevel) &&
    !empty($data->latitudeStart) &&
    !empty($data->latitudeEnd) &&
    !empty($data->longitudeStart) &&
    !empty($data->longitudeEnd) &&
    !empty($data->disasterDate) &&
    !empty($data->disasterBase)
){
 
    // set user property values
    $disasterEvents->disasterType = $data->disasterType;
    $disasterEvents->emergencyLevel = $data->emergencyLevel;
    $disasterEvents->latitudeStart = $data->latitudeStart;
    $disasterEvents->latitudeEnd = $data->latitudeEnd;
    $disasterEvents->longitudeStart = $data->longitudeStart;
    $disasterEvents->longitudeEnd = $data->longitudeEnd;
    $disasterEvents->disasterDate = $data->disasterDate;
    $disasterEvents->disasterBase = $data->disasterBase;
 
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