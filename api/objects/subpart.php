<?php
class Subpart{
 
    // database connection and table name
    private $conn;
    private $table_name = "subpart";
 
    // object properties
    public $subpartID;
    public $disasterID;
    public $latitude;
    public $longitude;
    public $address;
    public $missingPerson;
    public $rescuedPerson;
    public $isOpenForVolunteers;
    public $subpartName;
    public $disasterName;
    public $status;
    public $emergencyLevel;
 
    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    function read(){
 
        // select all query
        $query = "SELECT
                    subpartID, disasterID, latitude, longitude, `address`, missingPerson, rescuedPerson, isOpenForVolunteers, subpartName,disasterName, `status`, emergencyLevel
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
                    disasterID=:disasterID, latitude=:latitude, longitude=:longitude, address=:address, missingPerson=:missingPerson, rescuedPerson=:rescuedPerson, isOpenForVolunteers=:isOpenForVolunteers, subpartName=:subpartName,disasterName=:disasterName, `status`=:status, emergencyLevel=:emergencyLevel";
     
        // prepare query
        $stmt = $this->conn->prepare($query);
     
        // sanitize
        $this->disasterID=htmlspecialchars(strip_tags($this->disasterID));
        $this->latitude=htmlspecialchars(strip_tags($this->latitude));
        $this->longitude=htmlspecialchars(strip_tags($this->longitude));
        $this->address=htmlspecialchars(strip_tags($this->address));
        $this->missingPerson=htmlspecialchars(strip_tags($this->missingPerson));
        $this->rescuedPerson=htmlspecialchars(strip_tags($this->rescuedPerson));
        $this->isOpenForVolunteers=htmlspecialchars(strip_tags($this->isOpenForVolunteers));
        $this->subpartName=htmlspecialchars(strip_tags($this->subpartName));
	$this->disasterName=htmlspecialchars(strip_tags($this->disasterName));
        $this->status=htmlspecialchars(strip_tags($this->status));
        $this->emergencyLevel=htmlspecialchars(strip_tags($this->emergencyLevel));
     
        // bind values
        $stmt->bindParam(":disasterID", $this->disasterID);
        $stmt->bindParam(":latitude", $this->latitude);
        $stmt->bindParam(":longitude", $this->longitude);
        $stmt->bindParam(":address", $this->address);
        $stmt->bindParam(":missingPerson", $this->missingPerson);
        $stmt->bindParam(":rescuedPerson", $this->rescuedPerson);
        $stmt->bindParam(":isOpenForVolunteers", $this->isOpenForVolunteers);
        $stmt->bindParam(":subpartName", $this->subpartName);
	$stmt->bindParam(":disasterName", $this->disasterName);
        $stmt->bindParam(":status", $this->status);
        $stmt->bindParam(":emergencyLevel", $this->emergencyLevel);
     
        // execute query
        if($stmt->execute()){
            return true;
        }
     
        return false;
         
    }

    function search(){
        // select all query
        $query = "SELECT
                    subpartID, disasterID, latitude, longitude, `address`, missingPerson, rescuedPerson, isOpenForVolunteers, subpartName, disasterName, `status`, emergencyLevel
                FROM
                    " . $this->table_name . "
                WHERE
                    (subpartID = :subpartID OR :subpartID = '') 
                    AND (disasterID = :disasterID OR :disasterID = '')
                    AND (latitude = :latitude OR :latitude = '')
                    AND (longitude = :longitude OR :longitude = '')
                    AND (`address` = :address OR :address = '')
                    AND (missingPerson = :missingPerson OR :missingPerson = '')
                    AND (rescuedPerson = :rescuedPerson OR :rescuedPerson = '')
                    AND (isOpenForVolunteers = :isOpenForVolunteers OR :isOpenForVolunteers = '')
                    AND (subpartName = :subpartName OR :subpartName = '')
AND (disasterName = :disasterName OR :disasterName = '')
                    AND (`status` = :status OR :status = '')
                    AND (emergencyLevel = :emergencyLevel OR :emergencyLevel = '')";
     
        // prepare query statement
        $stmt = $this->conn->prepare($query);
     

        // sanitize
        $this->subpartID=htmlspecialchars(strip_tags($this->subpartID));
        $this->disasterID=htmlspecialchars(strip_tags($this->disasterID));
        $this->latitude=htmlspecialchars(strip_tags($this->latitude));
        $this->longitude=htmlspecialchars(strip_tags($this->longitude));
        $this->address=htmlspecialchars(strip_tags($this->address));
        $this->missingPerson=htmlspecialchars(strip_tags($this->missingPerson));
        $this->rescuedPerson=htmlspecialchars(strip_tags($this->rescuedPerson));
        $this->isOpenForVolunteers=htmlspecialchars(strip_tags($this->isOpenForVolunteers));
        $this->subpartName=htmlspecialchars(strip_tags($this->subpartName));
	$this->disasterName=htmlspecialchars(strip_tags($this->disasterName));
        $this->status=htmlspecialchars(strip_tags($this->status));
        $this->emergencyLevel=htmlspecialchars(strip_tags($this->emergencyLevel));
    
     
        // bind values
        $stmt->bindParam(":subpartID", $this->subpartID);
        $stmt->bindParam(":disasterID", $this->disasterID);
        $stmt->bindParam(":latitude", $this->latitude);
        $stmt->bindParam(":longitude", $this->longitude);
        $stmt->bindParam(":address", $this->address);
        $stmt->bindParam(":missingPerson", $this->missingPerson);
        $stmt->bindParam(":rescuedPerson", $this->rescuedPerson);
        $stmt->bindParam(":isOpenForVolunteers", $this->isOpenForVolunteers);
        $stmt->bindParam(":subpartName", $this->subpartName);
	$stmt->bindParam(":disasterName", $this->disasterName);
        $stmt->bindParam(":status", $this->status);
        $stmt->bindParam(":emergencyLevel", $this->emergencyLevel);
     
        // execute query
        $stmt->execute();
     
        return $stmt;
    }
}
?>