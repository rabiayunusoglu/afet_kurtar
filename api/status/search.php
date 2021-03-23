<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
 
// include database and object files
include_once '../config/database.php';
include_once '../objects/status.php';
 
// instantiate database and status object
$database = new Database();
$db = $database->getConnection();
 
// initialize object
$status = new Status($db);
 
// get keywords
$data = json_decode(file_get_contents("php://input"),true);

$status->statusID = isset($data["statusID"]) ? $data["statusID"] : "";
$status->statusMessage = isset($data["statusMessage"]) ? $data["statusMessage"] : "";
$status->teamID = isset($data["teamID"]) ? $data["teamID"] : "";
$status->subpartID = isset($data["subpartID"]) ? $data["subpartID"] : "";
$status->statusType = isset($data["statusType"]) ? $data["statusType"] : "";

// query status
$stmt = $status->search();
$num = $stmt->rowCount();
 
// check if more than 0 record found
if($num>0){
 
    // status array
    $status_arr=array();
    $status_arr["records"]=array();
 
    // retrieve our table contents
    // fetch() is faster than fetchAll()
    // http://stackoverflow.com/questions/2770630/pdofetchall-vs-pdofetch-in-a-loop
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);
 
        $status_item=array(
            "statusID" => $statusID,
            "statusMessage" => $statusMessage,
            "teamID" => $teamID,
            "subpartID" => $subpartID,
            "statusTime" => $statusTime,
        );
 
        array_push($status_arr["records"], $status_item);
    }
 
    // set response code - 200 OK
    http_response_code(200);
 
    // show status data
    echo json_encode($status_arr);
}
 
else{
    // set response code - 404 Not found
    http_response_code(404);
 
    // tell the user no status found
    echo json_encode(
        array("message" => "no status found.")
    );
}
?>