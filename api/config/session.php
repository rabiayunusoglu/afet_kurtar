<?php
session_start();
$data = json_decode(file_get_contents("php://input"));

$_SESSION["userType"] = $data->userType;
$_SESSION["userID"] = $data->userID;
$_SESSION["email"] = $data->email;
$_SESSION["userName"] = $data->userName;
$_SESSION["createTime"] = $data->createTime;

http_response_code(200);

?>