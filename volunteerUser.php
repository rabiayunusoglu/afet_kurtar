<?php
    if(session_id() == ''){
        header("location: https://afetkurtar.site/");
    }

    else if($_SESSION["userType"] == "authorizedUser.php"){
        header("location: https://afetkurtar.site/volunteerUser.php");
    }

    else if($_SESSION["userType"] == "personnelUser.php"){
        header("location: https://afetkurtar.site/personnelUser.php");
    }

    else if($_SESSION["userType"] != "volunteerUser.php"){
        header("location: https://afetkurtar.site/");
    }
?>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta content="width=device-width, inital-scale=1">
        <meta name="google-signin-client_id" content="984932517689-26548cubtdd33uu95vqc4t3ciq1u2os0.apps.googleusercontent.com">
        <title>Afet Kurtar</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-BmbxuPwQa2lc/FVzBcNJ7UAyJxM6wuqIj61tLrc4wSX0szH/Ev+nYRRuWlolflfl" crossorigin="anonymous">
        <link rel="stylesheet" href="css/styles.css">
        <link rel="preconnect" href="https://fonts.gstatic.com">
        <link href="https://fonts.googleapis.com/css2?family=Newsreader&display=swap" rel="stylesheet">
        <script src="https://apis.google.com/js/platform.js" async defer></script>
    </head>
    <body>
          <div><?php echo $_SESSION['userName'] ?></div>
          <div><?php echo $_SESSION['userType'] ?></div>

        <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/js/bootstrap.bundle.min.js" integrity="sha384-b5kHyXgcpbZJO/tY9Ul7kGkf1S0CWuKcCD38l8YkeH8z8QjE0GmW1gYU5S9FOnJ0" crossorigin="anonymous"></script>
        <script src="js/script.js"></script>
    </body>

</html>