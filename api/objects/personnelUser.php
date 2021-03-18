<?php
class PersonnelUser{
 
    // database connection and table name
    private $conn;
    private $table_name = "personnelUser";
 
    // object properties
    public $personnelID;
    public $institution;
    public $latitude;
    public $longitude;
    public $personnelName;
    public $personnelRole;
    public $teamID;
    
    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    function read(){
 
        // select all query
        $query = "SELECT
                    personnelID, institution, latitude, longitude, personnelName, personnelRole, teamID
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
                personnelID=:personnelID, institution=:institution, latitude=:latitude, longitude=:longitude, personnelName=:personnelName, personnelRole=:personnelRole, teamID=:teamID";
     
        // prepare query
        $stmt = $this->conn->prepare($query);
     
        // sanitize
        $this->personnelID=htmlspecialchars(strip_tags($this->personnelID));
        $this->institution=htmlspecialchars(strip_tags($this->institution));
        $this->latitude=htmlspecialchars(strip_tags($this->latitude));
        $this->longitude=htmlspecialchars(strip_tags($this->longitude));
        $this->personnelName=htmlspecialchars(strip_tags($this->personnelName));
        $this->personnelRole=htmlspecialchars(strip_tags($this->personnelRole));
        $this->teamID=htmlspecialchars(strip_tags($this->teamID));
    
     
        // bind values
        $stmt->bindParam(":personnelID", $this->personnelID);
        $stmt->bindParam(":institution", $this->institution);
        $stmt->bindParam(":latitude", $this->latitude);
        $stmt->bindParam(":longitude", $this->longitude);
        $stmt->bindParam(":personnelName", $this->personnelName);
        $stmt->bindParam(":personnelRole", $this->personnelRole);
        $stmt->bindParam(":teamID", $this->teamID);
    
     
        // execute query
        if($stmt->execute()){
            return true;
        }
     
        return false;
         
    }

    function search(){
        // select all query
        $query = "SELECT
                    personnelID, institution, latitude, longitude, personnelName, personnelRole, teamID
                FROM
                    " . $this->table_name . "
                WHERE
                    (personnelID = :personnelID OR :personnelID = '') 
                    AND (institution = :institution OR :institution = '')
                    AND (latitude = :latitude OR :latitude = '')
                    AND (longitude = :longitude OR :longitude = '')
                    AND (personnelName = :personnelName OR :personnelName = '')
                    AND (personnelRole = :personnelRole OR :personnelRole = '')
                    AND (teamID = :teamID OR :teamID = '')";
     
        // prepare query statement
        $stmt = $this->conn->prepare($query);
     

        // sanitize
        $this->personnelID=htmlspecialchars(strip_tags($this->personnelID));
        $this->institution=htmlspecialchars(strip_tags($this->institution));
        $this->latitude=htmlspecialchars(strip_tags($this->latitude));
        $this->longitude=htmlspecialchars(strip_tags($this->longitude));
        $this->personnelName=htmlspecialchars(strip_tags($this->personnelName));
        $this->personnelRole=htmlspecialchars(strip_tags($this->personnelRole));
        $this->teamID=htmlspecialchars(strip_tags($this->teamID));
    
     
        // bind values
        $stmt->bindParam(":personnelID", $this->personnelID);
        $stmt->bindParam(":institution", $this->institution);
        $stmt->bindParam(":latitude", $this->latitude);
        $stmt->bindParam(":longitude", $this->longitude);
        $stmt->bindParam(":personnelName", $this->personnelName);
        $stmt->bindParam(":personnelRole", $this->personnelRole);
        $stmt->bindParam(":teamID", $this->teamID);
     
        // execute query
        $stmt->execute();
     
        return $stmt;
    }
}
?>