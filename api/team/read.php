<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
 
// database connection will be here

// include database and object files
include_once '../config/database.php';
include_once '../objects/team.php';
 
// instantiate database and table object
$database = new Database();
$db = $database->getConnection();
 
// initialize object
$team = new Team($db);


$stmt = $team->read();
$num = $stmt->rowCount();
 

if($num>0){
 

    $team_arr=array();
    $team_arr["records"]=array();
 
 
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
            "needEquipment" => $needEquipment
        );
 
        array_push($team_arr["records"], $team_item);
    }
 
    // set response code - 200 OK
    http_response_code(200);
 
    echo json_encode($team_arr);
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