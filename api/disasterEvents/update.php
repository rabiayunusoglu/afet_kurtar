
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
  

 
if(
    isset($data["disasterID"]) 
){
 
    // set user property values
    $disasterEvents->disasterID = isset($data["disasterID"]) ? $data["disasterID"] : "";
    $disasterEvents->disasterType = isset($data["disasterType"]) ? $data["disasterType"] : "";
    $disasterEvents->emergencyLevel = isset($data["emergencyLevel"]) ? $data["emergencyLevel"] : "";
    $disasterEvents->latitude = isset($data["latitude"]) ? $data["latitude"] : "";
    $disasterEvents->longitude = isset($data["longitude"]) ? $data["longitude"] : "";
    $disasterEvents->disasterDate = isset($data["disasterDate"]) ? $data["disasterDate"] : "";
    $disasterEvents->disasterBase = isset($data["disasterBase"]) ? $data["disasterBase"] : "";
    $disasterEvents->disasterName = isset($data["disasterName"]) ? $data["disasterName"] : "";
    
 
    // create the product
    if($disasterEvents->update()){
 
        // set response code - 201 created
        http_response_code(201);
 
        // tell the user
        echo json_encode(array("message" => "disasterEvents was updated."));
    }
 
    // if unable to create the product, tell the user
    else{
 
        // set response code - 503 service unavailable
        http_response_code(503);
 
        // tell the user
        echo json_encode(array("message" => "Unable to update disasterEvents."));
    }
}
 
// tell the user data is incomplete
else{
 
    // set response code - 400 bad request
    http_response_code(400);
 
    // tell the user
    echo json_encode(array("message" => "Unable to update disasterEvents. Data is incomplete."));
}

?>