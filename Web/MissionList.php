<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta name="viewport">
    	<meta http-equiv="Content-Type" content="width=device-width, initial-scale=1">
		<script type="text/javascript" src="js/jquery-2.1.4.min.js"></script>
		<link  rel="stylesheet" type="text/css" href="style/listStyle.css"></link>
    	<script type="text/javascript" src="js/list.js"></script>
    	<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCbKJVH2DqQ6b9miE4IObrhWV-rziC6Z1k"></script>
    	<link rel="stylesheet" href="style/bootstrap.min.css">
    	<script src="js/bootstrap.min.js"></script>
    	<?php
			$link = mysql_connect('127.0.0.1', 'b4g_broker', 'SwabWmLm4wms29cr');
			mysql_set_charset('utf8', $link);
			$db_selected = mysql_select_db('b4g_db', $link);
			$query = "SELECT id, taskName, latitude, longitude, startTime, deadLine, description FROM TaskData";
			$result = mysql_query($query, $link);

			if (!$result) {
				echo 'SQL search failed: ' . mysql_error();
				exit;
			}
			?>
	</head>
	<body>
		<div id="container">
	        <div id="title">
	            <div id="titleName">Crowdsourcing</div>
	            <div id="subTitle">List of tasks</div> 
	        </div>
	        <div id="content">
	        	<div id="leftContainer">
	        		<table class="table">
						<tr class='list'>
							<th class='listItem' id="Title">Title</th>
							<th class='listItem' id="Description">Description</th>
							<th class='listItem' id="Time">Start-Time</th>
							<th class='listItem' id="Time">End-Time</th>
							<!--<th class='listItem' id="isDone">isDone</th>-->
						</tr>
					<?php
						$length_row = mysql_num_rows($result);
						$length_column = mysql_num_fields($result);
						while($row = mysql_fetch_array($result)) {
							echo "<tr class='list' videoPath='" . $row['id'] . "' lat='" . $row['latitude'] . "' lon='" . $row['longitude'] . "'>";
							echo "<td class='listItem listTitle'>" . $row['taskName'] . "</td>";
							echo "<td class='listItem listDescription'>" . $row['description'] . "</td>";
							echo "<td class='listItem listTime'>" . $row['startTime'] . "</td>";
							echo "<td class='listItem listTime'>" . $row['deadLine'] . "</td>";
							//echo "<td class='listItem listDone'>YES</td>";
							echo "</tr>";
						}
					?>
					</table>
	        	</div>
	        	<div id="rightContainer">
					<div id="listInform">
						<div id="userInform">
							<!--<div>完成時間: 2015/10/27 13:12</div>-->
						</div>
						<div id="map">
			        	</div>
			        	<video id="missionImg" controls>
							<source id="video" src="0.mp4" type="video/mp4">
						</video>
		        	</div>
	        	</div>
	        </div>
			
	        <div id="buttom">
	            <p id="labDescription">前瞻通訊網路應用平台技術-城市計算平台之研發 : 遊戲化的群眾外包系統. NMSL. </p>
	        </div>
	    </div>
	</body>
</html>