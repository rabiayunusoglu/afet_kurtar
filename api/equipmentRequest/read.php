<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
 
// database connection will be here

// include database and object files
include_once '../config/database.php';
include_once '../objects/equipmentRequest.php';
 
// instantiate database and table object
$database = new Database();
$db = $database->getConnection();
 
// initialize object
$equipmentRequest = new EquipmentRequest($db);


$stmt = $equipmentRequest->read();
$num = $stmt->rowCount();
 

if($num>0){
 

    $equipmentRequest_arr=array();
    $equipmentRequest_arr["records"]=array();
 
 
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);
 
        $equipmentRequest_item=array(
            "equipmentRequestID" => $equipmentRequestID,
            "quantity" => $quantity,
            "equipmentID" => $equipmentID,
            "teamRequestID" => $teamRequestID
        );
 
        array_push($equipmentRequest_arr["records"], $equipmentRequest_item);
    }
 
    // set response code - 200 OK
    http_response_code(200);
 
    echo json_encode($equipmentRequest_arr);
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