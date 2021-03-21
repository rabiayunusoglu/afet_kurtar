
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
include_once '../objects/team.php';
 
$database = new Database();
$db = $database->getConnection();
 
$team = new Team($db);
 
// get posted data
$data = json_decode(file_get_contents("php://input"),true);
  

 
if(
    // isset($data["teamID"]) &&
    // isset($data["status"]) &&
    // isset($data["needManPower"]) &&
    // isset($data["needEquipment"])
    true
){
 
    // set user property values
    $team->teamID = isset($data["teamID"]) ? $data["teamID"] : "";
    $team->status = isset($data["status"]) ? $data["status"] : "";
    $team->needManPower = isset($data["needManPower"]) ? $data["needManPower"] : "";
    
 
    // create the product
    if($team->update()){
 
        // set response code - 201 created
        http_response_code(201);
 
        // tell the user
        echo json_encode(array("message" => "team was updated."));
    }
 
    // if unable to create the product, tell the user
    else{
 
        // set response code - 503 service unavailable
        http_response_code(503);
 
        // tell the user
        echo json_encode(array("message" => "Unable to update team."));
    }
}
 
// tell the user data is incomplete
else{
 
    // set response code - 400 bad request
    http_response_code(400);
 
    // tell the user
    echo json_encode(array("message" => "Unable to update team. Data is incomplete."));
}

?>