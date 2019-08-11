function initMap(activityId, lat, long) {
    var id = "map".concat(activityId.toString())
    var map = document.getElementById(id);
    if (map.style.display === "none") {
        map.style.display = "block";
    } else {
        map.style.display = "none";
    }
    var options  = {
        zoom:15,
        center:{lat:lat,lng:long}
    };
    console.log(options);
    var googleMap = new google.maps.Map(map, options);
    var marker = new google.maps.Marker({
        position:{lat:lat,lng:long},
        map: googleMap
    })
}

function initMapStartPlace(lat, long) {
    var map = document.getElementById("startPlaceMap");
    if (map.style.display === "none") {
        map.style.display = "block";
    } else {
        map.style.display = "none";
    }
    var options  = {
        zoom:15,
        center:{lat:lat,lng:long}
    };
    console.log(options);
    var googleMap = new google.maps.Map(map, options);
    var marker = new google.maps.Marker({
        position:{lat:lat,lng:long},
        map: googleMap
    })
}

