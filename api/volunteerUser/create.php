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
include_once '../objects/volunteerUser.php';
 
$database = new Database();
$db = $database->getConnection();
 
$volunteerUser = new VolunteerUser($db);
 
// get posted data
$data = json_decode(file_get_contents("php://input"),true);
 
// make sure data is not empty
if(
    // isset($data["volunteerID"]) &&
    // isset($data["volunteerName"]) &&
    // isset($data["address"]) &&
    // isset($data["isExperienced"]) &&
    // isset($data["haveFirstAidCert"]) &&
    // isset($data["requestedSubpart"]) &&
    // isset($data["responseSubpart"]) &&
    // isset($data["assignedTeamID"]) &&
    // isset($data["role"]) &&
    // isset($data["latitude"]) &&
    // isset($data["longitude"])
    true
){
 
    // set user property values
    $volunteerUser->volunteerID = isset($data["volunteerID"]) ? $data["volunteerID"] : "";
    $volunteerUser->volunteerName = isset($data["volunteerName"]) ? $data["volunteerName"] : "";
    $volunteerUser->address = isset($data["address"]) ? $data["address"] : "";
    $volunteerUser->isExperienced = isset($data["isExperienced"]) ? $data["isExperienced"] : "";
    $volunteerUser->haveFirstAidCert = isset($data["haveFirstAidCert"]) ? $data["haveFirstAidCert"] : "";
    $volunteerUser->requestedSubpart = isset($data["requestedSubpart"]) ? $data["requestedSubpart"] : "";
    $volunteerUser->responseSubpart = isset($data["responseSubpart"]) ? $data["responseSubpart"] : "";
    $volunteerUser->assignedTeamID = isset($data["assignedTeamID"]) ? $data["assignedTeamID"] : "";
    $volunteerUser->role = isset($data["role"]) ? $data["role"] : "";
    $volunteerUser->latitude = isset($data["latitude"]) ? $data["latitude"] : "";
    $volunteerUser->longitude = isset($data["longitude"]) ? $data["longitude"] : "";
    $volunteerUser->role = isset($data["locationTime"]) ? $data["locationTime"] : "";
    $volunteerUser->latitude = isset($data["tc"]) ? $data["tc"] : "";
    $volunteerUser->longitude = isset($data["birthDate"]) ? $data["birthDate"] : "";
 
    // create the product
    if($volunteerUser->create()){
 
        // set response code - 201 created
        http_response_code(201);
 
        // tell the user
        echo json_encode(array("message" => "volunteerUser was created."));
    }
 
    // if unable to create the product, tell the user
    else{
 
        // set response code - 503 service unavailable
        http_response_code(503);
 
        // tell the user
        echo json_encode(array("message" => "Unable to create volunteerUser."));
    }
}
 
// tell the user data is incomplete
else{
 
    // set response code - 400 bad request
    http_response_code(400);
 
    // tell the user
    echo json_encode(array("message" => "Unable to create volunteerUser. Data is incomplete."));
}
?>