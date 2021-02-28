<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
 
// include database and object files
include_once '../config/database.php';
include_once '../objects/team.php';
 
// instantiate database and team object
$database = new Database();
$db = $database->getConnection();
 
// initialize object
$team = new Team($db);
 
// get keywords
$data = json_decode(file_get_contents("php://input"),true);

$team->teamID = isset($data["teamID"]) ? $data["teamID"] : "";
$team->assignedSubpartID = isset($data["assignedSubpartID"]) ? $data["assignedSubpartID"] : "";
$team->status = isset($data["status"]) ? $data["status"] : "";
$team->needManPower = isset($data["needManPower"]) ? $data["needManPower"] : "";
$team->needEquipment = isset($data["needEquipment"]) ? $data["needEquipment"] : "";

// query team
$stmt = $team->search();
$num = $stmt->rowCount();
 
// check if more than 0 record found
if($num>0){
 
    // team array
    $team_arr=array();
    $team_arr["records"]=array();
 
    // retrieve our table contents
    // fetch() is faster than fetchAll()
    // http://stackoverflow.com/questions/2770630/pdofetchall-vs-pdofetch-in-a-loop
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);
 
        $team_item=array(
            "teamID" => $teamID,
            "assignedSubpartID" => $assignedSubpartID,
            "status" => $status,
            "needManPower" => $needManPower,
            "needEquipment" => $needEquipment,
        );
 
        array_push($team_arr["records"], $team_item);
    }
 
    // set response code - 200 OK
    http_response_code(200);
 
    // show team data
    echo json_encode($team_arr);
}
 
else{
    // set response code - 404 Not found
    http_response_code(404);
 
    // tell the user no team found
    echo json_encode(
        array("message" => "no team found.")
    );
}
?>