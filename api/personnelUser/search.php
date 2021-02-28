<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
 
// include database and object files
include_once '../config/database.php';
include_once '../objects/personnelUser.php';
 
// instantiate database and personnelUser object
$database = new Database();
$db = $database->getConnection();
 
// initialize object
$personnelUser = new PersonnelUser($db);
 
// get keywords
$data = json_decode(file_get_contents("php://input"),true);

$personnelUser->personnelID = isset($data["personnelID"]) ? $data["personnelID"] : "";
$personnelUser->institution = isset($data["institution"]) ? $data["institution"] : "";
$personnelUser->latitude = isset($data["latitude"]) ? $data["latitude"] : "";
$personnelUser->longitude = isset($data["longitude"]) ? $data["longitude"] : "";
$personnelUser->personnelName = isset($data["personnelName"]) ? $data["personnelName"] : "";
$personnelUser->personnelRole = isset($data["personnelRole"]) ? $data["personnelRole"] : "";
$personnelUser->teamID = isset($data["teamID"]) ? $data["teamID"] : "";

// query personnelUser
$stmt = $personnelUser->search();
$num = $stmt->rowCount();
 
// check if more than 0 record found
if($num>0){
 
    // personnelUser array
    $personnelUser_arr=array();
    $personnelUser_arr["records"]=array();
 
    // retrieve our table contents
    // fetch() is faster than fetchAll()
    // http://stackoverflow.com/questions/2770630/pdofetchall-vs-pdofetch-in-a-loop
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);
 
        $personnelUser_item=array(
            "personnelID" => $personnelID,
            "institution" => $institution,
            "latitude" => $latitude,
            "longitude" => $longitude,
            "personnelName" => $personnelName,
            "personnelRole" => $personnelRole,
            "teamID" => $teamID,
        );
 
        array_push($personnelUser_arr["records"], $personnelUser_item);
    }
 
    // set response code - 200 OK
    http_response_code(200);
 
    // show personnelUser data
    echo json_encode($personnelUser_arr);
}
 
else{
    // set response code - 404 Not found
    http_response_code(404);
 
    // tell the user no personnelUser found
    echo json_encode(
        array("message" => "no personnelUser found.")
    );
}
?>