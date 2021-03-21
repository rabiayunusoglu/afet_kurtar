<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
 
// database connection will be here

// include database and object files
include_once '../config/database.php';
include_once '../objects/subpart.php';
 
// instantiate database and table object
$database = new Database();
$db = $database->getConnection();
 
// initialize object
$subpart = new Subpart($db);


$stmt = $subpart->read();
$num = $stmt->rowCount();
 

if($num>0){
 

    $subpart_arr=array();
    $subpart_arr["records"]=array();
 
 
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);
 
        $subpart_item=array(
            "subpartID" => $subpartID,
            "disasterID" => $disasterID,
            "latitude" => $latitude,
            "longitude" => $longitude,
            "address" => $address,
            "missingPerson" => $missingPerson,
            "rescuedPerson" => $rescuedPerson,
            "isOpenForVolunteers" => $isOpenForVolunteers,
            "subpartName" => $subpartName,
            "disasterName" => $disasterName,
            "status" => $status,
            "emergencyLevel" => $emergencyLevel,
        );
 
        array_push($subpart_arr["records"], $subpart_item);
    }
 
    // set response code - 200 OK
    http_response_code(200);
 
    echo json_encode($subpart_arr);
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