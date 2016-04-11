/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pathmanager;

import estructuras.Edge;
import estructuras.Distance;
import estructuras.Graph;
import java.util.Iterator;
import manejador.Paths;
import estructuras.Node;
import estructuras.RouteNode;
import estructuras.Route;
import estructuras.Routes;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Admin
 */
public class PathManager {

    private Graph map;
    private Paths routes;
    private Routes transport;
    public int fakeID;
    public int walkableID;

    public PathManager() {
        this.map = null;
        this.routes = null;
        this.transport = null;
        this.fakeID = 6000;
        this.walkableID = 4000;

    }

    public void setWalkableID(int walkableID) {
        this.walkableID = walkableID;
    }

    public void setFakeID(int fakeID) {
        this.fakeID = fakeID;
    }

    public Routes getTransport() {
        return transport;
    }

    public void setTransport(Routes transport) {
        this.transport = transport;
    }

    public Graph getMap() {
        return map;
    }

    public Paths getRoutes() {
        return routes;
    }

    public void setMap(Graph map) {
        this.map = map;
    }

    public void setRoutes(Paths routes) {
        this.routes = routes;
    }

    public PathManager(Graph map, Paths routes) {
        this.map = map;
        this.routes = routes;
    }

    public double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);

    }

    public double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public void simulateRoutesStartEnd(Routes routes, int start, int end) {

        Routes oridest = new Routes();
        Route origen = new Route();
        Route destino = new Route();
        origen.setName("inicio");
        origen.setMode(9);
        origen.setId(7500);
        destino.setName("fin");
        destino.setMode(9);
        destino.setId(8500);
        ArrayList nodoruta = new ArrayList();
        ArrayList nodofin = new ArrayList();
        RouteNode nodofinal = new RouteNode();
        RouteNode nodo = new RouteNode();
        nodo.setId(start);
        nodofinal.setId(end);
        nodoruta.add(nodo);
        nodofin.add(nodofinal);
        origen.setRoutes(nodoruta);
        destino.setRoutes(nodofin);
        oridest.setSingleRoute(origen);

        ArrayList cam = routes.getRoutes();
        cam.add(0, origen);
        cam.add(1, destino);
        routes.setRoutes(cam);
        this.setTransport(routes);
    }

    public double computeDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        dist = dist * 1000;

        return (dist);
    }

    public Graph weightGraph(Graph map) {
        if (map.getNodes().isEmpty()) {
            //System.out.println("ERROR: EMPTY");
        } else {

            Iterator nodosIterator = map.getNodes().iterator();
            while (nodosIterator.hasNext()) {
                Node nodoActual = (Node) nodosIterator.next();
                double lati1 = nodoActual.getLatitude();
                double longi1 = nodoActual.getLongitude();
                Iterator adyacentesIterator = nodoActual.getEdges().iterator();
                while (adyacentesIterator.hasNext()) {
                    Edge adyacenteActual = (Edge) adyacentesIterator.next();
                    Node adyacente = map.findNode(adyacenteActual.getId());
                    double lati2 = adyacente.getLatitude();
                    double longi2 = adyacente.getLongitude();
                    float peso = (float) this.computeDistance(lati1, longi1, lati2, longi2);
                    adyacenteActual.setWeight(peso);
                }
            }
        }
        return map;
    }

    public void weightTransportRoutes(Routes transport, Graph map) {
        if (transport.getRoutes().isEmpty()) {
            //System.out.println(" ERROR: EMPTY");
        } else {

            Iterator rutasIterator = transport.getRoutes().iterator();
            while (rutasIterator.hasNext()) {
                Route rutaActual = (Route) rutasIterator.next();

                Iterator rutaIterator = rutaActual.getRoutes().iterator();
                rutaActual.printRoute();
                while (rutaIterator.hasNext()) {

                    RouteNode nodo = (RouteNode) rutaIterator.next();
                    Node temporal = map.findNode(nodo.getId());
                    Iterator adyIterator = nodo.getEdges().iterator();
                    while (adyIterator.hasNext()) {

                        Edge adyacenteActual = (Edge) adyIterator.next();
                        Edge temp = temporal.findEdge(adyacenteActual.getId());
//
                        float peso = (float) temp.getWeight();
//
                        adyacenteActual.setWeight(peso);
//

                    }
                }
            }
        }
    }

    public Distance findRouteInRange(Graph map, int id, int range) {
        Distance resultado = map.DijkstraTruncado(id, range);
        return resultado;
    }

    public Route rebuildCompleteDijkstraPath(Distance vector, int startID, int endID) {

        int[] padres = vector.getPredecessors();
        Route caminofinal = new Route();
        Route resultado = new Route();
        caminofinal.setRoutes((ArrayList) vector.getNodes());

        while (endID != startID) {

            int index = caminofinal.getRoutes().indexOf(endID);
            RouteNode nuevo = new RouteNode(padres[index]);
            Edge adyac = new Edge(endID);

            Node test = this.map.findNode(nuevo.getId());
            Edge testdos = test.findEdge(adyac.getId());
            adyac.setWeight((float) testdos.getWeight());

            nuevo.setSingleEdge(adyac);
            resultado.setSingleNode(nuevo);
            endID = nuevo.getId();


        }
        RouteNode prueba = new RouteNode(Integer.MIN_VALUE);
        Edge ady = new Edge(endID, 0);
        prueba.setSingleEdge(ady);
        resultado.setSingleNode(prueba);

        return resultado;
    }

    public Route rebuildCompleteDijkstraPath2(Distance vector, int startID, int endID) {

        int[] padres = vector.getPredecessors();
        float[] distancias = vector.getDistances();
        Route caminofinal = new Route();
        Route resultado = new Route();
        caminofinal.setRoutes((ArrayList) vector.getNodes());
        boolean flag = false;
        while (endID != startID) {

            int index = caminofinal.getRoutes().indexOf(endID);
            RouteNode nuevo = new RouteNode(padres[index]);
            Edge adyac = new Edge(endID);
            adyac.setWeight(distancias[index]);
            nuevo.setSingleEdge(adyac);
            resultado.setSingleNode(nuevo);
            endID = nuevo.getId();


        }


        return resultado;
    }

    public Routes pathsInRoutes(int maxDistance, Routes routes) {
        Routes caminofinal = new Routes();
        Routes tramosEncontrados = new Routes();
        Routes tramosEncontradoscopia = new Routes();
        int flag;
        int nodosig = 0;
        int contador = 0;
        Route resultado = new Route();

        if (this.transport.getRoutes().isEmpty()) {
            //System.out.println("ERROR: EMPTY");
        } else {
//            //System.out.println(" Cantidad de routes: SIZE  " + this.transport.getRoutes().size());
            CopyOnWriteArrayList iteratorsPrincipales = new CopyOnWriteArrayList(routes.getRoutes());
//            //System.out.println("ITERATORSSIZERRR: " + iteratorsPrincipales.size());
            for (int x = 0; x < this.transport.getRoutes().size(); x++) {
                Iterator rutasPrincipalIterator = iteratorsPrincipales.listIterator(x);// Se crea el ITERADOR PRINCIPAL DE RUTAS
                Iterator rutasSecundarioIterator;
                while (rutasPrincipalIterator.hasNext()) { //Iterador de routes
                    contador++;

                    Route rutaActual = (Route) rutasPrincipalIterator.next();
                    rutasSecundarioIterator = rutasPrincipalIterator;

                    while (rutasSecundarioIterator.hasNext()) {
                        Route rutaSiguiente = (Route) rutasSecundarioIterator.next();
                        CopyOnWriteArrayList pruebanodo = new CopyOnWriteArrayList(rutaActual.getRoutes());
                        Iterator rutaIterator = pruebanodo.iterator();// se crea el iterador interno de la RUTA PRINCIPAL

                        while (rutaIterator.hasNext()) {
//                            if(!tramosEncontrados.getRoutes().isEmpty())
                            float ultimopesoencontrado = Float.MAX_VALUE;
                            float pesoactual;
                            flag = 0;
                            RouteNode nodo = (RouteNode) rutaIterator.next();
                            //System.out.println("Nodo: " + nodo.getId());
                            //System.out.println("ITERANDO DENTRO DE LA RUTA: " + rutaActual.getId() + " CONTRA LA RUTA: " + rutaSiguiente.getId());
                            int id = nodo.getId();
                            Distance conexion = this.findRouteInRange(map, id, maxDistance);
                            ListIterator conexionIterator = conexion.getNodes().listIterator();

                            while (conexionIterator.hasNext()) {
//                            //System.out.println("EXPLORANDO EL RESULTADO DE DIJKSTRA");
                                int conex = (Integer) conexionIterator.next();
                                CopyOnWriteArrayList prueba = new CopyOnWriteArrayList(rutaSiguiente.getRoutes());
                                Iterator rutaSiguienteIterator = prueba.iterator();

                                while (rutaSiguienteIterator.hasNext() && flag != -1) {    //comparo todos los elementos de la ruta siguiente, con cada adyacende del resultado de Dijkstra.
//                                    //System.out.println("COMPARANDO CADA NODO DE LA RUTA: " + rutaSiguiente.getId());


                                    RouteNode nodoSiguiente = (RouteNode) rutaSiguienteIterator.next();

                                    if (conex == nodoSiguiente.getId() && rutaSiguienteIterator.hasNext()) {
                                        //System.out.println("Eureka!!!");
                                        //System.out.println("Desde el nodo " + nodo.getId() + " de la ruta " + rutaActual.getId() + " se alcanza a la ruta " + rutaSiguiente.getId() + " por el nodo " + nodoSiguiente.getId());

                                        flag = 2;
                                        caminofinal.setRoutes((ArrayList) conexion.getNodes());
                                        int index = caminofinal.getRoutes().indexOf(nodoSiguiente.getId());
                                        float[] dist = conexion.getDistances();
                                        pesoactual = dist[index];
                                        if (nodo.getId() == nodoSiguiente.getId()) {
                                            flag = -1;
                                            //System.out.println("Aca");
                                            resultado = this.rebuildCompleteDijkstraPath(conexion, nodo.getId(), nodoSiguiente.getId());
                                            tramosEncontrados = this.tagPaths(tramosEncontrados, resultado, nodo.getId(), nodoSiguiente.getId(), rutaActual.getId(), rutaSiguiente.getId());

                                        } else {
                                            //if(!flag){
                                            if (ultimopesoencontrado == Float.MAX_VALUE) {
                                                //System.out.println("here..." + pesoactual);
                                                ultimopesoencontrado = pesoactual;
                                                resultado = this.rebuildCompleteDijkstraPath(conexion, nodo.getId(), nodoSiguiente.getId());
                                                nodosig = nodoSiguiente.getId();
                                                flag = 2;
                                            } else {
                                                if (ultimopesoencontrado > pesoactual) {
                                                    //System.out.println("Es menor!!! .." + ultimopesoencontrado + " >" + pesoactual);
                                                    resultado = this.rebuildCompleteDijkstraPath(conexion, nodo.getId(), nodoSiguiente.getId());
                                                    ultimopesoencontrado = pesoactual;
                                                    nodosig = nodoSiguiente.getId();
                                                    flag = 2;
                                                }


                                            }
                                        }

                                    }



                                }


                            }

                            if (flag > 0) {
                                //System.out.println("ETIQUETO");
                                tramosEncontrados = this.tagPaths(tramosEncontrados, resultado, nodo.getId(), nodosig, rutaActual.getId(), rutaSiguiente.getId());
                            }

//                          




                        }
                        //System.out.println("verifico toda 1 ruta");

                    }
                    //System.out.println("RUTA ACTUAL: " + rutaActual.getId());

                }
            }


        }

        //System.out.println("TERMINO DE CONECTAR RUTAS");

        //System.out.println("TRAMOS ENCONTRADOS = " + tramosEncontrados.getRoutes().size());
        return tramosEncontrados;
    }

    public Routes pathsInRoutes2(int maxDistance, Routes routes, Routes preprocessed) {

        Routes tramosEncontrados = new Routes();
        Routes caminofinal = new Routes();
        Routes preprocesadascopia = new Routes(preprocessed.getRoutes());
        int contador = 0;

        if (this.transport.getRoutes().isEmpty()) {
            //System.out.println("ERROR: EMPTY");
        } else {
            CopyOnWriteArrayList iteratorsPrincipales = new CopyOnWriteArrayList(routes.getRoutes());
            for (int y = 0; y < 2; y++) {
                Iterator rutasPrincipalIterator = iteratorsPrincipales.listIterator(y);// Se crea el ITERADOR PRINCIPAL DE RUTAS
                Iterator rutasSecundarioIterator;
                while (rutasPrincipalIterator.hasNext()) { //Iterador de routes
                    contador++;
                    float ultimopesoencontrado = Float.MAX_VALUE;
                    Route rutaActual = (Route) rutasPrincipalIterator.next();
                    rutasSecundarioIterator = rutasPrincipalIterator;
                    while (rutasSecundarioIterator.hasNext()) {

                        Route rutaSiguiente = (Route) rutasSecundarioIterator.next();
                        if (rutaSiguiente.getMode() != 4 && rutaSiguiente.getMode() != 9) {
                            RouteNode nodounico = (RouteNode) rutaActual.getRoutes().get(0);
                            int id = nodounico.getId();
                            Distance conexion = this.findRouteInRange(map, id, maxDistance);
                            ListIterator conexionIterator = conexion.getNodes().listIterator();

                            while (conexionIterator.hasNext()) {
//                            //System.out.println("EXPLORANDO EL RESULTADO DE DIJKSTRA");
                                int conex = (Integer) conexionIterator.next();
                                CopyOnWriteArrayList prueba = new CopyOnWriteArrayList(rutaSiguiente.getRoutes());
                                Iterator rutaSiguienteIterator = prueba.iterator();
                                while (rutaSiguienteIterator.hasNext()) {    //comparo todos los elementos de la ruta siguiente, con cada adyacende del resultado de Dijkstra.

//                                    //System.out.println("COMPARANDO CADA NODO DE LA RUTA: " + rutaSiguiente.getId());

                                    RouteNode nodoSiguiente = (RouteNode) rutaSiguienteIterator.next();

                                    if (conex == nodoSiguiente.getId() && rutaSiguienteIterator.hasNext()) {
                                        caminofinal.setRoutes((ArrayList) conexion.getNodes());
                                        int index = caminofinal.getRoutes().indexOf(nodoSiguiente.getId());
                                        float[] dist = conexion.getDistances();
                                        if (ultimopesoencontrado == Float.MAX_VALUE) {
                                            ultimopesoencontrado = dist[index];
//                                            //System.out.println("Ultimopesoencontrado= " + ultimopesoencontrado + " Node SiguienteID: " + nodoSiguiente.getId());
                                            Route resultado = this.rebuildCompleteDijkstraPath(conexion, nodounico.getId(), nodoSiguiente.getId());
                                            preprocessed = this.tagPaths(preprocessed, resultado, nodounico.getId(), nodoSiguiente.getId(), rutaActual.getId(), rutaSiguiente.getId());
                                        } else {
                                            float pesoactual = dist[index];
                                            if (ultimopesoencontrado > pesoactual) {

                                                int indexaborrarcaminable = preprocessed.getRoutes().indexOf(preprocessed.findRoute((this.walkableID - 1)));

                                                int indexaborraactual = preprocessed.getRoutes().indexOf(preprocessed.findRoute(rutaActual.getId()));
                                                int indexaborrarsiguiente = preprocessed.getRoutes().indexOf(preprocessed.findRoute(rutaSiguiente.getId()));
                                                Route actualcopia = new Route();
                                                actualcopia.setId(preprocessed.findRoute(rutaActual.getId()).getId());
                                                actualcopia.setMode(rutaActual.getMode());
                                                actualcopia.setName(rutaActual.getName());
                                                actualcopia.setRoutes(preprocesadascopia.findRoute(rutaActual.getId()).getRoutes());
                                                Route siguientecopia = new Route();
                                                siguientecopia.setId(preprocessed.findRoute(rutaSiguiente.getId()).getId());
                                                siguientecopia.setMode(rutaSiguiente.getMode());
                                                siguientecopia.setName(rutaSiguiente.getName());
                                                siguientecopia.setRoutes(preprocesadascopia.findRoute(rutaSiguiente.getId()).getRoutes());
                                                ArrayList prep = preprocessed.getRoutes();
                                                prep.set(indexaborraactual, actualcopia);
                                                prep.set(indexaborrarsiguiente, siguientecopia);
                                                preprocessed.setRoutes(prep);

                                                Route resultado = this.rebuildCompleteDijkstraPath(conexion, nodounico.getId(), nodoSiguiente.getId());
                                                preprocessed = this.tagPaths(preprocessed, resultado, nodounico.getId(), nodoSiguiente.getId(), rutaActual.getId(), rutaSiguiente.getId());
                                                ultimopesoencontrado = pesoactual;
                                            }
                                        }
                                    }
                                }


                            }
                            //System.out.println("verifico toda 1 ruta");
                        }
                    }
                    //System.out.println("RUTA ACTUAL: " + rutaActual.getId());

                }
            }
        }

        //System.out.println("TERMINO DE CONECTAR RUTAS");

        //System.out.println("TRAMOS ENCONTRADOS = " + preprocessed.getRoutes().size());
        return preprocessed;
    }

    public Routes tagPaths(Routes foundEdges, Route result, int actualNodeID, int nextNodeID, int actualPathID, int nextPathID) {

        Routes found = new Routes();
        Routes copia_transporte = new Routes();
        ArrayList copiaTransporte = this.transport.getRoutes();
        copia_transporte.setRoutes(copiaTransporte);


        if (result.getRoutes().size() > 1) {
            result = this.completeRoute(result);
        } else {
            result = this.inverseRoute(result);
        }

        ArrayList rutax = result.getRoutes();
        RouteNode nodoFicticio = new RouteNode(this.fakeID);
        Edge adyFicticio = new Edge(actualNodeID);
        Edge ficticios = new Edge(this.fakeID, 1);
        this.fakeID = this.fakeID + 1;
        adyFicticio.setWeight(1);
        nodoFicticio.setSingleEdge(adyFicticio);
        rutax.add(0, nodoFicticio);
        RouteNode nodoficticio = new RouteNode(this.fakeID);
        Edge adFicticio = new Edge(nextNodeID);
        Edge fictici = new Edge(this.fakeID, 1);
        this.fakeID = this.fakeID + 1;
        adFicticio.setWeight(1);
        nodoficticio.setSingleEdge(adFicticio);
        rutax.add(nodoficticio);
        Route Caminables = new Route(rutax, 4, this.walkableID);
        Caminables.findRouteNode(nextNodeID).setSingleEdge(fictici);
        this.walkableID = this.walkableID + 1;
        Caminables.findRouteNode(actualNodeID).setSingleEdge(ficticios);
        Caminables.setMode(4);
        foundEdges.getRoutes().add(Caminables);

        if (foundEdges.routeExist(actualPathID)) {
            nodoFicticio.findEdge(adyFicticio.getId()).setWeight(1);

            foundEdges.findRoute(actualPathID).setSingleNode(nodoFicticio);
            foundEdges.findRoute(actualPathID).findRouteNode(actualNodeID).setSingleEdge(ficticios);
        } else {
            Route actuals = this.transport.findRoute(actualPathID);
//          nodoFicticio.findEdge(adyFicticio.getId()).setPeso(0);
            actuals.setSingleNode(nodoFicticio);
            actuals.findRouteNode(actualNodeID).setSingleEdge(ficticios);
            foundEdges.getRoutes().add(actuals);
        }

        if (foundEdges.routeExist(nextPathID)) {
//        nodoficticio.findEdge(adyFicticio.getId()).setPeso(0);
            foundEdges.findRoute(nextPathID).setSingleNode(nodoficticio);
            foundEdges.findRoute(nextPathID).findRouteNode(nextNodeID).setSingleEdge(fictici);
        } else {
            Route siguiente = this.transport.findRoute(nextPathID);
            siguiente.setSingleNode(nodoficticio);
            siguiente.findRouteNode(nextNodeID).setSingleEdge(fictici);
            foundEdges.setSingleRoute(siguiente);
        }

        return foundEdges;

    }

    public Route sortRoute(Route result) {
        Route resultadoOrientado = new Route();
        resultadoOrientado.setId(result.getId());
        for (int y = result.getRoutes().size(); y > 0; y--) {
            RouteNode nodo = (RouteNode) result.getRoutes().get(y - 1);
            resultadoOrientado.setSingleNode(nodo);
        }

        return resultadoOrientado;
    }

    public Route inverseRoute(Route route) {
        Route rutaInversa = new Route();
        rutaInversa.setId(route.getId());

        Iterator rutaOrientada = route.getRoutes().iterator();
        while (rutaOrientada.hasNext()) {
            RouteNode nodoOrientado = (RouteNode) rutaOrientada.next();
            Iterator adyacentesIterator = nodoOrientado.getEdges().iterator();
            while (adyacentesIterator.hasNext()) {
                Edge adyacente = (Edge) adyacentesIterator.next();
                int i = adyacente.getId();
                RouteNode inverso = new RouteNode(i);
                Edge adyinverso = new Edge(nodoOrientado.getId());
                adyinverso.setWeight((float) adyacente.getWeight());
                if (route.getRoutes().size() == 1) {
                    if (adyinverso.getId() == Integer.MIN_VALUE) {
                        rutaInversa.setSingleNode(inverso);
                    }
                } else {
                    inverso.setSingleEdge(adyinverso);
                    rutaInversa.setSingleNode(inverso);
                }
            }


        }
        return rutaInversa;

    }

    public Route completeRoute(Route route) {
        Route ruta_copia = route;
        ruta_copia.setId(route.getId());
        Route ruta_ordenada_inversa;
        Route copia_ruta_ordenada;


        ruta_ordenada_inversa = this.inverseRoute(this.sortRoute(ruta_copia));
        copia_ruta_ordenada = this.inverseRoute(this.sortRoute(ruta_copia));

        if (route.getMode() != 4) {
            Edge temp = ((Edge) ((RouteNode) ruta_copia.getRoutes().get(0)).getEdges().get(0));
            RouteNode inicio = new RouteNode(((RouteNode) ruta_copia.getRoutes().get(0)).getId());
            Edge inicioAdy = new Edge(temp.getId(), (float) temp.getWeight());
            inicio.setSingleEdge(inicioAdy);
            ruta_ordenada_inversa.setSingleNode(inicio);
            copia_ruta_ordenada.setSingleNode(inicio);
        }


        Iterator rutaOrdenadaInversaIterator = ruta_ordenada_inversa.getRoutes().iterator();
        while (rutaOrdenadaInversaIterator.hasNext()) {
            if (rutaOrdenadaInversaIterator.hasNext()) {
                RouteNode nodoOrdenado = (RouteNode) rutaOrdenadaInversaIterator.next();
                Iterator nodoOrdenadoIterator = nodoOrdenado.getEdges().iterator();
                while (nodoOrdenadoIterator.hasNext()) {
                    if (nodoOrdenadoIterator.hasNext()) {
                        Edge adyacenteOrdenado = (Edge) nodoOrdenadoIterator.next();
                        RouteNode copia = ruta_copia.findRouteNode(nodoOrdenado.getId());
                        if (copia != null) {
                            Edge adyacentecopia = (Edge) copia.getEdges().get(0);
                            if (adyacentecopia.getId() != adyacenteOrdenado.getId()) {
                                copia_ruta_ordenada.findRouteNode(nodoOrdenado.getId()).setSingleEdge(adyacentecopia);
                                if (adyacenteOrdenado.getId() == Integer.MIN_VALUE) {
                                    copia_ruta_ordenada.findRouteNode(nodoOrdenado.getId()).getEdges().remove(0);
                                }
                            }
                        }

                    }
                }
            }
        }
        copia_ruta_ordenada.setId(route.getId());
        copia_ruta_ordenada.setMode(route.getMode());
        copia_ruta_ordenada.setName(route.getName());
//        copia_ruta_ordenada.printRoute();
        return copia_ruta_ordenada;

    }

    public Routes completeTransportRoutes() {
        Routes completadas = new Routes();
        ArrayList rutascompletas = new ArrayList();
//        Iterator rutasDeTransporte = this.transport.getRoutes().iterator();
        for (int i = 0; i < this.transport.getRoutes().size(); i++) {
//            Route rutaIterator = (Route) rutasDeTransporte.next();

            Route completa = this.completeRoute((Route) this.transport.getRoutes().get(i));
            rutascompletas.add(completa);
//            completadas.getRoutes().add(completa);
        }
        completadas.setRoutes(rutascompletas);
        return completadas;
    }

    public Graph convertRoutesToGraph(Routes processedRoutes) {

        Graph definitivo = new Graph();
        boolean ingresado = false;

        Iterator rutasIterator = processedRoutes.getRoutes().iterator();
        while (rutasIterator.hasNext()) {
            Route rutaActual = (Route) rutasIterator.next();
            Iterator nodorutaIterator = rutaActual.getRoutes().iterator();
            while (nodorutaIterator.hasNext()) {
                RouteNode nodoActual = (RouteNode) nodorutaIterator.next();
                Node nodonuevo = new Node();
                if (nodoActual.getId() < 6000) {
                    nodonuevo.setId(Integer.parseInt(String.valueOf(rutaActual.getId()).concat(String.valueOf(nodoActual.getId()))));
                    definitivo.setSingleNode(nodonuevo);
                } else {
                    nodonuevo.setId(nodoActual.getId());
                    definitivo.setSingleNode(nodonuevo);
                }
                Iterator adyacentesIterator = nodoActual.getEdges().iterator();
                while (adyacentesIterator.hasNext()) {
                    Edge adyacente = (Edge) adyacentesIterator.next();
                    Edge adyacentenuevo = new Edge();
                    if (adyacente.getId() < 6000) {
                        adyacentenuevo.setId(Integer.parseInt(String.valueOf(rutaActual.getId()).concat(String.valueOf(adyacente.getId()))));
                    } else {
                        adyacentenuevo.setId(adyacente.getId());
                    }
                    adyacentenuevo.setWeight((float) adyacente.getWeight());
                    if (nodoActual.getId() >= 6000 && definitivo.findNode(nodoActual.getId()) != null) {
                        definitivo.findNode(nodoActual.getId()).setSingleEdge(adyacentenuevo);
                        ingresado = true;
                    } else {
                        nodonuevo.setSingleEdge(adyacentenuevo);
                    }
                }
                if (!ingresado) {
                    definitivo.setSingleNode(nodonuevo);
                }
            }
            //System.out.println("----------FIN------------");
        }
        return definitivo;
    }
}
