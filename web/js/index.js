/* jshint browser : true, jquery : true */
/*globals google*/


//Route data with stops
var busRoutes = {};
var map;

var wayList, stopList, routeList;

var mapStops = {};
var mapBuses = null;
var activeUpdate = false;
var ETAMessage = null;
var selectedStop = null;


$(document).ready(main);

function main() {
    'use strict';
    
    wayList = $("#wayList");
    stopList = $("#stopList");
    routeList = $("#routeList");
    
    $("#planRoute").click(goToRoutePlanning);
    
    routeList.change(onRouteListChanged);
    wayList.change(onWayListChanged);
    stopList.change(onStopListChanged);
    
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
    
    var findReturnPosition = function(route) {
        for(var j = 0; j < route.stops.length; j++) {
            if(route.stops[j].stopID === route.returnStop) {
                route.returnStopPosition = j;
                break;
            }
        }
    };
    
    $.get({url: "routes"})
    .done(function(routes) {
        
        for(var i = 0; i < routes.length; i++) {
            var routeID = routes[i].routeID;
            
            routeList.append("<option value=" + routeID + ">" + routes[i].routeName + "</option>");
            busRoutes[routeID] = routes[i];
            findReturnPosition(busRoutes[routeID]);
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
    
    var busRoute = window.busRoutes[routeID];
    var stops = busRoute.stops;
    var start = 0,
        end = busRoute.returnStopPosition;
    
    //Continue from second half is way is false
    if(!way) {
        start = end;
        end = stops.length;
    }
    
    for(var i = start; i < end; i++) {
        
        var stop = stops[i];
        
        stopList.append("<option value=" + stop.stopID + ">" + stop.stopName + "</option>");
        drawStop(stop);
        
        /*if(stops[i][way].length !== 0) {
            stopList.append("<option value=" + i + ">" + stops[i][0] + "</option>");
            drawStop(stops[i], way);
        }*/
    }
}

function clearMarkers() {
    
    for(var stop in mapStops)
        mapStops[stop].marker.setMap(null);
    
    mapStops = {};
}

function clearUnits() {
    for(var bus in mapBuses)
        mapBuses[bus].marker.setMap(null);
    
    mapBuses = null;
}

function drawMap() {
    'use strict';
    
    map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 10.644268, lng: -71.673042},
        zoom: 14
    });
}

function drawStop(stop) {
    var marker = new google.maps.Marker({
        map : map,
        animation : google.maps.Animation.DROP,
        title : stop.stopName,
        position : {
            lat : stop.lat,
            lng : stop.lng
        }
    });
    
    marker.addListener('click', function() {onMarkerClick.call(stop);});
    mapStops[stop.stopID] = stop;
    mapStops[stop.stopID].marker = marker;
}

function onMarkerClick(e) {
    map.panTo(this.marker.position);
    var self = this;
    stopList.val(this.stopID);
    setTimeout(function() {
        map.setZoom(16);
        showETA(self);
    }, 500);
}

function onStopListChanged(e) {
    if(stopList.val().toString() !== 'none') {
        
        var stop = mapStops[parseInt(stopList.val())];
        onMarkerClick.call(stop);
    }
}

function onWayListChanged(e) {
    'use strict';
    
    var way = wayList.val();
    
    if(way === 'none') {
        stopList.prop('disabled', 'disabled');
        window.clearSelect(stopList);
        onStopListChanged();
        clearMarkers();
    }
    else {
        stopList.prop('disabled', false);
        loadStops(routeList.val(), way === 'true');
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
        onWayListChanged();
        onStopListChanged();
        
        activeUpdate = false;
        clearUnits();
    }
    else {
        wayList.prop('disabled', false);
        loadWays(route);
        activeUpdate = true;
        updateUnits();
    }
}

function loadWays(routeID) {
    var start = busRoutes[routeID].stops[0].stopName,
        end = busRoutes[routeID].stops[busRoutes[routeID].returnStopPosition].stopName;
    
    wayList.append("<option value='" + true + "'>" + start + " - " + end + "</option>");
    wayList.append("<option value='" + false + "'>" + end + " - " + start + "</option>");
}

function updateUnits() {
    
    if(!activeUpdate) return;
    
    if(mapBuses === null)
        mapBuses = {};
        
    $.ajax({
        url: 'units',
        success: function(list) {
            
            var bus = null,
                marker;

            for(var i = 0; i < list.length; i++) {
                bus = list[i];
                var mapBus = mapBuses[bus.id];
                
                if(mapBus === undefined) {
                    mapBuses[bus.id] = bus;
                    mapBus = mapBuses[bus.id];
                }
                
                if(mapBus.marker === undefined) {
                    
                    
                    var icon = {
                        url : "resources/bus_logo.png",
                        scaledSize : new google.maps.Size(25, 25)
                    };
                    
                    marker = new google.maps.Marker({
                        clickable : false,
                        icon : icon,
                        title : bus.id + '',
                        label : bus.id + '',
                        map : map,
                        position : {
                            lat : bus.lat,
                            lng : bus.lng
                        }
                    });
                    
                    
                    mapBuses[bus.id].marker = marker;
                }
                else {
                    marker = mapBuses[bus.id].marker;
                    marker.setPosition({
                        lat : bus.lat,
                        lng : bus.lng
                    });
                    mapBuses[bus.id] = bus;
                    mapBuses[bus.id].marker = marker;
                }
            }
            
            setTimeout(updateUnits, 1000);
        },
        error: function(query, error) {
            window.alert("Error: " + error);
            activeUpdate = false;
        }
    });
}

function showETA(stop) {
    
    if(ETAMessage !== null) ETAMessage.close();
    
    var content = "<div>" +
        "<strong>Tiempo estimado de llegada:</strong><br>" + 
        "<span id='ETA'>Calculando</span>" +
        "</div>";
    
    ETAMessage = new google.maps.InfoWindow({content : content});
    ETAMessage.open(map, stop.marker);
    
    selectedStop = stop;
    updateETA(stop);
    
}

function updateETA(stop) {
    var error = function(response) {};
    var success = function(response) {
        if(response.distance) {
            var seconds = response.distance / mapBuses[response.unitID].avgSpeed;
            var date = new Date(null);
            date.setSeconds(Math.floor(seconds));
            var time = date.toISOString().substr(11, 8);
            
            $("#ETA").text(time);
            
            if(selectedStop.stopID === stop.stopID)
                setTimeout(function() { updateETA(stop); }, 5000);
        } else {
            error(response);
            if(selectedStop.stopID === stop.stopID)
                setTimeout(function() { updateETA(stop); }, 5000);
        }
    };
    
    $.ajax({
        url: "distance?routeid=" + stop.routeID + "&stopid=" + stop.stopID,
        method: "GET",
        success: success,
        error: error
    });
}