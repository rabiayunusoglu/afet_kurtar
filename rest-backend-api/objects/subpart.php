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
 
    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    function read(){
 
        // select all query
        $query = "SELECT
                    subpartID, disasterID, latitude, longitude, [address], missingPerson, rescuedPerson, isOpenForVolunteers
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
                    disasterID=:disasterID, latitude=:latitude, longitude=:longitude, address=:address, missingPerson=:missingPerson, rescuedPerson=:rescuedPerson, isOpenForVolunteers=:isOpenForVolunteers";
     
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
    
     
        // bind values
        $stmt->bindParam(":disasterID", $this->disasterID);
        $stmt->bindParam(":latitude", $this->latitude);
        $stmt->bindParam(":longitude", $this->longitude);
        $stmt->bindParam(":address", $this->address);
        $stmt->bindParam(":missingPerson", $this->missingPerson);
        $stmt->bindParam(":rescuedPerson", $this->rescuedPerson);
        $stmt->bindParam(":isOpenForVolunteers", $this->isOpenForVolunteers);
    
     
        // execute query
        if($stmt->execute()){
            return true;
        }
     
        return false;
         
    }
}
?>