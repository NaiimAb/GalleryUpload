<?php
include '../includes/config.php';

if (!empty($_GET["user_id"])) {

    $user_id = $_GET['user_id'];

    $gallery_query = mysqli_query($conn, "SELECT * FROM gallery WHERE user_id = '{$user_id}'") or die($conn->error);

    //Fetch into associative array
    $data_array = array();

    while ($row = mysqli_fetch_assoc($gallery_query)) {
        $data_array[] = $row;
    }

   print json_encode($data_array);
}
else {
    echo '404 - Not Found <br/>';
    echo 'the Requested URL is not found on this server ';
}