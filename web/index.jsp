<%-- 
    Document   : index
    Created on : 04-mar-2016, 19:28:51
    Author     : migue
    Api Key    : AIzaSyCm07AAWzZo0VOI6BKiUrcKB_LqG4YYWRU
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Zenderoz Web</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/styles.css" rel="stylesheet">
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
          <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
    </head>
    <body>
        <div class="container">
            <div class="row">
                <div class="col-md-4 col-md-offset-5">
                    <button type="button" id="planRoute" class="btn btn-lg btn-warning">Planificar ruta</button>
                </div>
            </div>
            <div class="row">
                <div class="col-md-4">
                    Seleccione la ruta:
                    <select id="routeList" class="form-control">
                    </select>
                </div>
                
                <div class="col-md-4">
                    Seleccione el sentido:
                    <select id="wayList" class="form-control" disabled>
                    </select>
                </div>
                
                <div class="col-md-4">
                    Seleccione la parada a consultar:
                    <select id="stopList" class="form-control" disabled>
                    </select>
                </div>

            </div>
            <div class="row">
                <div id="map" style="height:50em"></div>
            </div>
        </div>
        
        <script src="js/jquery-2.2.1.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/common.js"></script>
        <script src="js/index.js"></script>
        <script src="https://maps.googleapis.com/maps/api/js?callback=drawMap" async defer></script>
    </body>
</html>
