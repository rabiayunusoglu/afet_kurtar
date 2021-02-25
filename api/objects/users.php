<?php
class Users{
 
    // database connection and table name
    private $conn;
    private $table_name = "users";
 
    // object properties
    public $userID;
    public $userType;
    public $userName;
    public $email;
    public $createTime;
 
    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    function read(){
 
        // select all query
        $query = "SELECT
                    userID, userType, userName, email, createTime
                FROM
                    " . $this->table_name . " ";
     
        // prepare query statement
        $stmt = $this->conn->prepare($query);
     
        // execute query
        $stmt->execute();
     
        return $stmt;
    }

    function create(){
 
        // query to insert record
        $query = "INSERT INTO
                    " . $this->table_name . "
                SET
                    userType=:userType, userName=:userName, email=:email, createTime=:createTime";
     
        // prepare query
        $stmt = $this->conn->prepare($query);
     
        // sanitize
        $this->userType=htmlspecialchars(strip_tags($this->userType));
        $this->userName=htmlspecialchars(strip_tags($this->userName));
        $this->email=htmlspecialchars(strip_tags($this->email));
        $this->createTime=htmlspecialchars(strip_tags($this->createTime));
    
     
        // bind values
        $stmt->bindParam(":userType", $this->userType);
        $stmt->bindParam(":userName", $this->userName);
        $stmt->bindParam(":email", $this->email);
        $stmt->bindParam(":createTime", $this->createTime);
    
     
        // execute query
        if($stmt->execute()){
            return true;
        }
     
        return false;
         
    }

    function searchByMail($mail){
        echo $mail;
        // select all query
        $query = "SELECT
                    userID, userType, userName, email, createTime
                FROM
                    " . $this->table_name . "
                WHERE
                    email = ? ";
     
        // prepare query statement
        $stmt = $this->conn->prepare($query);
     
        // sanitize
        $mail=htmlspecialchars(strip_tags($mail));
     
        // bind
        $stmt->bindParam(1, $mail);
     
        // execute query
        $stmt->execute();
     
        return $stmt;
    }
}
?>