<?php
class DisasterEvents{
 
    // database connection and table name
    private $conn;
    private $table_name = "disasterEvents";
 
    // object properties
    public $disasterID;
    public $disasterType;
    public $emergencyLevel;
    public $latitude;
    public $longitude;
    public $disasterDate;
    public $disasterBase;
    public $disasterName;
 
    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    function read(){
 
        // select all query
        $query = "SELECT
                    disasterID, disasterType, emergencyLevel, latitude, longitude, disasterDate, disasterBase, disasterName
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
                disasterType=:disasterType, emergencyLevel=:emergencyLevel, latitude=:latitude, longitude=:longitude, disasterDate=:disasterDate, disasterBase=:disasterBase, disasterName=:disasterName";
 
    // prepare query
    $stmt = $this->conn->prepare($query);
 
    // sanitize
    $this->disasterType=htmlspecialchars(strip_tags($this->disasterType));
    $this->emergencyLevel=htmlspecialchars(strip_tags($this->emergencyLevel));
    $this->latitude=htmlspecialchars(strip_tags($this->latitude));
    $this->longitude=htmlspecialchars(strip_tags($this->longitude));
    $this->disasterDate=htmlspecialchars(strip_tags($this->disasterDate));
    $this->disasterBase=htmlspecialchars(strip_tags($this->disasterBase));
    $this->disasterName=htmlspecialchars(strip_tags($this->disasterName));
 
    // bind values
    $stmt->bindParam(":disasterType", $this->disasterType);
    $stmt->bindParam(":emergencyLevel", $this->emergencyLevel);
    $stmt->bindParam(":latitude", $this->latitude);
    $stmt->bindParam(":longitude", $this->longitude);
    $stmt->bindParam(":disasterDate", $this->disasterDate);
    $stmt->bindParam(":disasterBase", $this->disasterBase);
    $stmt->bindParam(":disasterName", $this->disasterName);

 
    // execute query
    if($stmt->execute()){
        return true;
    }
 
    return false;
     
    }

    function search(){
        // select all query
        // AND (latitude = :latitude OR :latitude = '')
        // AND (longitude = :longitude OR :longitude = '')
        $query = "SELECT
                    disasterID, disasterType, emergencyLevel, latitude, longitude,  disasterDate, disasterBase, disasterName
                FROM
                    " . $this->table_name . "
                WHERE
                    (disasterID = :disasterID OR :disasterID = '') 
                    AND (disasterType = :disasterType OR :disasterType = '')
                    AND (emergencyLevel = :emergencyLevel OR :emergencyLevel = '') 
                    AND (disasterDate = :disasterDate OR :disasterDate = '')
                    AND (disasterBase = :disasterBase OR :disasterBase = '')
                    AND (disasterName = :disasterName OR :disasterName = '')";
     
        // prepare query statement
        $stmt = $this->conn->prepare($query);
     

        // sanitize
        $this->disasterID=htmlspecialchars(strip_tags($this->disasterID));
        $this->disasterType=htmlspecialchars(strip_tags($this->disasterType));
        $this->emergencyLevel=htmlspecialchars(strip_tags($this->emergencyLevel));
        $this->latitude=htmlspecialchars(strip_tags($this->latitude));
        $this->longitude=htmlspecialchars(strip_tags($this->longitude));
        $this->disasterDate=htmlspecialchars(strip_tags($this->disasterDate));
        $this->disasterBase=htmlspecialchars(strip_tags($this->disasterBase));
        $this->disasterName=htmlspecialchars(strip_tags($this->disasterName));
    
        // bind values
        $stmt->bindParam(":disasterID", $this->disasterID);
        $stmt->bindParam(":disasterType", $this->disasterType);
        $stmt->bindParam(":emergencyLevel", $this->emergencyLevel);
        $stmt->bindParam(":latitude", $this->latitude);
        $stmt->bindParam(":longitude", $this->longitude);
        $stmt->bindParam(":disasterDate", $this->disasterDate);
        $stmt->bindParam(":disasterBase", $this->disasterBase);
        $stmt->bindParam(":disasterName", $this->disasterName);

     
        // execute query
        $stmt->execute();
     
        return $stmt;
    }

    function delete(){
        // delete query
        $query = "DELETE FROM " . $this->table_name . " WHERE disasterID = :disasterID";
    
        // prepare query
        $stmt = $this->conn->prepare($query);
    
        // sanitize
        $this->disasterID=htmlspecialchars(strip_tags($this->disasterID));
    
        // bind id of record to delete
        $stmt->bindParam(":disasterID", $this->disasterID);
    
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
                    disasterType=:disasterType, emergencyLevel=:emergencyLevel, latitude=:latitude, longitude=:longitude, disasterDate=:disasterDate, disasterBase=:disasterBase, disasterName=:disasterName
                WHERE
                    disasterID=:disasterID";
    
        // prepare query statement
        $stmt = $this->conn->prepare($query);
    
        // sanitize
        $this->disasterID=htmlspecialchars(strip_tags($this->disasterID));
        $this->disasterType=htmlspecialchars(strip_tags($this->disasterType));
        $this->emergencyLevel=htmlspecialchars(strip_tags($this->emergencyLevel));
        $this->latitude=htmlspecialchars(strip_tags($this->latitude));
        $this->longitude=htmlspecialchars(strip_tags($this->longitude));
        $this->disasterDate=htmlspecialchars(strip_tags($this->disasterDate));
        $this->disasterBase=htmlspecialchars(strip_tags($this->disasterBase));
        $this->disasterName=htmlspecialchars(strip_tags($this->disasterName));
    
        // bind values
        $stmt->bindParam(":disasterID", $this->disasterID);
        $stmt->bindParam(":disasterType", $this->disasterType);
        $stmt->bindParam(":emergencyLevel", $this->emergencyLevel);
        $stmt->bindParam(":latitude", $this->latitude);
        $stmt->bindParam(":longitude", $this->longitude);
        $stmt->bindParam(":disasterDate", $this->disasterDate);
        $stmt->bindParam(":disasterBase", $this->disasterBase);
        $stmt->bindParam(":disasterName", $this->disasterName);
    
    
        // execute the query
        if($stmt->execute()){
            return true;
        }
    
        return false;
    }
}
?>