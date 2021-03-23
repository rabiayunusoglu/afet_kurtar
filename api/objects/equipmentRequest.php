<?php
class EquipmentRequest{
 
    // database connection and table name
    private $conn;
    private $table_name = "equipmentRequest";
 
    // object properties
    public $equipmentRequestID;
    public $quantity;
    public $equipmentID;
    public $teamRequestID;
 
    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    function read(){
 
        // select all query
        $query = "SELECT
                    equipmentRequestID, quantity,equipmentID, teamRequestID
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
                quantity=:quantity,equipmentID=:equipmentID, teamRequestID=:teamRequestID";
 
    // prepare query
    $stmt = $this->conn->prepare($query);
 
    // sanitize
    $this->quantity=htmlspecialchars(strip_tags($this->quantity));
    $this->equipmentID=htmlspecialchars(strip_tags($this->equipmentID));
    $this->teamRequestID=htmlspecialchars(strip_tags($this->teamRequestID));

 
    // bind values
    $stmt->bindParam(":quantity", $this->quantity);
    $stmt->bindParam(":equipmentID", $this->equipmentID);
    $stmt->bindParam(":teamRequestID", $this->teamRequestID);

 
    // execute query
    if($stmt->execute()){
        return true;
    }
 
    return false;
     
    }

    function search(){
        // select all query
        $query = "SELECT
                    equipmentRequestID, quantity,equipmentID, teamRequestID
                FROM
                    " . $this->table_name . "
                WHERE
                    (equipmentRequestID = :equipmentRequestID OR :equipmentRequestID = '') 
                    AND (quantity = :quantity OR :quantity = '')
                    AND (equipmentID = :equipmentID OR :equipmentID = '')
                    AND (teamRequestID = :teamRequestID OR :teamRequestID = '')";
     
        // prepare query statement
        $stmt = $this->conn->prepare($query);
     

        // sanitize
        $this->equipmentRequestID=htmlspecialchars(strip_tags($this->equipmentRequestID));
        $this->quantity=htmlspecialchars(strip_tags($this->quantity));
        $this->equipmentID=htmlspecialchars(strip_tags($this->equipmentID));
        $this->teamRequestID=htmlspecialchars(strip_tags($this->teamRequestID));

    
        // bind values
        $stmt->bindParam(":equipmentRequestID", $this->equipmentRequestID);
        $stmt->bindParam(":quantity", $this->quantity);
        $stmt->bindParam(":equipmentID", $this->equipmentID);
        $stmt->bindParam(":teamRequestID", $this->teamRequestID);
     
        // execute query
        $stmt->execute();
     
        return $stmt;
    }
}
?>