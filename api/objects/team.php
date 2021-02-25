<?php
class Team{
 
    // database connection and table name
    private $conn;
    private $table_name = "team";
 
    // object properties
    public $teamID;
    public $assignedSubpartID;
    public $status;
    public $needManPower;
    public $needEquipment;
 
    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    function read(){
 
        // select all query
        $query = "SELECT
                    teamID, assignedSubpartID, [status], needManPower, needEquipment
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
                    assignedSubpartID=:assignedSubpartID, status=:status, needManPower=:needManPower, needEquipment=:needEquipment";
     
        // prepare query
        $stmt = $this->conn->prepare($query);
     
        // sanitize
        $this->assignedSubpartID=htmlspecialchars(strip_tags($this->assignedSubpartID));
        $this->status=htmlspecialchars(strip_tags($this->status));
        $this->needManPower=htmlspecialchars(strip_tags($this->needManPower));
        $this->needEquipment=htmlspecialchars(strip_tags($this->needEquipment));
    
     
        // bind values
        $stmt->bindParam(":assignedSubpartID", $this->assignedSubpartID);
        $stmt->bindParam(":status", $this->status);
        $stmt->bindParam(":needManPower", $this->needManPower);
        $stmt->bindParam(":needEquipment", $this->needEquipment);
    
     
        // execute query
        if($stmt->execute()){
            return true;
        }
     
        return false;
         
    }
}
?>