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
    public $userToken;
 
    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    function read(){
 
        // select all query
        $query = "SELECT
                    userID, userType, userName, email, createTime, userToken
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
                    userType=:userType, userName=:userName, email=:email, userToken=:userToken";
     
        // prepare query
        $stmt = $this->conn->prepare($query);
     
        // sanitize
        $this->userType=htmlspecialchars(strip_tags($this->userType));
        $this->userName=htmlspecialchars(strip_tags($this->userName));
        $this->email=htmlspecialchars(strip_tags($this->email));
        $this->userToken=htmlspecialchars(strip_tags($this->userToken));
    
     
        // bind values
        $stmt->bindParam(":userType", $this->userType);
        $stmt->bindParam(":userName", $this->userName);
        $stmt->bindParam(":email", $this->email);
        $stmt->bindParam(":userToken", $this->userToken);
    
     
        // execute query
        if($stmt->execute()){
            return true;
        }
     
        return false;
         
    }

    function search(){
        // select all query
        $query = "SELECT
                    userID, userType, userName, email, createTime, userToken
                FROM
                    " . $this->table_name . "
                WHERE
                    (userID = :userID OR :userID = '') 
                    AND (userType = :userType OR :userType = '') 
                    AND (userName = :userName OR :userName = '') 
                    AND (email = :email OR :email = '') 
                    AND (createTime = :createTime OR :createTime = '')
                    AND (userToken = :userToken OR :userToken = '')";
     
        // prepare query statement
        $stmt = $this->conn->prepare($query);
     

        // sanitize
        $this->userID=htmlspecialchars(strip_tags($this->userID));
        $this->userType=htmlspecialchars(strip_tags($this->userType));
        $this->userName=htmlspecialchars(strip_tags($this->userName));
        $this->email=htmlspecialchars(strip_tags($this->email));
        $this->createTime=htmlspecialchars(strip_tags($this->createTime));
        $this->userToken=htmlspecialchars(strip_tags($this->userToken));

     
        // bind values
        $stmt->bindParam(":userID", $this->userID);
        $stmt->bindParam(":userType", $this->userType);
        $stmt->bindParam(":userName", $this->userName);
        $stmt->bindParam(":email", $this->email);
        $stmt->bindParam(":createTime", $this->createTime);
        $stmt->bindParam(":userToken", $this->userToken);

     
        // execute query
        $stmt->execute();
     
        return $stmt;
    }

    function delete(){
        // delete query
        $query = "DELETE FROM " . $this->table_name . " WHERE userID = :userID";
    
        // prepare query
        $stmt = $this->conn->prepare($query);
    
        // sanitize
        $this->userID=htmlspecialchars(strip_tags($this->userID));
    
        // bind id of record to delete
        $stmt->bindParam(":userID", $this->userID);
    
        // execute query
        if($stmt->execute()){
            return true;
        }
    
        return false;
    }

    function update(){
        // update query
        $query = "UPDATE
                    " . $this->table_name . "
                SET
                    userType=:userType, userName=:userName, email=:email, userToken=:userToken
                WHERE
                    userID=:userID";
    
        // prepare query statement
        $stmt = $this->conn->prepare($query);
    
        // sanitize
        $this->userID=htmlspecialchars(strip_tags($this->userID));
        $this->userType=htmlspecialchars(strip_tags($this->userType));
        $this->userName=htmlspecialchars(strip_tags($this->userName));
        $this->email=htmlspecialchars(strip_tags($this->email));
        $this->createTime=htmlspecialchars(strip_tags($this->createTime));
        $this->userToken=htmlspecialchars(strip_tags($this->userToken));

     
        // bind values
        $stmt->bindParam(":userID", $this->userID);
        $stmt->bindParam(":userType", $this->userType);
        $stmt->bindParam(":userName", $this->userName);
        $stmt->bindParam(":email", $this->email);
        $stmt->bindParam(":createTime", $this->createTime);
        $stmt->bindParam(":userToken", $this->userToken);

    
    
        // execute the query
        if($stmt->execute()){
            return true;
        }
    
        return false;
    }
}
?>