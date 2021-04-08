<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
 
// include database and object files
include_once '../config/database.php';
include_once '../objects/message.php';
 
// instantiate database and users object
$database = new Database();
$db = $database->getConnection();
 
// initialize object
$message = new Message($db);
 
// get keywords
$data = json_decode(file_get_contents("php://input"),true);


//userID, userType, userName, email, createTime
$message->messageID = isset($data["messageID"]) ? $data["messageID"] : "";
$message->teamID = isset($data["teamID"]) ? $data["teamID"] : "";
$message->userID = isset($data["userID"]) ? $data["userID"] : "";
$message->messageData = isset($data["messageData"]) ? $data["messageData"] : "";
$message->messageName = isset($data["messageName"]) ? $data["messageName"] : "";
$message->messageTime = isset($data["messageTime"]) ? $data["messageTime"] : "";





//$mail = $_POST['email'];
 
// query users
$stmt = $message->search();
$num = $stmt->rowCount();
 
// check if more than 0 record found
if($num>0){
 
    // users array
    $message_arr=array();
    $message_arr["records"]=array();
 
    // retrieve our table contents
    // fetch() is faster than fetchAll()
    // http://stackoverflow.com/questions/2770630/pdofetchall-vs-pdofetch-in-a-loop
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);
 
        $message_item=array(
            "messageID" => $messageID,
            "teamID" => $teamID,
            "userID" => $userID,
            "messageData" => $messageData,
            "messageName" => $messageName,
            "messageTime" => $messageTime
        );
 
        array_push($message_arr["records"], $message_item);
    }
 
    // set response code - 200 OK
    http_response_code(200);
 
    // show users data
    echo json_encode($message_arr);
}
 
else{
    // set response code - 404 Not found
    http_response_code(404);
 
    // tell the user no users found
    echo json_encode(
        array("message" => "no message found.")
    );
}
?>