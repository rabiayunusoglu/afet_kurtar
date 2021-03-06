<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-institution: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-institution, Access-Control-Allow-Headers, Authorization, X-Requested-With");
 
// get database connection
include_once '../config/database.php';
 
// instantiate product object
include_once '../objects/personnelUser.php';
 
$database = new Database();
$db = $database->getConnection();
 
$personnelUser = new PersonnelUser($db);
 
// get posted data
$data = json_decode(file_get_contents("php://input"),true);
 
// make sure data is not empty
if(
    isset($data["personnelID"]) &&
    isset($data["institution"]) &&
    isset($data["personnelName"]) &&
    isset($data["personnelRole"])
){
 
    // set user property values
    $personnelUser->personnelID = isset($data["personnelID"]) ? $data["personnelID"] : "";
    $personnelUser->institution = isset($data["institution"]) ? $data["institution"] : "";
    $personnelUser->latitude = isset($data["latitude"]) ? $data["latitude"] : "";
    $personnelUser->longitude = isset($data["longitude"]) ? $data["longitude"] : "";
    $personnelUser->personnelName = isset($data["personnelName"]) ? $data["personnelName"] : "";
    $personnelUser->personnelRole = isset($data["personnelRole"]) ? $data["personnelRole"] : "";
    $personnelUser->teamID = isset($data["teamID"]) ? $data["teamID"] : "";
 
    // create the product
    if($personnelUser->create()){
 
        // set response code - 201 created
        http_response_code(201);
 
        // tell the user
        echo json_encode(array("message" => "personnelUser was created."));
    }
 
    // if unable to create the product, tell the user
    else{
 
        // set response code - 503 service unavailable
        http_response_code(503);
 
        // tell the user
        echo json_encode(array("message" => "Unable to create personnelUser."));
    }
}
 
// tell the user data is incomplete
else{
 
    // set response code - 400 bad request
    http_response_code(400);
 
    // tell the user
    echo json_encode(array("message" => "Unable to create personnelUser. Data is incomplete."));
}
?>