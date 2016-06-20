<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport">
    <meta http-equiv="Content-Type" content="width=device-width, initial-scale=1">
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
    <script type="text/javascript" src="js/googleMap.js"></script>
    <script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCbKJVH2DqQ6b9miE4IObrhWV-rziC6Z1k&callback=initMap"></script>
    <link  rel="stylesheet" type="text/css" href="style/mainStyle.css"></link>
    
    <link rel="stylesheet" href="style/bootstrap.min.css">
    <script src="js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="style/jquery-ui.css" />
    <script type="text/javascript" src="js/jquery-ui.js"></script>
    <script type="text/javascript" src="js/jquery-ui-slide.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-timepicker-addon.js"></script>
    <script type="text/javascript" src="js/submit.js"></script>
</head>
<body>
    <div id="container">
        <div id="title">
            <div id="titleName">Crowdsourcing</div>
            <div id="subTitle">Let everyone help you!</div> 
        </div>
        <div id="content">
            <div id="rightContainer">
                <div id="map"></div>
            </div>
            <div id="leftContainer">
                <form id="submitForm" role="form" action="mysql.php" method="get">
                    <div class="form-group">
                        <label for="missionName">MissionTitle</label>
                        <input type="text" class="form-control" id="missionName" name="missionName" placeholder="Enter Title">
                    </div>
                    <div class="form-group">
                        <!--<label for="longitude">Longitude</label>-->
                        <input type="hidden" class="form-control" id="longitude" name="longitude" placeholder="Enter Longitude">
                    </div>
                    <div class="form-group">
                        <!--<label for="latitude">Latitude</label>-->
                        <input type="hidden" class="form-control" id="latitude" name="latitude" placeholder="Enter Latitude">
                    </div>
                    <div class="form-group">
                        <label for="startTime">Start-Time</label>
                        <input type="datetime" class="form-control" id="startTime" name="startTime" placeholder="YYYY:MM:DDHH:MM">
                    </div>
                    <div class="form-group">
                        <label for="datetime">End-Time</label>
                        <input type="datetime" class="form-control" id="datetime" name="datetime" placeholder="YYYY:MM:DDHH:MM">
                    </div>
                    <div class="form-group">
                        <label for="missionDescription">Description</label>
                        <textarea type="textarea" class="form-control" id="missionDescription" name="missionDescription" placeholder="Enter Description" rows="5" cols="40"></textarea>
                    </div>
                    <button type="submit" class="btn btn-info">submit</button>
                </form>
            </div>
        </div>
        <div id="buttom">
            <p id="labDescription">前瞻通訊網路應用平台技術-城市計算平台之研發 : 遊戲化的群眾外包系統. NMSL. </p>
        </div>
    </div>
</body>
</html>