<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
 
// database connection will be here

// include database and object files
include_once '../config/database.php';
include_once '../objects/personnelUser.php';
 
// instantiate database and table object
$database = new Database();
$db = $database->getConnection();
 
// initialize object
$personnelUser = new PersonnelUser($db);


$stmt = $personnelUser->read();
$num = $stmt->rowCount();
 

if($num>0){
 

    $personnelUser_arr=array();
    $personnelUser_arr["records"]=array();
 
 
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);
 
        $personnelUser_item=array(
            "personnelID" => $personnelID,
	    "personnelName" => $personnelName,
            "personnelEmail" => $personnelEmail,
	    "personnelRole" => $personnelRole,
	    "teamID" => $teamID,
            "latitude" => $latitude,
            "longitude" => $longitude,
            "institution" => $institution,
            "locationTime" => $locationTime,
        );
 
        array_push($personnelUser_arr["records"], $personnelUser_item);
    }
 
    // set response code - 200 OK
    http_response_code(200);
 
    echo json_encode($personnelUser_arr);
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