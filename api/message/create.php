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
include_once '../objects/message.php';
 
$database = new Database();
$db = $database->getConnection();
 
$message = new Message($db);
 
// get posted data
$data = json_decode(file_get_contents("php://input"),true);
 
// make sure data is not empty
if(
    isset($data["teamID"]) &&
    isset($data["userID"]) &&
    isset($data["messageData"])&&
    isset($data["messageName"])
    
){
 
    // set user property values
    $message->teamID = isset($data["teamID"]) ? $data["teamID"] : "";
    $message->userID = isset($data["userID"]) ? $data["userID"] : "";
    $message->messageData = isset($data["messageData"]) ? $data["messageData"] : "";
    $message->messageName = isset($data["messageName"]) ? $data["messageName"] : "";
 
 
    // create the product
    if($message->create()){

       

        // set response code - 201 created
        http_response_code(201);
 
        // tell the user
        echo json_encode(array("message" => "message created."));
    }
 
    // if unable to create the product, tell the user
    else{
 
        // set response code - 503 service unavailable
        http_response_code(503);
 
        // tell the user
        echo json_encode(array("message" => "Unable to create message."));
    }
}
 
// tell the user data is incomplete
else{
 
    // set response code - 400 bad request
    http_response_code(400);
 
    // tell the user
    echo json_encode(array("message" => "Unable to create message. Data is incomplete."));
}
?>