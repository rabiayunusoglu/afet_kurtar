<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
 
// include database and object files
include_once '../config/database.php';
include_once '../objects/notice.php';
 
// instantiate database and notice object
$database = new Database();
$db = $database->getConnection();
 
// initialize object
$notice = new Notice($db);
 
// get keywords
$data = json_decode(file_get_contents("php://input"),true);

$notice->noticeID = isset($data["noticeID"]) ? $data["noticeID"] : "";
$notice->type = isset($data["type"]) ? $data["type"] : "";
$notice->latitude = isset($data["latitude"]) ? $data["latitude"] : "";
$notice->longitude = isset($data["longitude"]) ? $data["longitude"] : "";
$notice->message = isset($data["message"]) ? $data["message"] : "";
$notice->imageURL = isset($data["imageURL"]) ? $data["imageURL"] : "";

// query notice
$stmt = $notice->search();
$num = $stmt->rowCount();
 
// check if more than 0 record found
if($num>0){
 
    // notice array
    $notice_arr=array();
    $notice_arr["records"]=array();
 
    // retrieve our table contents
    // fetch() is faster than fetchAll()
    // http://stackoverflow.com/questions/2770630/pdofetchall-vs-pdofetch-in-a-loop
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);
 
        $notice_item=array(
            "noticeID" => $noticeID,
            "type" => $type,
            "latitude" => $latitude,
            "longitude" => $longitude,
            "message" => $message,
            "imageURL" => $imageURL,
        );
 
        array_push($notice_arr["records"], $notice_item);
    }
 
    // set response code - 200 OK
    http_response_code(200);
 
    // show notice data
    echo json_encode($notice_arr);
}
 
else{
    // set response code - 404 Not found
    http_response_code(404);
 
    // tell the user no notice found
    echo json_encode(
        array("message" => "no notice found.")
    );
}
?>