var map;
var mapMarker;
function initMap(lat, lon) {
	var latlng = new google.maps.LatLng(lon, lat);
　　var myOptions = {
　　　　zoom: 17,
　　　　center: latlng
　　};
　　currentMap = new google.maps.Map(document.getElementById("map"), myOptions);

　　// Clear all the click event of the map
　　// Register a click event to the map
	var optionOfMarker = {
　　　　position: latlng,
　　　　map: currentMap,
　　　　title: latlng.toString()
　　};
	// Show marker in the place of mouse clicks
　　mapMarker = new google.maps.Marker(optionOfMarker);
　　mapMarker.setAnimation(google.maps.Animation.DROP);
}

$( document ).ready(function() {
    console.log( "ready!" );
    $(".list").each(function() {
    	$(this).click(function() {
    		console.log( "click" );
	    	$("#video").attr('src', $(this).attr('videoPath') + '.mp4');
	    	$("#missionImg").load();
			$("#rightContainer").fadeIn();
			initMap($(this).attr('lat'), $(this).attr('lon'));
		});
	});
	$("#rightContainer").click(function() {
		$("#rightContainer").fadeOut();
	});
	
});