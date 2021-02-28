<?php
session_start();
//$data = json_decode(file_get_contents("php://input"),true);
//echo json_decode(file_get_contents("php://input"),true);
//echo $_POST["userType"];

$_SESSION["userType"] = $_POST["userType"];
$_SESSION["userID"] = $_POST["userID"];
$_SESSION["email"] = $_POST["email"];
$_SESSION["userName"] = $_POST["userName"];
$_SESSION["createTime"] = $_POST["createTime"];

http_response_code(200);

echo $_SESSION["userID"];

?>