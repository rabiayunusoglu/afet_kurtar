<?php
class Equipment{
 
    // database connection and table name
    private $conn;
    private $table_name = "equipment";
 
    // object properties
    public $equipmentID;
    public $equipmentName;
    public $equipmentImageURL;
 
    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    function read(){
 
        // select all query
        $query = "SELECT
                    equipmentID, equipmentName, equipmentImageURL
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
                equipmentName=:equipmentName, equipmentImageURL=:equipmentImageURL";
 
    // prepare query
    $stmt = $this->conn->prepare($query);
 
    // sanitize
    $this->equipmentName=htmlspecialchars(strip_tags($this->equipmentName));
    $this->equipmentImageURL=htmlspecialchars(strip_tags($this->equipmentImageURL));

 
    // bind values
    $stmt->bindParam(":equipmentName", $this->equipmentName);
    $stmt->bindParam(":equipmentImageURL", $this->equipmentImageURL);

 
    // execute query
    if($stmt->execute()){
        return true;
    }
 
    return false;
     
}
}
?>