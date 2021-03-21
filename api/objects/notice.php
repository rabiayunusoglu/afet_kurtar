<?php
class Notice{
 
    // database connection and table name
    private $conn;
    private $table_name = "notice";
 
    // object properties
    public $noticeID;
    public $type;
    public $latitude;
    public $longitude;
    public $message;
    public $imageURL;
 
    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    function read(){
 
        // select all query
        $query = "SELECT
                    noticeID, `type`, latitude, longitude, `message`, imageURL
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
                    type=:type, latitude=:latitude, longitude=:longitude, message=:message, imageURL=:imageURL";
     
        // prepare query
        $stmt = $this->conn->prepare($query);
     
        // sanitize
        $this->type=htmlspecialchars(strip_tags($this->type));
        $this->latitude=htmlspecialchars(strip_tags($this->latitude));
        $this->longitude=htmlspecialchars(strip_tags($this->longitude));
        $this->message=htmlspecialchars(strip_tags($this->message));
        $this->imageURL=htmlspecialchars(strip_tags($this->imageURL));
    
     
        // bind values
        $stmt->bindParam(":type", $this->type);
        $stmt->bindParam(":latitude", $this->latitude);
        $stmt->bindParam(":longitude", $this->longitude);
        $stmt->bindParam(":message", $this->message);
        $stmt->bindParam(":imageURL", $this->imageURL);
    
     
        // execute query
        if($stmt->execute()){
            return true;
        }
     
        return false;
         
    }

    function search(){
        // select all query
        $query = "SELECT
                    noticeID, `type`, latitude, longitude, `message`, imageURL
                FROM
                    " . $this->table_name . "
                WHERE
                    (noticeID = :noticeID OR :noticeID = '') 
                    AND (`type` = :type OR :type = '')
                    AND (latitude = :latitude OR :latitude = '')
                    AND (longitude = :longitude OR :longitude = '')
                    AND (`message` = :message OR :message = '')
                    AND (imageURL = :imageURL OR :imageURL = '')";
     
        // prepare query statement
        $stmt = $this->conn->prepare($query);
     

        // sanitize
        $this->noticeID=htmlspecialchars(strip_tags($this->noticeID));
        $this->type=htmlspecialchars(strip_tags($this->type));
        $this->latitude=htmlspecialchars(strip_tags($this->latitude));
        $this->longitude=htmlspecialchars(strip_tags($this->longitude));
        $this->message=htmlspecialchars(strip_tags($this->message));
        $this->imageURL=htmlspecialchars(strip_tags($this->imageURL));
    
     
        // bind values
        $stmt->bindParam(":noticeID", $this->noticeID);
        $stmt->bindParam(":type", $this->type);
        $stmt->bindParam(":latitude", $this->latitude);
        $stmt->bindParam(":longitude", $this->longitude);
        $stmt->bindParam(":message", $this->message);
        $stmt->bindParam(":imageURL", $this->imageURL);
     
        // execute query
        $stmt->execute();
     
        return $stmt;
    }
    function delete(){
  
    // delete query
    $query = "DELETE FROM " . $this->table_name . " WHERE noticeID = :noticeID";
  
    // prepare query
    $stmt = $this->conn->prepare($query);
  
    // sanitize
	$this->noticeID=htmlspecialchars(strip_tags($this->noticeID));
  
    // bind id of record to delete
    $stmt->bindParam(":noticeID", $this->noticeID);
  
    // execute query
    if($stmt->execute()){
        return true;
    }
  
    return false;
}
}
?>