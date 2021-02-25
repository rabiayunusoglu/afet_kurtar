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
include_once '../objects/notice.php';
 
$database = new Database();
$db = $database->getConnection();
 
$notice = new notice($db);
 
// get posted data
$data = json_decode(file_get_contents("php://input"));
 
// make sure data is not empty
if(
    !empty($data->type) &&
    !empty($data->latitude) &&
    !empty($data->longitude) &&
    !empty($data->message) &&
    !empty($data->imageURL)
){
 
    // set user property values
    $notice->type = $data->type;
    $notice->latitude = $data->latitude;
    $notice->longitude = $data->longitude;
    $notice->message = $data->message;
    $notice->imageURL = $data->imageURL;
 
    // create the product
    if($notice->create()){
 
        // set response code - 201 created
        http_response_code(201);
 
        // tell the user
        echo json_encode(array("message" => "notice was created."));
    }
 
    // if unable to create the product, tell the user
    else{
 
        // set response code - 503 service unavailable
        http_response_code(503);
 
        // tell the user
        echo json_encode(array("message" => "Unable to create notice."));
    }
}
 
// tell the user data is incomplete
else{
 
    // set response code - 400 bad request
    http_response_code(400);
 
    // tell the user
    echo json_encode(array("message" => "Unable to create notice. Data is incomplete."));
}
?>