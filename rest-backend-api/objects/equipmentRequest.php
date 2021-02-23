<?php
class EquipmentRequest{
 
    // database connection and table name
    private $conn;
    private $table_name = "equipmentRequest";
 
    // object properties
    public $equipmentRequestID;
    public $quantity;
    public $teamRequestID;
 
    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    function read(){
 
        // select all query
        $query = "SELECT
                    equipmentRequestID, quantity, teamRequestID
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
                quantity=:quantity, teamRequestID=:teamRequestID";
 
    // prepare query
    $stmt = $this->conn->prepare($query);
 
    // sanitize
    $this->equipmentName=htmlspecialchars(strip_tags($this->equipmentName));
    $this->teamRequestID=htmlspecialchars(strip_tags($this->teamRequestID));

 
    // bind values
    $stmt->bindParam(":quantity", $this->quantity);
    $stmt->bindParam(":teamRequestID", $this->teamRequestID);

 
    // execute query
    if($stmt->execute()){
        return true;
    }
 
    return false;
     
}
}
?>