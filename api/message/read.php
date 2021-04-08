<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
 
// database connection will be here

// include database and object files
include_once '../config/database.php';
include_once '../objects/message.php';
 
// instantiate database and table object
$database = new Database();
$db = $database->getConnection();
 
// initialize object
$message = new Message($db);

$stmt = $message->read();
$num = $stmt->rowCount();
 

if($num>0){
 

    $message_arr=array();
    $message_arr["records"]=array();
 
 
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
 
    echo json_encode($message_arr);
}

else{
 
    // set response code - 404 Not found
    http_response_code(404);
 
    // tell the user no items found
    echo json_encode(
        array("message" => "No items found.")
    );
}

?>