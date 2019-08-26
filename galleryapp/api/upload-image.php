<?php
include '../includes/config.php';

$target_dir = "uploads/images/";

$response = array();
$file_name = $_POST["name"];
$user_id = $_POST["user_id"];
$update_image = $_POST["update"];

if (isset($_FILES["file"])) {
    $target_file_name = $target_dir . $file_name;
    if($update_image == "true" && file_exists($target_file_name)) {
        unlink($target_file_name);
    }

    if (move_uploaded_file($_FILES["file"]["tmp_name"], $_SERVER["DOCUMENT_ROOT"]."/galleryapp/".$target_file_name)) {
        if($update_image == "false") {
            $db_query = mysqli_query($conn, "INSERT INTO gallery (path, user_id) values ('{$target_file_name}', {$user_id})") or die($conn->error);
        }
        $res_code = 1;
        $error = false;
        $message = "Successfully uploaded image";

    } else {
        $res_code = 2;
        $error = true;
        $message = "Error occurred when adding image";
    }
} else {
    $res_code = 2;
    $error = true;
    $message = "Required Field Missing";
}

$response["status_code"] = $res_code;
$response["error"] = $error;
$response["message"] = $message;
echo json_encode($response);
