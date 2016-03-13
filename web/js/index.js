/* jshint browser : true, jquery : true */
/*globals google*/


//Route data with stops
var busRoutes = [];


$(document).ready(main);

function main() {
    'use strict';
    
    $("#planRoute").click(goToRoutePlanning);
    
    $("#routeList").change(onRouteListChanged);
    
    window.clearSelect($("#routeList"));
    window.clearSelect($("#stopList"));
    
    loadBusRoutes();
}

function goToRoutePlanning() {
    'use strict';
    window.location.href = "planRoute.jsp";
    
}

function loadBusRoutes() {
    'use strict';
    
    var routesSelect = $("#routeList");
    
    $.get({url: "resources/busRoutes.json"})
    .done(function(routes) {
        
        busRoutes = routes;
        
        for(var i = 0; i < busRoutes.length; i++) {
            routesSelect.append("<option value=" + i + ">" + busRoutes[i].routeName + "</option>");
        }
    })
    .fail(function(xhr, error) {
        window.alert("Error: " + error);
    });
    
}

function loadStops(routeID) {
    'use strict';
    
    var stopsList = $("#stopList");
    window.clearSelect(stopsList);
    
    var stops = window.busRoutes[routeID].stops;
    
    for(var i = 0; i < stops.length; i++) {
        stopsList.append("<option value=" + i + ">" + stops[i] + "</option>");
    }
}

function drawMap() {
    'use strict';
    
    var map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 10.644268, lng: -71.673042},
        zoom: 14
    });
}

function onRouteListChanged(e) {
    'use strict';
    
    var route = $("#routeList").val();
    var stopList = $("#stopList");
    
    if(route === 'none') {
        stopList.prop('disabled', 'disabled');
        window.clearSelect(stopList);
    }
    else {
        stopList.prop('disabled', false);
        loadStops(route);
    }
}