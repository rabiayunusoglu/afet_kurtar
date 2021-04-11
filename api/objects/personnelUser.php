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
    public $personnelEmail;
    public $personnelRole;
    public $teamID;
    public $locationTime;
    
    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    function read(){
 
        // select all query
        $query = "SELECT
                    personnelID, institution, latitude, longitude, personnelName,personnelEmail, personnelRole, teamID, locationTime
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
                personnelID=:personnelID, institution=:institution, latitude=:latitude, longitude=:longitude, personnelName=:personnelName,personnelEmail=:personnelEmail, personnelRole=:personnelRole, teamID=:teamID, locationTime=:locationTime";
     
        // prepare query
        $stmt = $this->conn->prepare($query);
     
        // sanitize
        $this->personnelID=htmlspecialchars(strip_tags($this->personnelID));
        $this->institution=htmlspecialchars(strip_tags($this->institution));
        $this->latitude=htmlspecialchars(strip_tags($this->latitude));
        $this->longitude=htmlspecialchars(strip_tags($this->longitude));
        $this->personnelName=htmlspecialchars(strip_tags($this->personnelName));
        $this->personnelEmail=htmlspecialchars(strip_tags($this->personnelEmail));
        $this->personnelRole=htmlspecialchars(strip_tags($this->personnelRole));
        $this->teamID=htmlspecialchars(strip_tags($this->teamID));
        $this->locationTime=htmlspecialchars(strip_tags($this->locationTime));
    
     
        // bind values
        $stmt->bindParam(":personnelID", $this->personnelID);
        $stmt->bindParam(":institution", $this->institution);
        $stmt->bindParam(":latitude", $this->latitude);
        $stmt->bindParam(":longitude", $this->longitude);
        $stmt->bindParam(":personnelName", $this->personnelName);
        $stmt->bindParam(":personnelEmail", $this->personnelEmail);
        $stmt->bindParam(":personnelRole", $this->personnelRole);
        $stmt->bindParam(":teamID", $this->teamID);
        $stmt->bindParam(":locationTime", $this->locationTime);
    
     
        // execute query
        if($stmt->execute()){
            return true;
        }
     
        return false;
         
    }

    function search(){
        // select all query
        $query = "SELECT
                    personnelID, institution, latitude, longitude, personnelName,personnelEmail, personnelRole, teamID, locationTime
                FROM
                    " . $this->table_name . "
                WHERE
                    (personnelID = :personnelID OR :personnelID = '') 
                    AND (institution = :institution OR :institution = '')
                    AND (latitude = :latitude OR :latitude = '')
                    AND (longitude = :longitude OR :longitude = '')
                    AND (personnelName = :personnelName OR :personnelName = '')
                    AND (personnelEmail = :personnelEmail OR :personnelEmail = '')
                    AND (personnelRole = :personnelRole OR :personnelRole = '')
                    AND (teamID = :teamID OR :teamID = '')
                    AND (locationTime = :locationTime OR :locationTime = '')";
     
        // prepare query statement
        $stmt = $this->conn->prepare($query);
     

        // sanitize
        $this->personnelID=htmlspecialchars(strip_tags($this->personnelID));
        $this->institution=htmlspecialchars(strip_tags($this->institution));
        $this->latitude=htmlspecialchars(strip_tags($this->latitude));
        $this->longitude=htmlspecialchars(strip_tags($this->longitude));
        $this->personnelName=htmlspecialchars(strip_tags($this->personnelName));
        $this->personnelEmail=htmlspecialchars(strip_tags($this->personnelEmail));
        $this->personnelRole=htmlspecialchars(strip_tags($this->personnelRole));
        $this->teamID=htmlspecialchars(strip_tags($this->teamID));
        $this->locationTime=htmlspecialchars(strip_tags($this->locationTime));
    
     
        // bind values
        $stmt->bindParam(":personnelID", $this->personnelID);
        $stmt->bindParam(":institution", $this->institution);
        $stmt->bindParam(":latitude", $this->latitude);
        $stmt->bindParam(":longitude", $this->longitude);
        $stmt->bindParam(":personnelName", $this->personnelName);
        $stmt->bindParam(":personnelEmail", $this->personnelEmail);
        $stmt->bindParam(":personnelRole", $this->personnelRole);
        $stmt->bindParam(":teamID", $this->teamID);
        $stmt->bindParam(":locationTime", $this->locationTime);
     
        // execute query
        $stmt->execute();
     
        return $stmt;
    }

    function delete(){
        // delete query
        $query = "DELETE FROM " . $this->table_name . " WHERE personnelID = :personnelID";
    
        // prepare query
        $stmt = $this->conn->prepare($query);
    
        // sanitize
        $this->personnelID=htmlspecialchars(strip_tags($this->personnelID));
    
        // bind id of record to delete
        $stmt->bindParam(":personnelID", $this->personnelID);
    
        // execute query
        if($stmt->execute()){
            return true;
        }
    
        return false;
    }

    function update(){

        $setContent = "";

        if($this->institution != ""){
            $setContent .= "institution=:institution,";
        }
        if($this->latitude != ""){
            $setContent .= "latitude=:latitude,";
        }
        if($this->longitude != ""){
            $setContent .= "longitude=:longitude,";
        }
        if($this->personnelName != ""){
            $setContent .= "personnelName=:personnelName,";
        }
        if($this->personnelEmail != ""){
            $setContent .= "personnelEmail=:personnelEmail,";
        }
        if($this->personnelRole != ""){
            $setContent .= "personnelRole=:personnelRole,";
        }
        if($this->teamID != ""){
            $setContent .= "teamID=:teamID,";
        }
        if($this->locationTime != ""){
            $setContent .= "locationTime=:locationTime,";
        }

        $setContent  = rtrim($setContent, ",");

        // update query
        $query = "UPDATE
                    " . $this->table_name . "
                SET
                    " . $setContent . "
                WHERE
                    personnelID=:personnelID";
    
        // prepare query statement
        $stmt = $this->conn->prepare($query);
    
        // sanitize
        $this->personnelID=htmlspecialchars(strip_tags($this->personnelID));
        $this->institution=htmlspecialchars(strip_tags($this->institution));
        $this->latitude=htmlspecialchars(strip_tags($this->latitude));
        $this->longitude=htmlspecialchars(strip_tags($this->longitude));
        $this->personnelName=htmlspecialchars(strip_tags($this->personnelName));
        $this->personnelEmail=htmlspecialchars(strip_tags($this->personnelEmail));
        $this->personnelRole=htmlspecialchars(strip_tags($this->personnelRole));
        $this->teamID=htmlspecialchars(strip_tags($this->teamID));
        $this->locationTime=htmlspecialchars(strip_tags($this->locationTime));
    
        if($this->institution != ""){
            $stmt->bindParam(":institution", $this->institution);
        }
        if($this->latitude != ""){
            $stmt->bindParam(":latitude", $this->latitude);
        }
        if($this->longitude != ""){
            $stmt->bindParam(":longitude", $this->longitude);
        }
        if($this->personnelName != ""){
            $stmt->bindParam(":personnelName", $this->personnelName);
        }
        if($this->personnelEmail != ""){
            $stmt->bindParam(":personnelEmail", $this->personnelEmail);
        }
        if($this->personnelRole != ""){
            $stmt->bindParam(":personnelRole", $this->personnelRole);
        }
        if($this->teamID != ""){
            $stmt->bindParam(":teamID", $this->teamID);
        }
        if($this->locationTime != ""){
            $stmt->bindParam(":locationTime", $this->locationTime);
        }

        // bind values
        $stmt->bindParam(":personnelID", $this->personnelID);
    
    
        // execute the query
        if($stmt->execute()){
            return true;
        }
    
        return false;
    }
}
?>