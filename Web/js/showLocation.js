var map;
var mapMarker;
function initMap() {
	var latlng = new google.maps.LatLng(24.79513721101473, 120.99257530644536);
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