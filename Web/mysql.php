<?php

$link = mysql_connect('127.0.0.1', 'b4g_broker', 'SwabWmLm4wms29cr');
mysql_set_charset('utf8', $link);

$db_selected = mysql_select_db('b4g_db', $link);

$missionName = $_GET['missionName'];
$latitude = $_GET['latitude'];
$longitude = $_GET['longitude'];
$startTime = $_GET['startTime'];
$deadLine = $_GET['datetime'];
$missionDescription = $_GET['missionDescription'];

$query = "INSERT INTO TaskData (taskName, latitude, longitude, startTime, deadLine, description) VALUES ('" . $missionName . "','" . $latitude . "','" . $longitude . "','" . $startTime . "','" . $deadLine . "','" . $missionDescription . "')";
$result = mysql_query($query, $link);

if (!$result) {
	echo 'SQL search failed: ' . mysql_error();
	exit;
}
?>