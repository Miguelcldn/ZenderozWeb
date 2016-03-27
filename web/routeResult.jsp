<%-- 
    Document   : routeResult
    Created on : 06-mar-2016, 18:30:58
    Author     : Miguel Celedon
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Zenderoz Web - Resultado de planificación</title>
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
                <button id="backButton" type="button" class="btn btn-lg btn-warning">Regresar</button>
            </div>
            <div class="row">
                <div style="height:50em">
                    <img id="map" class="loading-gif center-block" src="resources/loading.gif">
                </div>
            </div>
            <div class="row">
                <textarea id="instructionsTextArea" class="form-control" rows="5"></textarea>
            </div>
        </div>
        
        <script src="js/jquery-2.2.1.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/routeResult.js"></script>
    </body>
</html>
