/* jshint browser : true, jquery : true */
/*globals google*/


//Route data with stops
var busRoutes = [];
var map;

var wayList, stopList, routeList;

var mapStops = [];
var mapBuses = null;
var activeUpdate = false;


$(document).ready(main);

function main() {
    'use strict';
    
    wayList = $("#wayList");
    stopList = $("#stopList");
    routeList = $("#routeList");
    
    $("#planRoute").click(goToRoutePlanning);
    
    routeList.change(onRouteListChanged);
    wayList.change(onWayListChanged);
    
    window.clearSelect(routeList);
    window.clearSelect(stopList);
    window.clearSelect(wayList);
    
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
        
        for(var i = 0; i < busRoutes.length; i++) {
            routeList.append("<option value=" + i + ">" + busRoutes[i].routeName + "</option>");
        }
    })
    .fail(function(xhr, error) {
        window.alert("Error: " + error);
    });
    
}

function loadStops(routeID, way) {
    'use strict';
    
    window.clearSelect(stopList);
    clearMarkers();
    
    var stops = window.busRoutes[routeID].stops;
    
    for(var i = 0; i < stops.length; i++) {
        if(stops[i][way].length !== 0) {
            stopList.append("<option value=" + i + ">" + stops[i][0] + "</option>");
            drawStop(stops[i], way);
        }
    }
}

function clearMarkers() {
    
    for(var i = 0; i < mapStops.length; i++)
        mapStops[i].marker.setMap(null);
    
    mapStops = [];
}

function drawMap() {
    'use strict';
    
    map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 10.644268, lng: -71.673042},
        zoom: 14
    });
}

function drawStop(stop, way) {
    var marker = new google.maps.Marker({
        map : map,
        animation : google.maps.Animation.DROP,
        title : stop[0],
        position : {
            lat : stop[way][0],
            lng : stop[way][1]
        }
    });
    
    marker.addListener('click', onMarkerClick);
    mapStops.push(marker);
}

function onMarkerClick(e) {
    map.panTo(this.position);
    setTimeout(function() {
        map.setZoom(16);
    }, 500);
}

function onWayListChanged(e) {
    'use strict';
    
    var way = wayList.val();
    
    if(way === 'none') {
        stopList.prop('disabled', 'disabled');
        window.clearSelect(stopList);
    }
    else {
        stopList.prop('disabled', false);
        loadStops(routeList.val(), way);
    }
}

function onRouteListChanged(e) {
    'use strict';
    
    var route = routeList.val();
    
    if(route === 'none') {
        wayList.prop('disabled', 'disabled');
        stopList.prop('disabled', 'disabled');
        window.clearSelect(wayList);
        window.clearSelect(stopList);
        activeUpdate = false;
    }
    else {
        wayList.prop('disabled', false);
        loadWays(route);
        activeUpdate = true;
        updateUnits();
    }
}

function loadWays(routeID) {
    var start = busRoutes[routeID].stops[0][0],
        end = busRoutes[routeID].stops[busRoutes[routeID].stops.length - 1][0];
    
    wayList.append("<option value='" + 1 + "'>" + start + " - " + end + "</option>");
    wayList.append("<option value='" + 2 + "'>" + end + " - " + start + "</option>");
}

function updateUnits() {
    
    if(mapBuses === null)
        mapBuses = {};
        
    $.ajax({
        url: 'units',
        success: function(list) {
            
            var bus = null;

            for(var i = 0; i < list.length; i++) {
                bus = list[i];
                var mapBus = mapBuses[bus.id];
                
                if(mapBus === undefined) {
                    
                    var icon = {
                        url : "resources/bus_logo.png",
                        scaledSize : new google.maps.Size(25, 25)
                    };
                    
                    mapBus = new google.maps.Marker({
                        clickable : false,
                        icon : icon,
                        label : bus.id,
                        map : map,
                        position : {
                            lat : bus.lat,
                            lng : bus.lng
                        }
                    });
                    
                    mapBuses[bus.id] = mapBus;
                }
                else {
                    mapBus.setPosition({
                        lat : bus.lat,
                        lng : bus.lng
                    });
                }
            }
            
            if(activeUpdate)
                setTimeout(updateUnits, 1000);
        },
        error: function(query, error) {
            window.alert("Error: " + error);
            activeUpdate = false;
        }
    });
}