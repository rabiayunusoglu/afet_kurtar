<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-disasterID: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-disasterID, Access-Control-Allow-Headers, Authorization, X-Requested-With");
 
// get database connection
include_once '../config/database.php';
 
// instantiate product object
include_once '../objects/users.php';
 
$database = new Database();
$db = $database->getConnection();
 
$users = new Users($db);
 
// get posted data
$data = json_decode(file_get_contents("php://input"),true);
 
// make sure data is not empty
if(
    // isset($data["userType"]) &&
    // isset($data["userName"]) &&
    // isset($data["email"])
    true
){
 
    // set user property values
    $users->userType = isset($data["userType"]) ? $data["userType"] : "";
    $users->userName = isset($data["userName"]) ? $data["userName"] : "";
    $users->email = isset($data["email"]) ? $data["email"] : "";
 
    // create the product
    if($users->create()){

        $stmt = $users->search();

        $insertedUserID = 0;
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
            // extract row
            // this will make $row['name'] to
            // just $name only
            extract($row);
            $insertedUserID = $userID;
        }
        
        // set response code - 201 created
        http_response_code(201);
 
        // tell the user
        echo json_encode(array("message" => "users was created.", "id" => $insertedUserID));
    }
 
    // if unable to create the product, tell the user
    else{
 
        // set response code - 503 service unavailable
        http_response_code(503);
 
        // tell the user
        echo json_encode(array("message" => "Unable to create users."));
    }
}
 
// tell the user data is incomplete
else{
 
    // set response code - 400 bad request
    http_response_code(400);
 
    // tell the user
    echo json_encode(array("message" => "Unable to create users. Data is incomplete."));
}
?>