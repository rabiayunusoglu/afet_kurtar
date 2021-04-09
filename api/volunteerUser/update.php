<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
  
// include database and object files
include_once '../config/database.php';
// instantiate product object
include_once '../objects/volunteerUser.php';

$database = new Database();
$db = $database->getConnection();
 
$volunteerUser = new VolunteerUser($db);
// get posted data
$data = json_decode(file_get_contents("php://input"),true);
 
// set ID property of volunteeruser to be edited
$volunteerUser->volunteerID = isset($data["volunteerID"]) ? $data["volunteerID"] : "";

// set volunteerUser property values
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
    $volunteerUser->locationTime = isset($data["locationTime"]) ? $data["locationTime"] : "";
    $volunteerUser->tc = isset($data["tc"]) ? $data["tc"] : "";
    $volunteerUser->tel = isset($data["tel"]) ? $data["tel"] : "";
    $volunteerUser->birthDate = isset($data["birthDate"]) ? $data["birthDate"] : "";
 // update the volunteerUser
if($volunteerUser->update()){
  
    // set response code - 200 ok
    http_response_code(200);
  
    // tell the user
    echo json_encode(array("message" => "volunteerUser was updated."));
}
// if unable to update the volunteerUser, tell the user
else{
  
    // set response code - 503 service unavailable
    http_response_code(503);
  
    // tell the user
    echo json_encode(array("message" => "Unable to update volunteeruser."));
}
?>