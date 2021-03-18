<?php
class DisasterEvents{
 
    // database connection and table name
    private $conn;
    private $table_name = "disasterEvents";
 
    // object properties
    public $disasterID;
    public $disasterType;
    public $emergencyLevel;
    public $latitudeStart;
    public $latitudeEnd;
    public $longitudeStart;
    public $longitudeEnd;
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
                    disasterID, disasterType, emergencyLevel, latitudeStart, latitudeEnd, longitudeStart, longitudeEnd, disasterDate, disasterBase, disasterName
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
                disasterType=:disasterType, emergencyLevel=:emergencyLevel, latitudeStart=:latitudeStart, latitudeEnd=:latitudeEnd, longitudeStart=:longitudeStart, longitudeEnd=:longitudeEnd, disasterDate=:disasterDate, disasterBase=:disasterBase, disasterName=:disasterName";
 
    // prepare query
    $stmt = $this->conn->prepare($query);
 
    // sanitize
    $this->disasterType=htmlspecialchars(strip_tags($this->disasterType));
    $this->emergencyLevel=htmlspecialchars(strip_tags($this->emergencyLevel));
    $this->latitudeStart=htmlspecialchars(strip_tags($this->latitudeStart));
    $this->latitudeEnd=htmlspecialchars(strip_tags($this->latitudeEnd));
    $this->longitudeStart=htmlspecialchars(strip_tags($this->longitudeStart));
    $this->longitudeEnd=htmlspecialchars(strip_tags($this->longitudeEnd));
    $this->disasterDate=htmlspecialchars(strip_tags($this->disasterDate));
    $this->disasterName=htmlspecialchars(strip_tags($this->disasterName));
 
    // bind values
    $stmt->bindParam(":disasterType", $this->disasterType);
    $stmt->bindParam(":emergencyLevel", $this->emergencyLevel);
    $stmt->bindParam(":latitudeStart", $this->latitudeStart);
    $stmt->bindParam(":latitudeEnd", $this->latitudeEnd);
    $stmt->bindParam(":longitudeStart", $this->longitudeStart);
    $stmt->bindParam(":longitudeEnd", $this->longitudeEnd);
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
        $query = "SELECT
                    disasterID, disasterType, emergencyLevel, latitudeStart, latitudeEnd, longitudeStart, longitudeEnd, disasterDate, disasterBase, disasterName
                FROM
                    " . $this->table_name . "
                WHERE
                    (disasterID = :disasterID OR :disasterID = '') 
                    AND (disasterType = :disasterType OR :disasterType = '')
                    AND (emergencyLevel = :emergencyLevel OR :emergencyLevel = '') 
                    AND (latitudeStart = :latitudeStart OR :latitudeStart = '')
                    AND (latitudeEnd = :latitudeEnd OR :latitudeEnd = '') 
                    AND (longitudeStart = :longitudeStart OR :longitudeStart = '')
                    AND (longitudeEnd = :longitudeEnd OR :longitudeEnd = '') 
                    AND (disasterDate = :disasterDate OR :disasterDate = '')
                    AND (disasterBase = :disasterBase OR :disasterBase = '')
                    AND (disasterName = :disasterName OR :disasterName = '')";
     
        // prepare query statement
        $stmt = $this->conn->prepare($query);
     

        // sanitize
        $this->disasterID=htmlspecialchars(strip_tags($this->disasterID));
        $this->disasterType=htmlspecialchars(strip_tags($this->disasterType));
        $this->emergencyLevel=htmlspecialchars(strip_tags($this->emergencyLevel));
        $this->latitudeStart=htmlspecialchars(strip_tags($this->latitudeStart));
        $this->latitudeEnd=htmlspecialchars(strip_tags($this->latitudeEnd));
        $this->longitudeStart=htmlspecialchars(strip_tags($this->longitudeStart));
        $this->longitudeEnd=htmlspecialchars(strip_tags($this->longitudeEnd));
        $this->disasterDate=htmlspecialchars(strip_tags($this->disasterDate));
        $this->disasterBase=htmlspecialchars(strip_tags($this->disasterBase));
        $this->disasterName=htmlspecialchars(strip_tags($this->disasterName));
    
        // bind values
        $stmt->bindParam(":disasterID", $this->disasterType);
        $stmt->bindParam(":disasterType", $this->disasterType);
        $stmt->bindParam(":emergencyLevel", $this->emergencyLevel);
        $stmt->bindParam(":latitudeStart", $this->latitudeStart);
        $stmt->bindParam(":latitudeEnd", $this->latitudeEnd);
        $stmt->bindParam(":longitudeStart", $this->longitudeStart);
        $stmt->bindParam(":longitudeEnd", $this->longitudeEnd);
        $stmt->bindParam(":disasterDate", $this->disasterDate);
        $stmt->bindParam(":disasterBase", $this->disasterBase);
        $stmt->bindParam(":disasterName", $this->disasterName);

     
        // execute query
        $stmt->execute();
     
        return $stmt;
    }
}
?>