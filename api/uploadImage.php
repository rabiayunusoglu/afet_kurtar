<?php
   if(isset($_FILES['image'])){
      $errors= array();
      $file_name = $_FILES['image']['name'];
      $file_size =$_FILES['image']['size'];
      $file_tmp =$_FILES['image']['tmp_name'];
      $file_type=$_FILES['image']['type'];
      $file_ext=strtolower(end(explode('.',$_FILES['image']['name'])));
      
      $extensions= array("jpeg","jpg","png");
      
      if(in_array($file_ext,$extensions)=== false){
         $errors[]="extension not allowed, please choose a JPEG or PNG file.";
      }
      
      if($file_size > 8388608){
         $errors[]='File size must be excately 8 MB';
      }
      echo "https://afetkurtar.site/imgs/" . $file_name;

      if(empty($errors)==true){
         move_uploaded_file($file_tmp,"../imgs/".$file_name);
      }else{
         print_r($errors);
      }
   }else{
       echo "ERROR: isset is false.";
   }
?>