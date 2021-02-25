<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
 
// include database and object files
include_once '../config/database.php';
include_once '../objects/users.php';
 
// instantiate database and users object
$database = new Database();
$db = $database->getConnection();
 
// initialize object
$users = new Users($db);
 
// get keywords
// $data = json_decode(file_get_contents("php://input"));
// $mail = $data->email;

$mail = $_POST['email'];
 
// query users
$stmt = $users->searchByMail($mail);
$num = $stmt->rowCount();
 
// check if more than 0 record found
if($num>0){
 
    // users array
    $users_arr=array();
    $users_arr["records"]=array();
 
    // retrieve our table contents
    // fetch() is faster than fetchAll()
    // http://stackoverflow.com/questions/2770630/pdofetchall-vs-pdofetch-in-a-loop
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);
 
        $users_item=array(
            "userID" => $userID,
            "userType" => $userType,
            "userName" => html_entity_decode($userName),
            "email" => $email,
            "createTime" => $createTime,
        );
 
        array_push($users_arr["records"], $users_item);
    }
 
    // set response code - 200 OK
    http_response_code(200);
 
    // show users data
    echo json_encode($users_arr);
}
 
else{
    // set response code - 404 Not found
    http_response_code(404);
 
    // tell the user no users found
    echo json_encode(
        array("message" => "no users found.")
    );
}
?>