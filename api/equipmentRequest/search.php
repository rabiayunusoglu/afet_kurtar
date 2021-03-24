<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
 
// include database and object files
include_once '../config/database.php';
include_once '../objects/equipmentRequest.php';
 
// instantiate database and equipmentRequest object
$database = new Database();
$db = $database->getConnection();
 
// initialize object
$equipmentRequest = new EquipmentRequest($db);
 
// get keywords
$data = json_decode(file_get_contents("php://input"),true);

$equipmentRequest->equipmentRequestID = isset($data["equipmentRequestID"]) ? $data["equipmentRequestID"] : "";
$equipmentRequest->quantity = isset($data["quantity"]) ? $data["quantity"] : "";
$equipmentRequest->equipmentID = isset($data["equipmentID"]) ? $data["equipmentID"] : "";
$equipmentRequest->teamRequestID = isset($data["teamRequestID"]) ? $data["teamRequestID"] : "";


// query equipmentRequest
$stmt = $equipmentRequest->search();
$num = $stmt->rowCount();
 
// check if more than 0 record found
if($num>0){
 
    // equipmentRequest array
    $equipmentRequest_arr=array();
    $equipmentRequest_arr["records"]=array();
 
    // retrieve our table contents
    // fetch() is faster than fetchAll()
    // http://stackoverflow.com/questions/2770630/pdofetchall-vs-pdofetch-in-a-loop
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);
 
        $equipmentRequest_item=array(
            "equipmentRequestID" => $equipmentRequestID,
            "quantity" => $quantity,
            "equipmentID" => $equipmentID,
            "teamRequestID" => $teamRequestID,
        );
 
        array_push($equipmentRequest_arr["records"], $equipmentRequest_item);
    }
 
    // set response code - 200 OK
    http_response_code(200);
 
    // show equipmentRequest data
    echo json_encode($equipmentRequest_arr);
}
 
else{
    // set response code - 404 Not found
    http_response_code(404);
 
    // tell the user no equipmentRequest found
    echo json_encode(
        array("message" => "no equipmentRequest found.")
    );
}
?>