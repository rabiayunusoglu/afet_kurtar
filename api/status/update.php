
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
include_once '../objects/status.php';
 
$database = new Database();
$db = $database->getConnection();
 
$status = new Status($db);
 
// get posted data
$data = json_decode(file_get_contents("php://input"),true);
  

 
if(
    isset($data["statusID"])
){
 
    // set user property values
    $status->statusID = isset($data["statusID"]) ? $data["statusID"] : "";
    $status->statusMessage = isset($data["statusMessage"]) ? $data["statusMessage"] : "";
    $status->teamID = isset($data["teamID"]) ? $data["teamID"] : "";
    $status->subpartID = isset($data["subpartID"]) ? $data["subpartID"] : "";
    $status->statusType = isset($data["statusType"]) ? $data["statusType"] : "";
        
 
    // create the product
    if($status->update()){
 
        // set response code - 201 created
        http_response_code(201);
 
        // tell the user
        echo json_encode(array("message" => "status was updated."));
    }
 
    // if unable to create the product, tell the user
    else{
 
        // set response code - 503 service unavailable
        http_response_code(503);
 
        // tell the user
        echo json_encode(array("message" => "Unable to update status."));
    }
}
 
// tell the user data is incomplete
else{
 
    // set response code - 400 bad request
    http_response_code(400);
 
    // tell the user
    echo json_encode(array("message" => "Unable to update status. Data is incomplete."));
}

?>