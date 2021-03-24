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
    $volunteerUser->address = isset($data["address"]) ? $data["address"] : null;
    $volunteerUser->isExperienced = isset($data["isExperienced"]) ? $data["isExperienced"] : null;
    $volunteerUser->haveFirstAidCert = isset($data["haveFirstAidCert"]) ? $data["haveFirstAidCert"] : null;
    $volunteerUser->requestedSubpart = isset($data["requestedSubpart"]) ? $data["requestedSubpart"] : null;
    $volunteerUser->responseSubpart = isset($data["responseSubpart"]) ? $data["responseSubpart"] : null;
    $volunteerUser->assignedTeamID = isset($data["assignedTeamID"]) ? $data["assignedTeamID"] : null;
    $volunteerUser->role = isset($data["role"]) ? $data["role"] : null;
    $volunteerUser->latitude = isset($data["latitude"]) ? $data["latitude"] : null;
    $volunteerUser->longitude = isset($data["longitude"]) ? $data["longitude"] : null;
    $volunteerUser->locationTime = isset($data["locationTime"]) ? $data["locationTime"] : null;
    $volunteerUser->tc = isset($data["tc"]) ? $data["tc"] : null;
    $volunteerUser->tel = isset($data["tel"]) ? $data["tel"] : null;
    $volunteerUser->birthDate = isset($data["birthDate"]) ? $data["birthDate"] : null;
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