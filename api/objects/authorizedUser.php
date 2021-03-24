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
                authorizedID=:authorizedID, institution=:institution, authorizedName=:authorizedName";
 
    // prepare query
    $stmt = $this->conn->prepare($query);
 
    // sanitize
    $this->authorizedID=htmlspecialchars(strip_tags($this->authorizedID));
    $this->institution=htmlspecialchars(strip_tags($this->institution));
    $this->authorizedName=htmlspecialchars(strip_tags($this->authorizedName));
 
    // bind values
    $stmt->bindParam(":authorizedID", $this->authorizedID);
    $stmt->bindParam(":institution", $this->institution);
    $stmt->bindParam(":authorizedName", $this->authorizedName);
 
    // execute query
    if($stmt->execute()){
        return true;
    }
 
    return false;
     
    }

    function search(){
        // select all query
        $query = "SELECT
                    authorizedID, institution, authorizedName
                FROM
                    " . $this->table_name . "
                WHERE
                    (authorizedID = :authorizedID OR :authorizedID = '') AND (institution = :institution OR :institution = '') AND (authorizedName = :authorizedName OR :authorizedName = '')";
     
        // prepare query statement
        $stmt = $this->conn->prepare($query);
     

        // sanitize
        $this->authorizedID=htmlspecialchars(strip_tags($this->authorizedID));
        $this->institution=htmlspecialchars(strip_tags($this->institution));
        $this->authorizedName=htmlspecialchars(strip_tags($this->authorizedName));
    
        // bind values
        $stmt->bindParam(":authorizedID", $this->authorizedID);
        $stmt->bindParam(":institution", $this->institution);
        $stmt->bindParam(":authorizedName", $this->authorizedName);
     
        // execute query
        $stmt->execute();
     
        return $stmt;
    }

    function delete(){
        // delete query
        $query = "DELETE FROM " . $this->table_name . " WHERE authorizedID = :authorizedID";
    
        // prepare query
        $stmt = $this->conn->prepare($query);
    
        // sanitize
        $this->authorizedID=htmlspecialchars(strip_tags($this->authorizedID));
    
        // bind id of record to delete
        $stmt->bindParam(":authorizedID", $this->authorizedID);
    
        // execute query
        if($stmt->execute()){
            return true;
        }
    
        return false;
    }

    function update(){
        // update query
        $query = "UPDATE
                    " . $this->table_name . "
                SET
                    institution=:institution, authorizedName=:authorizedName
                WHERE
                    authorizedID=:authorizedID";
    
        // prepare query statement
        $stmt = $this->conn->prepare($query);
    
        // sanitize
        $this->authorizedID=htmlspecialchars(strip_tags($this->authorizedID));
        $this->institution=htmlspecialchars(strip_tags($this->institution));
        $this->authorizedName=htmlspecialchars(strip_tags($this->authorizedName));
    
        // bind values
        $stmt->bindParam(":authorizedID", $this->authorizedID);
        $stmt->bindParam(":institution", $this->institution);
        $stmt->bindParam(":authorizedName", $this->authorizedName);
    
    
        // execute the query
        if($stmt->execute()){
            return true;
        }
    
        return false;
    }
}
?>