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
include_once '../objects/volunteerUser.php';
 
$database = new Database();
$db = $database->getConnection();
 
$volunteerUser = new VolunteerUser($db);
 
// get posted data
$data = json_decode(file_get_contents("php://input"));
 
// make sure data is not empty
if(
    !empty($data->volunteerName) &&
    !empty($data->address) &&
    !empty($data->isExperienced) &&
    !empty($data->haveFirstAidCert) &&
    !empty($data->requestedSubpart) &&
    !empty($data->responseSubpart) &&
    !empty($data->assignedTeamID) &&
    !empty($data->role) &&
    !empty($data->latitude) &&
    !empty($data->longitude)
){
 
    // set user property values
    $volunteerUser->volunteerName = $data->volunteerName;
    $volunteerUser->address = $data->address;
    $volunteerUser->isExperienced = $data->isExperienced;
    $volunteerUser->haveFirstAidCert = $data->haveFirstAidCert;
    $volunteerUser->requestedSubpart = $data->requestedSubpart;
    $volunteerUser->responseSubpart = $data->responseSubpart;
    $volunteerUser->assignedTeamID = $data->assignedTeamID;
    $volunteerUser->role = $data->role;
    $volunteerUser->latitude = $data->latitude;
    $volunteerUser->longitude = $data->longitude;
 
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