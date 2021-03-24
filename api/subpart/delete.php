

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
  

  
// delete the subpart
if(
     isset($data["subpartID"])
){
 
    // set user property values
    $subpart->subpartID = isset($data["subpartID"]) ? $data["subpartID"] : "";
   
 
    if($subpart->delete()){
 
        // set response code - 201 created
        http_response_code(201);
 
        // tell the user
        echo json_encode(array("message" => "subpart was deleted."));
    }
 

    else{
 
        // set response code - 503 service unavailable
        http_response_code(503);
 
        // tell the user
        echo json_encode(array("message" => "Unable to delete subpart."));
    }
}
 
// tell the user data is incomplete
else{
 
    // set response code - 400 bad request
    http_response_code(400);
 
    // tell the user
    echo json_encode(array("message" => "Unable to delete subpart. Data is incomplete."));
}
?>