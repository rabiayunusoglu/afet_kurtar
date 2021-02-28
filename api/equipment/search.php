<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
 
// include database and object files
include_once '../config/database.php';
include_once '../objects/equipment.php';
 
// instantiate database and equipment object
$database = new Database();
$db = $database->getConnection();
 
// initialize object
$equipment = new Equipment($db);
 
// get keywords
$data = json_decode(file_get_contents("php://input"),true);

$equipment->equipmentID = isset($data["equipmentID"]) ? $data["equipmentID"] : "";
$equipment->equipmentName = isset($data["equipmentName"]) ? $data["equipmentName"] : "";
$equipment->equipmentImageURL = isset($data["equipmentImageURL"]) ? $data["equipmentImageURL"] : "";

// query equipment
$stmt = $equipment->search();
$num = $stmt->rowCount();
 
// check if more than 0 record found
if($num>0){
 
    // equipment array
    $equipment_arr=array();
    $equipment_arr["records"]=array();
 
    // retrieve our table contents
    // fetch() is faster than fetchAll()
    // http://stackoverflow.com/questions/2770630/pdofetchall-vs-pdofetch-in-a-loop
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);
 
        $equipment_item=array(
            "equipmentID" => $equipmentID,
            "equipmentName" => $equipmentName,
            "equipmentImageURL" => $equipmentImageURL,
        );
 
        array_push($equipment_arr["records"], $equipment_item);
    }
 
    // set response code - 200 OK
    http_response_code(200);
 
    // show equipment data
    echo json_encode($equipment_arr);
}
 
else{
    // set response code - 404 Not found
    http_response_code(404);
 
    // tell the user no equipment found
    echo json_encode(
        array("message" => "no equipment found.")
    );
}
?>