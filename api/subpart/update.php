
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
include_once '../objects/subpart.php';
 
$database = new Database();
$db = $database->getConnection();
 
$subpart = new Subpart($db);
 
// get posted data
$data = json_decode(file_get_contents("php://input"),true);
  

 
if(
    isset($data["subpartID"])
){
 
    // set user property values
    $subpart->subpartID = isset($data["subpartID"]) ? $data["subpartID"] : "";
    $subpart->disasterID = isset($data["disasterID"]) ? $data["disasterID"] : "";
    $subpart->latitude = isset($data["latitude"]) ? $data["latitude"] : "";
    $subpart->longitude = isset($data["longitude"]) ? $data["longitude"] : "";
    $subpart->address = isset($data["address"]) ? $data["address"] : "";
    $subpart->missingPerson = isset($data["missingPerson"]) ? $data["missingPerson"] : "";
    $subpart->rescuedPerson = isset($data["rescuedPerson"]) ? $data["rescuedPerson"] : "";
    $subpart->isOpenForVolunteers = isset($data["isOpenForVolunteers"]) ? $data["isOpenForVolunteers"] : "";
    $subpart->subpartName = isset($data["subpartName"]) ? $data["subpartName"] : "";
    $subpart->disasterName = isset($data["disasterName"]) ? $data["disasterName"] : "";
    $subpart->status = isset($data["status"]) ? $data["status"] : "";
    $subpart->emergencyLevel = isset($data["emergencyLevel"]) ? $data["emergencyLevel"] : "";
        
 
    // create the product
    if($subpart->update()){
 
        // set response code - 201 created
        http_response_code(201);
 
        // tell the user
        echo json_encode(array("message" => "subpart was updated."));
    }
 
    // if unable to create the product, tell the user
    else{
 
        // set response code - 503 service unavailable
        http_response_code(503);
 
        // tell the user
        echo json_encode(array("message" => "Unable to update subpart."));
    }
}
 
// tell the user data is incomplete
else{
 
    // set response code - 400 bad request
    http_response_code(400);
 
    // tell the user
    echo json_encode(array("message" => "Unable to update subpart. Data is incomplete."));
}

?>