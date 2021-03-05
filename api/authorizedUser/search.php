<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
 
// include database and object files
include_once '../config/database.php';
include_once '../objects/authorizedUser.php';
 
// instantiate database and authorizedUser object
$database = new Database();
$db = $database->getConnection();
 
// initialize object
$authorizedUser = new AuthorizedUser($db);
 
// get keywords
$data = json_decode(file_get_contents("php://input"),true);

$authorizedUser->authorizedID = isset($data["authorizedID"]) ? $data["authorizedID"] : "";
$authorizedUser->authorizedName = isset($data["authorizedName"]) ? $data["authorizedName"] : "";
$authorizedUser->institution = isset($data["institution"]) ? $data["institution"] : "";
 
// query authorizedUser
$stmt = $authorizedUser->search();
$num = $stmt->rowCount();
 
// check if more than 0 record found
if($num>0){
 
    // authorizedUser array
    $authorizedUser_arr=array();
    $authorizedUser_arr["records"]=array();
 
    // retrieve our table contents
    // fetch() is faster than fetchAll()
    // http://stackoverflow.com/questions/2770630/pdofetchall-vs-pdofetch-in-a-loop
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);
 
        $authorizedUser_item=array(
            "authorizedName" => $authorizedName,
            "institution" => $institution,
            "authorizedID" => $authorizedID,
        );
 
        array_push($authorizedUser_arr["records"], $authorizedUser_item);
    }
 
    // set response code - 200 OK
    http_response_code(200);
 
    // show authorizedUser data
    echo json_encode($authorizedUser_arr);
}
 
else{
    // set response code - 404 Not found
    http_response_code(404);
 
    // tell the user no authorizedUser found
    echo json_encode(
        array("message" => "no authorizedUser found.")
    );
}
?>