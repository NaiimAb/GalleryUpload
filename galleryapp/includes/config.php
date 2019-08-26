<?php

error_reporting(E_ALL ^ E_NOTICE);
ini_set('display_errors', '0');

$servername = "localhost";
$dbusername = "root";
$dbpassword = "";
$dbname = "user_gallery";

// Create connection
$conn = new mysqli($servername, $dbusername, $dbpassword, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

mysqli_query($conn, "SET NAMES utf8");

