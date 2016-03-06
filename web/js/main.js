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
    
    window.location.href = "planRoute.jsp";
    
}

function loadBusRoutes() {
    
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
    
    var map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: -34.397, lng: 150.644},
        zoom: 8
    });
}