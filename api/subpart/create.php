<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-disasterID: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-disasterID, Access-Control-Allow-Headers, Authorization, X-Requested-With");
 
// get database connection
include_once '../config/database.php';
 
// instantiate product object
include_once '../objects/subpart.php';
 
$database = new Database();
$db = $database->getConnection();
 
$subpart = new Subpart($db);
 
// get posted data
$data = json_decode(file_get_contents("php://input"));
 
// make sure data is not empty
if(
    !empty($data->disasterID) &&
    !empty($data->latitude) &&
    !empty($data->longitude) &&
    !empty($data->address) &&
    !empty($data->missingPerson) &&
    !empty($data->rescuedPerson) &&
    !empty($data->isOpenForVolunteers)
){
 
    // set user property values
    $subpart->disasterID = $data->disasterID;
    $subpart->latitude = $data->latitude;
    $subpart->longitude = $data->longitude;
    $subpart->address = $data->address;
    $subpart->missingPerson = $data->missingPerson;
    $subpart->rescuedPerson = $data->rescuedPerson;
    $subpart->isOpenForVolunteers = $data->isOpenForVolunteers;
 
    // create the product
    if($subpart->create()){
 
        // set response code - 201 created
        http_response_code(201);
 
        // tell the user
        echo json_encode(array("message" => "subpart was created."));
    }
 
    // if unable to create the product, tell the user
    else{
 
        // set response code - 503 service unavailable
        http_response_code(503);
 
        // tell the user
        echo json_encode(array("message" => "Unable to create subpart."));
    }
}
 
// tell the user data is incomplete
else{
 
    // set response code - 400 bad request
    http_response_code(400);
 
    // tell the user
    echo json_encode(array("message" => "Unable to create subpart. Data is incomplete."));
}
?>