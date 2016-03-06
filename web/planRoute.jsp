<%-- 
    Document   : planRoute
    Created on : 06-mar-2016, 16:32:22
    Author     : Miguel Celedon
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Zenderoz Web - Planificar Ruta</title>
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
                <button type="button" class="btn btn-lg btn-warning">Regresar</button>
            </div>
            <div class="row routeForm">
                <form role="form">
                    <div class="form-group">
                        <label for="maxDistance">Máxima distancia a caminar</label>
                        <div class="radio">
                            <label><input type="radio" value="300" name="maxDistance" checked>300 mts.</label>
                        </div>
                        <div class="radio">
                            <label><input type="radio" value="500" name="maxDistance">500 mts.</label>
                        </div>
                        <div class="radio">
                            <label><input type="radio" value="750" name="maxDistance">750 mts.</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="originStreet">Punto de origen</label>
                        <select class="form-control" name="originStreet">
                            <option value="none">Calle--</option>
                        </select>
                        <select class="form-control" disabled>
                            <option value="none">Avenida--</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="destStreet">Punto de destino</label>
                        <select class="form-control" name="destStreet">
                            <option value="none">Calle--</option>
                        </select>
                        <select class="form-control" disabled>
                            <option value="none">Avenida--</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="type">Tipo de búsqueda</label>
                        <div class="radio">
                            <label><input name="type" type="radio" value="distance" checked>Menos distancia</label>
                        </div>
                        <div class="radio">
                            <label><input name="type" type="radio" value="change">Menos transbordos</label>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-default">Planificar</button>
                </form>
            </div>
        </div>
        
        <script src="js/jquery-2.2.1.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/main.js"></script>
    </body>
</html>
