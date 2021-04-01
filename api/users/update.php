
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
include_once '../objects/users.php';
 
$database = new Database();
$db = $database->getConnection();
 
$users = new Users($db);
 
// get posted data
$data = json_decode(file_get_contents("php://input"),true);
  

 
if(
    isset($data["userID"])
){
 
    // set user property values
    $users->userID = isset($data["userID"]) ? $data["userID"] : "";
    $users->userType = isset($data["userType"]) ? $data["userType"] : "";
    $users->userName = isset($data["userName"]) ? $data["userName"] : "";
    $users->email = isset($data["email"]) ? $data["email"] : "";
    $users->createTime = isset($data["createTime"]) ? $data["createTime"] : "";
    $users->userToken = isset($data["userToken"]) ? $data["userToken"] : "";

    
 
    // create the product
    if($users->update()){
 
        // set response code - 201 created
        http_response_code(201);
 
        // tell the user
        echo json_encode(array("message" => "users was updated."));
    }
 
    // if unable to create the product, tell the user
    else{
 
        // set response code - 503 service unavailable
        http_response_code(503);
 
        // tell the user
        echo json_encode(array("message" => "Unable to update users."));
    }
}
 
// tell the user data is incomplete
else{
 
    // set response code - 400 bad request
    http_response_code(400);
 
    // tell the user
    echo json_encode(array("message" => "Unable to update users. Data is incomplete."));
}

?>