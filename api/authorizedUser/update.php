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
include_once '../objects/authorizedUser.php';
 
$database = new Database();
$db = $database->getConnection();
 
$authorizedUser = new AuthorizedUser($db);
 
// get posted data
$data = json_decode(file_get_contents("php://input"),true);
  

 
if(
    isset($data["authorizedID"]) 
){
 
    // set user property values
    $authorizedUser->authorizedID = isset($data["authorizedID"]) ? $data["authorizedID"] : "";
    $authorizedUser->authorizedName = isset($data["authorizedName"]) ? $data["authorizedName"] : "";
    $authorizedUser->institution = isset($data["institution"]) ? $data["institution"] : "";
    
 
    // create the product
    if($authorizedUser->update()){
 
        // set response code - 201 created
        http_response_code(201);
 
        // tell the user
        echo json_encode(array("message" => "authorizedUser was updated."));
    }
 
    // if unable to create the product, tell the user
    else{
 
        // set response code - 503 service unavailable
        http_response_code(503);
 
        // tell the user
        echo json_encode(array("message" => "Unable to update authorizedUser."));
    }
}
 
// tell the user data is incomplete
else{
 
    // set response code - 400 bad request
    http_response_code(400);
 
    // tell the user
    echo json_encode(array("message" => "Unable to update authorizedUser. Data is incomplete."));
}

?>