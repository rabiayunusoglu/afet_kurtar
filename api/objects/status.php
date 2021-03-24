<?php
class Status{
 
    // database connection and table name
    private $conn;
    private $table_name = "status";
 
    // object properties
    public $statusID;
    public $statusMessage;
    public $teamID;
    public $subpartID;
    public $statusTime;
    
 
    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    function read(){
 
        // select all query
        $query = "SELECT
                    statusID, statusMessage, teamID,subpartID ,statusTime
                FROM
                    " . $this->table_name . " ";
     
        // prepare query statement
        $stmt = $this->conn->prepare($query);
     
        // execute query
        $stmt->execute();
     
        return $stmt;
    }

    // create product
    function create(){
 
    // query to insert record
    $query = "INSERT INTO
                " . $this->table_name . "
            SET
                statusMessage=:statusMessage, teamID=:teamID, subpartID=:subpartID";
 
    // prepare query
    $stmt = $this->conn->prepare($query);
 
    // sanitize
    $this->statusMessage=htmlspecialchars(strip_tags($this->statusMessage));
    $this->teamID=htmlspecialchars(strip_tags($this->teamID));
    $this->subpartID=htmlspecialchars(strip_tags($this->subpartID));

 
    // bind values
    $stmt->bindParam(":statusMessage", $this->statusMessage);
    $stmt->bindParam(":teamID", $this->teamID);
    $stmt->bindParam(":subpartID", $this->subpartID);

 
    // execute query
    if($stmt->execute()){
        return true;
    }
 
    return false;
     
    }

    function search(){
        // select all query
        $query = "SELECT
                    statusID, statusMessage, teamID,subpartID, statusTime
                FROM
                    " . $this->table_name . "
                WHERE
                    (statusID = :statusID OR :statusID = '') 
                    AND (statusMessage = :statusMessage OR :statusMessage = '')
                    AND (teamID = :teamID OR :teamID = '')
                    AND (subpartID = :subpartID OR :subpartID = '')
                    AND (statusTime = :statusTime OR :statusTime = '')";
     
        // prepare query statement
        $stmt = $this->conn->prepare($query);
     

        // sanitize
        $this->statusID=htmlspecialchars(strip_tags($this->statusID));
        $this->statusMessage=htmlspecialchars(strip_tags($this->statusMessage));
        $this->teamID=htmlspecialchars(strip_tags($this->teamID));
        $this->subpartID=htmlspecialchars(strip_tags($this->subpartID));
        $this->statusTime=htmlspecialchars(strip_tags($this->statusTime));

    
        // bind values
        $stmt->bindParam(":statusID", $this->statusID);
        $stmt->bindParam(":statusMessage", $this->statusMessage);
        $stmt->bindParam(":teamID", $this->teamID);
        $stmt->bindParam(":subpartID", $this->subpartID);
        $stmt->bindParam(":statusTime", $this->statusTime);
     
        // execute query
        $stmt->execute();
     
        return $stmt;
    }

    function delete(){
        // delete query
        $query = "DELETE FROM " . $this->table_name . " WHERE statusID = :statusID";
    
        // prepare query
        $stmt = $this->conn->prepare($query);
    
        // sanitize
        $this->statusID=htmlspecialchars(strip_tags($this->statusID));
    
        // bind id of record to delete
        $stmt->bindParam(":statusID", $this->statusID);
    
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
                    statusMessage=:statusMessage, teamID=:teamID, subpartID=:subpartID
                WHERE
                    statusID=:statusID";
    
        // prepare query statement
        $stmt = $this->conn->prepare($query);
    
        // sanitize
        $this->statusID=htmlspecialchars(strip_tags($this->statusID));
        $this->statusMessage=htmlspecialchars(strip_tags($this->statusMessage));
        $this->teamID=htmlspecialchars(strip_tags($this->teamID));
        $this->subpartID=htmlspecialchars(strip_tags($this->subpartID));
        $this->statusTime=htmlspecialchars(strip_tags($this->statusTime));
    
        // bind values
        $stmt->bindParam(":statusID", $this->statusID);
        $stmt->bindParam(":statusMessage", $this->statusMessage);
        $stmt->bindParam(":teamID", $this->teamID);
        $stmt->bindParam(":subpartID", $this->subpartID);
        $stmt->bindParam(":statusTime", $this->statusTime);
    
    
        // execute the query
        if($stmt->execute()){
            return true;
        }
    
        return false;
    }
}
?>