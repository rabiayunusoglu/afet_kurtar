<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
 
// database connection will be here

// include database and object files
include_once '../config/database.php';
include_once '../objects/status.php';
 
// instantiate database and table object
$database = new Database();
$db = $database->getConnection();
 
// initialize object
$status = new Status($db);


$stmt = $status->read();
$num = $stmt->rowCount();
 

if($num>0){
 

    $status_arr=array();
    $status_arr["records"]=array();
 
 
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);
 
        $status_item=array(
            "statusID" => $statusID,
            "statusMessage" => $statusMessage,
            "teamID" => $teamID,
            "statusTime" => $statusTime
        );
 
        array_push($status_arr["records"], $status_item);
    }
 
    // set response code - 200 OK
    http_response_code(200);
 
    echo json_encode($status_arr);
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