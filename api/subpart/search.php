<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
 
// include database and object files
include_once '../config/database.php';
include_once '../objects/subpart.php';
 
// instantiate database and subpart object
$database = new Database();
$db = $database->getConnection();
 
// initialize object
$subpart = new Subpart($db);
 
// get keywords
$data = json_decode(file_get_contents("php://input"),true);

$subpart->subpartID = isset($data["subpartID"]) ? $data["subpartID"] : "";
$subpart->disasterID = isset($data["disasterID"]) ? $data["disasterID"] : "";
$subpart->latitude = isset($data["latitude"]) ? $data["latitude"] : "";
$subpart->longitude = isset($data["longitude"]) ? $data["longitude"] : "";
$subpart->address = isset($data["address"]) ? $data["address"] : "";
$subpart->missingPerson = isset($data["missingPerson"]) ? $data["missingPerson"] : "";
$subpart->rescuedPerson = isset($data["rescuedPerson"]) ? $data["rescuedPerson"] : "";
$subpart->isOpenForVolunteers = isset($data["isOpenForVolunteers"]) ? $data["isOpenForVolunteers"] : "";

// query subpart
$stmt = $subpart->search();
$num = $stmt->rowCount();
 
// check if more than 0 record found
if($num>0){
 
    // subpart array
    $subpart_arr=array();
    $subpart_arr["records"]=array();
 
    // retrieve our table contents
    // fetch() is faster than fetchAll()
    // http://stackoverflow.com/questions/2770630/pdofetchall-vs-pdofetch-in-a-loop
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
        );
 
        array_push($subpart_arr["records"], $subpart_item);
    }
 
    // set response code - 200 OK
    http_response_code(200);
 
    // show subpart data
    echo json_encode($subpart_arr);
}
 
else{
    // set response code - 404 Not found
    http_response_code(404);
 
    // tell the user no subpart found
    echo json_encode(
        array("message" => "no subpart found.")
    );
}
?>