<?php
class VolunteerUser{
 
    // database connection and table name
    private $conn;
    private $table_name = "volunteerUser";
 
    // object properties
    public $volunteerID;
    public $volunteerName;
    public $role;
    public $assignedTeamID;
    public $latitude;
    public $longitude;
    public $address;
    public $isExperienced;
    public $haveFirstAidCert;
    public $requestedSubpart;
    public $responseSubpart;
    public $locationTime;
    public $tc;
    public $birthDate;
 
    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    function read(){
        // select all query
        $query = "SELECT
                    volunteerID, volunteerName, `role`, assignedTeamID, latitude, longitude, `address`, isExperienced, haveFirstAidCert, requestedSubpart, responseSubpart, locationTime, tc, birthDate
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
                volunteerID:=volunteerID, volunteerName=:volunteerName, `role`=:role, assignedTeamID=:assignedTeamID, latitude=:latitude, longitude=:longitude, `address`=:address, isExperienced=:isExperienced, haveFirstAidCert=:haveFirstAidCert, requestedSubpart=:requestedSubpart, responseSubpart=:responseSubpart";
     
        // prepare query
        $stmt = $this->conn->prepare($query);
     
        // sanitize
        $this->volunteerID=htmlspecialchars(strip_tags($this->volunteerID));
        $this->volunteerName=htmlspecialchars(strip_tags($this->volunteerName));
        $this->role=htmlspecialchars(strip_tags($this->role));
        $this->assignedTeamID=htmlspecialchars(strip_tags($this->assignedTeamID));
        $this->latitude=htmlspecialchars(strip_tags($this->latitude));
        $this->longitude=htmlspecialchars(strip_tags($this->longitude));
        $this->address=htmlspecialchars(strip_tags($this->address));
        $this->isExperienced=htmlspecialchars(strip_tags($this->isExperienced));
        $this->haveFirstAidCert=htmlspecialchars(strip_tags($this->haveFirstAidCert));
        $this->requestedSubpart=htmlspecialchars(strip_tags($this->requestedSubpart));
        $this->responseSubpart=htmlspecialchars(strip_tags($this->responseSubpart));
        $this->locationTime=htmlspecialchars(strip_tags($this->locationTime));
        $this->tc=htmlspecialchars(strip_tags($this->tc));
        $this->birthDate=htmlspecialchars(strip_tags($this->birthDate));
    
     
        // bind values
        $stmt->bindParam(":volunteerID", $this->volunteerID);
        $stmt->bindParam(":volunteerName", $this->volunteerName);
        $stmt->bindParam(":role", $this->role);
        $stmt->bindParam(":assignedTeamID", $this->assignedTeamID);
        $stmt->bindParam(":latitude", $this->latitude);
        $stmt->bindParam(":longitude", $this->longitude);
        $stmt->bindParam(":address", $this->address);
        $stmt->bindParam(":isExperienced", $this->isExperienced);
        $stmt->bindParam(":haveFirstAidCert", $this->haveFirstAidCert);
        $stmt->bindParam(":requestedSubpart", $this->requestedSubpart);
        $stmt->bindParam(":responseSubpart", $this->responseSubpart);
        $stmt->bindParam(":locationTime", $this->locationTime);
        $stmt->bindParam(":tc", $this->tc);
        $stmt->bindParam(":birthDate", $this->birthDate);
    
     
        // execute query
        if($stmt->execute()){
            return true;
        }
     
        return false;
         
    }

    function search(){
        // select all query
        $query = "SELECT
                    volunteerID, volunteerName, `role`, assignedTeamID, latitude, longitude, `address`, isExperienced, haveFirstAidCert, requestedSubpart, responseSubpart, locationTime, tc, birthDate
                FROM
                    " . $this->table_name . "
                WHERE
                    (volunteerID = :volunteerID OR :volunteerID = '') 
                    AND (volunteerName = :volunteerName OR :volunteerName = '')
                    AND (`role` = :role OR :role = '')
                    AND (assignedTeamID = :assignedTeamID OR :assignedTeamID = '')
                    AND (latitude = :latitude OR :latitude = '')
                    AND (longitude = :longitude OR :longitude = '')
                    AND (`address` = :address OR :address = '')
                    AND (isExperienced = :isExperienced OR :isExperienced = '')
                    AND (haveFirstAidCert = :haveFirstAidCert OR :haveFirstAidCert = '')
                    AND (requestedSubpart = :requestedSubpart OR :requestedSubpart = '')
                    AND (responseSubpart = :responseSubpart OR :responseSubpart = '')
                    AND (locationTime = :locationTime OR :locationTime = '')
                    AND (tc = :tc OR :tc = '')
                    AND (birthDate = :birthDate OR :birthDate = '')";
     
        // prepare query statement
        $stmt = $this->conn->prepare($query);
     

        // sanitize
        $this->volunteerID=htmlspecialchars(strip_tags($this->volunteerID));
        $this->volunteerName=htmlspecialchars(strip_tags($this->volunteerName));
        $this->role=htmlspecialchars(strip_tags($this->role));
        $this->assignedTeamID=htmlspecialchars(strip_tags($this->assignedTeamID));
        $this->latitude=htmlspecialchars(strip_tags($this->latitude));
        $this->longitude=htmlspecialchars(strip_tags($this->longitude));
        $this->address=htmlspecialchars(strip_tags($this->address));
        $this->isExperienced=htmlspecialchars(strip_tags($this->isExperienced));
        $this->haveFirstAidCert=htmlspecialchars(strip_tags($this->haveFirstAidCert));
        $this->requestedSubpart=htmlspecialchars(strip_tags($this->requestedSubpart));
        $this->responseSubpart=htmlspecialchars(strip_tags($this->responseSubpart));
        $this->locationTime=htmlspecialchars(strip_tags($this->locationTime));
        $this->tc=htmlspecialchars(strip_tags($this->tc));
        $this->birthDate=htmlspecialchars(strip_tags($this->birthDate));
    
     
        // bind values
        $stmt->bindParam(":volunteerID", $this->volunteerID);
        $stmt->bindParam(":volunteerName", $this->volunteerName);
        $stmt->bindParam(":role", $this->role);
        $stmt->bindParam(":assignedTeamID", $this->assignedTeamID);
        $stmt->bindParam(":latitude", $this->latitude);
        $stmt->bindParam(":longitude", $this->longitude);
        $stmt->bindParam(":address", $this->address);
        $stmt->bindParam(":isExperienced", $this->isExperienced);
        $stmt->bindParam(":haveFirstAidCert", $this->haveFirstAidCert);
        $stmt->bindParam(":requestedSubpart", $this->requestedSubpart);
        $stmt->bindParam(":responseSubpart", $this->responseSubpart);
        $stmt->bindParam(":locationTime", $this->locationTime);
        $stmt->bindParam(":tc", $this->tc);
        $stmt->bindParam(":birthDate", $this->birthDate);
     
        // execute query
        $stmt->execute();
     
        return $stmt;
    }
}
?>