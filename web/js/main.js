/* jshint browser : true, jquery : true */
/*globals google*/


//Route data with stops
var busRoutes = [];


$(document).ready(main);

function main() {
    'use strict';
    
    $("#planRoute").click(goToRoutePlanning);
    
    loadBusRoutes();
}

function goToRoutePlanning() {
    'use strict';
    window.location.href = "planRoute.jsp";
    
}

function loadBusRoutes() {
    'use strict';
    
    $.get({url: "resources/busRoutes.json"})
    .done(function(routes) {
        busRoutes = routes;
        window.console.dir(busRoutes);
    })
    .fail(function(xhr, error) {
        window.alert("Error: " + error);
    });
    
}

function drawMap() {
    'use strict';
    
    var map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 10.644268, lng: -71.673042},
        zoom: 14
    });
}