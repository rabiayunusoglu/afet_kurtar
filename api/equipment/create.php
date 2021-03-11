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
include_once '../objects/equipment.php';
 
$database = new Database();
$db = $database->getConnection();
 
$equipment = new Equipment($db);
 
// get posted data
$data = json_decode(file_get_contents("php://input"),true);
 
// make sure data is not empty
if(
    // isset($data["equipmentName"]) &&
    // isset($data["equipmentImageURL"])
    true
){
 
    // set user property values
    $equipment->equipmentName = isset($data["equipmentName"]) ? $data["equipmentName"] : "";
    $equipment->equipmentImageURL = isset($data["equipmentImageURL"]) ? $data["equipmentImageURL"] : "";
 
    // create the product
    if($equipment->create()){
 
        // set response code - 201 created
        http_response_code(201);
 
        // tell the user
        echo json_encode(array("message" => "Equipment was created."));
    }
 
    // if unable to create the product, tell the user
    else{
 
        // set response code - 503 service unavailable
        http_response_code(503);
 
        // tell the user
        echo json_encode(array("message" => "Unable to create equipment."));
    }
}
 
// tell the user data is incomplete
else{
 
    // set response code - 400 bad request
    http_response_code(400);
 
    // tell the user
    echo json_encode(array("message" => "Unable to create equipment. Data is incomplete."));
}
?>