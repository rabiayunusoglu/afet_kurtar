<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
 
// include database and object files
include_once '../config/database.php';
include_once '../objects/volunteerUser.php';
 
// instantiate database and volunteerUser object
$database = new Database();
$db = $database->getConnection();
 
// initialize object
$volunteerUser = new VolunteerUser($db);
 
// get keywords
$data = json_decode(file_get_contents("php://input"),true);

$volunteerUser->volunteerID = isset($data["volunteerID"]) ? $data["volunteerID"] : "";
$volunteerUser->volunteerName = isset($data["volunteerName"]) ? $data["volunteerName"] : "";
$volunteerUser->address = isset($data["address"]) ? $data["address"] : "";
$volunteerUser->isExperienced = isset($data["isExperienced"]) ? $data["isExperienced"] : "";
$volunteerUser->isExperienced = isset($data["haveFirstAidCert"]) ? $data["haveFirstAidCert"] : "";
$volunteerUser->requestedSubpart = isset($data["requestedSubpart"]) ? $data["requestedSubpart"] : "";
$volunteerUser->responseSubpart = isset($data["responseSubpart"]) ? $data["responseSubpart"] : "";
$volunteerUser->assignedTeamID = isset($data["assignedTeamID"]) ? $data["assignedTeamID"] : "";
$volunteerUser->role = isset($data["role"]) ? $data["role"] : "";
$volunteerUser->latitude = isset($data["latitude"]) ? $data["latitude"] : "";
$volunteerUser->longitude = isset($data["longitude"]) ? $data["longitude"] : "";

// query volunteerUser
$stmt = $volunteerUser->search();
$num = $stmt->rowCount();
 
// check if more than 0 record found
if($num>0){
 
    // volunteerUser array
    $volunteerUser_arr=array();
    $volunteerUser_arr["records"]=array();
 
    // retrieve our table contents
    // fetch() is faster than fetchAll()
    // http://stackoverflow.com/questions/2770630/pdofetchall-vs-pdofetch-in-a-loop
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);
 
        $volunteerUser_item=array(
            "volunteerID" => $volunteerID,
            "volunteerName" => $volunteerName,
            "address" => $address,
            "isExperienced" => $isExperienced,
            "haveFirstAidCert" => $haveFirstAidCert,
            "requestedSubpart" => $requestedSubpart,
            "responseSubpart" => $responseSubpart,
            "assignedTeamID" => $assignedTeamID,
            "role" => $role,
            "latitude" => $latitude,
            "longitude" => $longitude,
        );
 
        array_push($volunteerUser_arr["records"], $volunteerUser_item);
    }
 
    // set response code - 200 OK
    http_response_code(200);
 
    // show volunteerUser data
    echo json_encode($volunteerUser_arr);
}
 
else{
    // set response code - 404 Not found
    http_response_code(404);
 
    // tell the user no volunteerUser found
    echo json_encode(
        array("message" => "no volunteerUser found.")
    );
}
?>