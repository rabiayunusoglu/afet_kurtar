<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
 
// include database and object files
include_once '../config/database.php';
include_once '../objects/disasterEvents.php';
 
// instantiate database and disasterEvents object
$database = new Database();
$db = $database->getConnection();
 
// initialize object
$disasterEvents = new DisasterEvents($db);
 
// get keywords
$data = json_decode(file_get_contents("php://input"),true);

$disasterEvents->disasterID = isset($data["disasterID"]) ? $data["disasterID"] : "";
$disasterEvents->disasterID = isset($data["disasterType"]) ? $data["disasterType"] : "";
$disasterEvents->emergencyLevel = isset($data["emergencyLevel"]) ? $data["emergencyLevel"] : "";
$disasterEvents->latitudeStart = isset($data["latitude"]) ? $data["latitude"] : "";
$disasterEvents->longitudeStart = isset($data["longitude"]) ? $data["longitude"] : "";
$disasterEvents->disasterDate = isset($data["disasterDate"]) ? $data["disasterDate"] : "";
$disasterEvents->disasterBase = isset($data["disasterBase"]) ? $data["disasterBase"] : "";
$disasterEvents->disasterName = isset($data["disasterName"]) ? $data["disasterName"] : "";

// query disasterEvents
$stmt = $disasterEvents->search();
$num = $stmt->rowCount();
 
// check if more than 0 record found
if($num>0){
 
    // disasterEvents array
    $disasterEvents_arr=array();
    $disasterEvents_arr["records"]=array();
 
    // retrieve our table contents
    // fetch() is faster than fetchAll()
    // http://stackoverflow.com/questions/2770630/pdofetchall-vs-pdofetch-in-a-loop
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);
 
        $disasterEvents_item=array(
            "disasterID" => $disasterID,
            "disasterType" => $disasterType,
            "emergencyLevel" => $emergencyLevel,
            "latitude" => $latitude,
            "longitude" => $longitude,
            "disasterDate" => $disasterDate,
            "disasterBase" => $disasterBase,
            "disasterName" => $disasterName,
        );
 
        array_push($disasterEvents_arr["records"], $disasterEvents_item);
    }
 
    // set response code - 200 OK
    http_response_code(200);
 
    // show disasterEvents data
    echo json_encode($disasterEvents_arr);
}
 
else{
    // set response code - 404 Not found
    http_response_code(404);
 
    // tell the user no disasterEvents found
    echo json_encode(
        array("message" => "no disasterEvents found.")
    );
}
?>