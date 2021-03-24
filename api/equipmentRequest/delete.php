

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
include_once '../objects/equipmentRequest.php';
  
$database = new Database();
$db = $database->getConnection();
 
$equipmentRequest = new EquipmentRequest($db);
   
// get posted data
$data = json_decode(file_get_contents("php://input"),true);
  

  
// delete the equipmentRequest
if(
     isset($data["equipmentRequestID"])
){
 
    // set user property values
    $equipmentRequest->equipmentRequestID = isset($data["equipmentRequestID"]) ? $data["equipmentRequestID"] : "";
   
 
    if($equipmentRequest->delete()){
 
        // set response code - 201 created
        http_response_code(201);
 
        // tell the user
        echo json_encode(array("message" => "equipmentRequest was deleted."));
    }
 

    else{
 
        // set response code - 503 service unavailable
        http_response_code(503);
 
        // tell the user
        echo json_encode(array("message" => "Unable to delete equipmentRequest."));
    }
}
 
// tell the user data is incomplete
else{
 
    // set response code - 400 bad request
    http_response_code(400);
 
    // tell the user
    echo json_encode(array("message" => "Unable to delete equipmentRequest. Data is incomplete."));
}
?>