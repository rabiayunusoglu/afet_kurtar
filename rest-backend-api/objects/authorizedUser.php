<?php
class AuthorizedUser{
 
    // database connection and table name
    private $conn;
    private $table_name = "authorizedUser";
 
    // object properties
    public $authorizedID;
    public $institution;
    public $authorizedName;
 
    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    function read(){
 
        // select all query
        $query = "SELECT
                    authorizedID, institution, authorizedName
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
                institution=:institution, authorizedName=:authorizedName";
 
    // prepare query
    $stmt = $this->conn->prepare($query);
 
    // sanitize
    $this->institution=htmlspecialchars(strip_tags($this->institution));
    $this->authorizedName=htmlspecialchars(strip_tags($this->authorizedName));
 
    // bind values
    $stmt->bindParam(":institution", $this->institution);
    $stmt->bindParam(":authorizedName", $this->authorizedName);
 
    // execute query
    if($stmt->execute()){
        return true;
    }
 
    return false;
     
}
}
?>