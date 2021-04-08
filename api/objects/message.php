<?php
class Message{
 
    // database connection and table name
    private $conn;
    private $table_name = "message";
 
    // object properties
    public $messageID;
    public $teamID;
    public $userID;
    public $messageData;
    public $messageName;
    public $messageTime;
  
 
    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    function read(){
 
        // select all query
        $query = "SELECT
                    messageID, teamID, userID, messageData,messageName, messageTime
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
                    teamID=:teamID, userID=:userID, messageData=:messageData,messageName=:messageName";
     
        // prepare query
        $stmt = $this->conn->prepare($query);
     
        // sanitize
        $this->teamID=htmlspecialchars(strip_tags($this->teamID));
        $this->userID=htmlspecialchars(strip_tags($this->userID));
        $this->messageData=htmlspecialchars(strip_tags($this->messageData));
        $this->messageName=htmlspecialchars(strip_tags($this->messageName));

    
     
        // bind values
        $stmt->bindParam(":teamID", $this->teamID);
        $stmt->bindParam(":userID", $this->userID);
        $stmt->bindParam(":messageData", $this->messageData);
        $stmt->bindParam(":messageName", $this->messageName);
  
    
     
        // execute query
        if($stmt->execute()){
            return true;
        }
     
        return false;
         
    }

    function search(){
        // select all query
        $query = "SELECT
                    messageID, teamID, userID, messageData,messageName, messageTime
                FROM
                    " . $this->table_name . "
                WHERE
                    (messageID = :messageID OR :messageID = '') 
                    AND (teamID = :teamID OR :teamID = '') 
                    AND (userID = :userID OR :userID = '') 
                    AND (messageData = :messageData OR :messageData = '')
                     AND (messageName = :messageName OR :messageName = '')
                    AND (messageTime = :messageTime OR :messageTime = '')";
     
        // prepare query statement
        $stmt = $this->conn->prepare($query);
     

        // sanitize
        $this->messageID=htmlspecialchars(strip_tags($this->messageID));
        $this->teamID=htmlspecialchars(strip_tags($this->teamID));
        $this->userID=htmlspecialchars(strip_tags($this->userID));
        $this->messageData=htmlspecialchars(strip_tags($this->messageData));
         $this->messageName=htmlspecialchars(strip_tags($this->messageName));
        $this->messageTime=htmlspecialchars(strip_tags($this->messageTime));

     
        // bind values
        $stmt->bindParam(":messageID", $this->messageID);
        $stmt->bindParam(":teamID", $this->teamID);
        $stmt->bindParam(":userID", $this->userID);
        $stmt->bindParam(":messageData", $this->messageData);
         $stmt->bindParam(":messageName", $this->messageName);
        $stmt->bindParam(":messageTime", $this->messageTime);

     
        // execute query
        $stmt->execute();
     
        return $stmt;
    }

}
?>