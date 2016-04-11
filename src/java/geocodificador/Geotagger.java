/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geocodificador;

import estructuras.Edge;
import estructuras.Path;
import estructuras.Graph;
import java.util.Iterator;
import manejador.Paths;
import estructuras.RouteNode;
import estructuras.Route;
import estructuras.Routes;
import java.util.ArrayList;

/**
 *
 * @author Ame
 */
public class Geotagger {

    Paths streets;
    Paths avenues;
    Graph map;
    Routes transport;

    public void setMap(Graph map) {
        this.map = map;
    }

    public Routes getTransport() {
        return transport;
    }

    public Geotagger(Paths streets, Paths avenues) {
        this.streets = streets;
        this.avenues = avenues;

    }

    public void setTransport(Routes transport) {
        this.transport = transport;
    }

    public Paths getAvenues() {
        return avenues;
    }

    public Paths getStreets() {
        return streets;
    }

    public void setAvenues(Paths avenues) {
        this.avenues = avenues;
    }

    public void setStreets(Paths streets) {
        this.streets = streets;
    }

    public int findStreetAvenueNodeID(String avenida_id, String calle_id) {
        ArrayList avenida_nodos = new ArrayList();
        ArrayList calle_nodos = new ArrayList();
        int id_nodo_interseccion = -1;
        boolean flag = false;

        Iterator callesIterator = this.streets.getPaths().iterator();
        while (callesIterator.hasNext()) {
            Path calle = (Path) callesIterator.next();
            if (calle.getName().equalsIgnoreCase(calle_id)) {
                calle_nodos = calle.getNodes();
            }
        }
        Iterator avenidasIterator = this.avenues.getPaths().iterator();
        while (avenidasIterator.hasNext()) {
            Path avenida = (Path) avenidasIterator.next();
            if (avenida.getName().equalsIgnoreCase(avenida_id)) {
                avenida_nodos = avenida.getNodes();
            }
        }

        Iterator callenodosIterator = calle_nodos.iterator();
        while (callenodosIterator.hasNext() && !flag) {
            int id_calle = (Integer) callenodosIterator.next();
            Iterator avenidanodosIterator = avenida_nodos.iterator();
            while (avenidanodosIterator.hasNext() && !flag) {
                int id_av = (Integer) avenidanodosIterator.next();
                if (id_calle == id_av) {
                    id_nodo_interseccion = id_calle;
                    flag = true;
                }
            }
        }
        return id_nodo_interseccion;
    }

    public ArrayList findSpot(Path streetAvenue) {
        ArrayList ubicacion = new ArrayList();

        boolean found = false;
        if (avenues.getPaths().isEmpty() == false) {
            Iterator caminosIterator = streetAvenue.getNodes().iterator();
            while (caminosIterator.hasNext()) {
                int nodo = (Integer) caminosIterator.next();
                Iterator callesIterator = avenues.getPaths().iterator();
                while (callesIterator.hasNext() && found == false) {

                    Path calle = (Path) callesIterator.next();
                    Iterator calleIterator = calle.getNodes().iterator();
                    while (calleIterator.hasNext()) {
                        int punto = (Integer) calleIterator.next();
//                    //System.out.println("AHORA AKI");
                        if (nodo == punto) {
                            ubicacion.add(calle.getName());
                            found = true;
                        }
                    }
                }
                found = false;
            }
        }
        return ubicacion;
    }

    public int findIntersectionNode(Path street, Path avenue) {
        int nodoid = Integer.MIN_VALUE;
        if (street.getNodes().isEmpty() == false && avenue.getNodes().isEmpty() == false) {
            Iterator callIterator = street.getNodes().iterator();
            while (callIterator.hasNext()) {
                int calleid = (Integer) callIterator.next();
                Iterator avIterator = avenue.getNodes().iterator();
                while (avIterator.hasNext()) {
                    int avid = (Integer) avIterator.next();
//                    //System.out.println("AHORA AKI");
                    if (calleid == avid) {
                        //System.out.println("calle " + street);
                        nodoid = calleid;
                    }
                }
            }
        }
        return nodoid;
    }

    public String realNodeLocation(int nodeID) {
        String ubicacion = new String();
        boolean calleflag = false;
        boolean avenidaflag = false;
        if (streets.getPaths().isEmpty() == false) {
            Iterator callesIterator = streets.getPaths().iterator();
            while (callesIterator.hasNext()) {
                Path calle = (Path) callesIterator.next();
                Iterator calleIterator = calle.getNodes().iterator();
                while (calleIterator.hasNext()) {
                    int puntocalle = (Integer) calleIterator.next();
                    if (nodeID == puntocalle) {
                        ubicacion = ubicacion.concat("calle " + calle.getName());
                        calleflag = true;
                    }
                }
            }
        }

        if (avenues.getPaths().isEmpty() == false) {
            Iterator avenidasIterator = avenues.getPaths().iterator();
            while (avenidasIterator.hasNext()) {
                Path avenida = (Path) avenidasIterator.next();
                Iterator avenidaIterator = avenida.getNodes().iterator();
                while (avenidaIterator.hasNext()&&!avenidaflag) {
                    int puntoav = (Integer) avenidaIterator.next();
                    if (nodeID == puntoav) {
                        if (calleflag) {
                            ubicacion = ubicacion.concat(" con");
                        }
                        ubicacion = ubicacion.concat(" Avenida " + avenida.getName());
                        avenidaflag = true;
                    }
                }
            }
        }
        if (!calleflag && !avenidaflag) {
            ubicacion = "Calle/Av SN";
        }
        return ubicacion;
    }

    public ArrayList findStreetNodesList(String name) {
        ArrayList listadenodos = new ArrayList();
        Iterator callesIterator = this.streets.getPaths().iterator();
        while (callesIterator.hasNext()) {
            Path calleactual = (Path) callesIterator.next();
            if (calleactual.getName().equalsIgnoreCase(name)) {
                listadenodos = calleactual.getNodes();
            }
        }
        return listadenodos;
    }

    public ArrayList findStreetContent(String name, Paths streetAvenue) {
        ArrayList contenido = new ArrayList();
        Iterator calleavenidaIterator = streetAvenue.getPaths().iterator();
        while (calleavenidaIterator.hasNext()) {
            Path camino = (Path) calleavenidaIterator.next();
            if (name.equalsIgnoreCase(String.valueOf(camino.getId()))) {

                contenido = camino.getNodes();

            }
        }
        return contenido;
    }

    public String[] listAll(Paths streetsAvenues) {
        String[] nombretodas = new String[streetsAvenues.getPaths().size()];
        int contador = 0;
        Iterator todas = streetsAvenues.getPaths().iterator();
        while (todas.hasNext()) {
            Path camino = (Path) todas.next();
            nombretodas[contador] = camino.getName();
            contador++;
        }
        return nombretodas;
    }

    public String[] findIntersections(ArrayList streetAvenue) {
        String[] intersecciones = new String[streetAvenue.size()];
        for (int i = 0; i < streetAvenue.size(); i++) {

            intersecciones[i] = (String) streetAvenue.get(i);
        }
        return intersecciones;
    }

    public String constructor(Route result, int start, int end, int search) {
        Double pesoanterior;
        String descripcion = new String();
        String ultimarutatomada = null;
        int paso = 1;
        String id;
        String idady;
        String idnodo;
        RouteNode ultimonodoficticio = new RouteNode(Integer.MIN_VALUE);

        if (result.getRoutes().isEmpty() == false) {
            Iterator resultadoIterator = result.getRoutes().iterator();
            while (resultadoIterator.hasNext()) {

                RouteNode nodo = (RouteNode) resultadoIterator.next();
                Edge adyacente = (Edge) nodo.getEdges().get(0);
                id = String.valueOf(nodo.getId()).substring(0, 4);
                idady = String.valueOf(adyacente.getId()).substring(0, 4);


                if (result.getRoutes().indexOf(nodo) == 0) { //Si estoy en el comienzo
                    idnodo = String.valueOf(nodo.getId()).substring(4);
                    descripcion = descripcion.concat("Comienza en: " + this.realNodeLocation(Integer.parseInt(idnodo)) + "\\n");


                } else {
                    //si el adyacente va a una transferencia, guardo la ultima ruta conocida
                    if (Integer.parseInt(idady) > 6000 && Integer.parseInt(idady) != start && Integer.parseInt(idady) != end) {
                        ultimarutatomada = id;
                    }
                    //Si consigo un nodo ficticio
                    if (Integer.parseInt(id) > 6000 && Integer.parseInt(id) != start) { //&& Integer.parseInt(idady) != fin) {
                        if (ultimonodoficticio.getId() == Integer.MIN_VALUE) {
                            ultimonodoficticio = nodo;
                        }
                        if (ultimarutatomada == null) {
                            ultimarutatomada = idady;
                        } else {
                            Edge ultimoconocido = (Edge) ultimonodoficticio.getEdges().get(0);
                            //si es una transferencia en el mismo punto
                            if (result.getRoutes().indexOf(ultimonodoficticio) == ((result.getRoutes().indexOf(nodo)) - 2) && Integer.parseInt(idady) != end) {
                                ultimarutatomada = idady;
                            } else {

                                //si caminé desde el ultimo nodo ficticio...
                                if (Integer.parseInt(ultimarutatomada) >= 4000 && !String.valueOf(ultimoconocido.getId()).substring(4).equalsIgnoreCase(String.valueOf(adyacente.getId()).substring(4))) {
                                    pesoanterior = adyacente.getWeight() - ultimoconocido.getWeight();
                                    if (search == 1) {
                                        descripcion = descripcion.concat(paso + ") Camine  desde la ");

                                        descripcion = descripcion.concat("" + this.realNodeLocation(Integer.parseInt(String.valueOf(ultimoconocido.getId()).substring(4))));
                                        descripcion = descripcion.concat(" hasta la " + this.realNodeLocation(Integer.parseInt(String.valueOf(adyacente.getId()).substring(4))));
                                        descripcion = descripcion.concat("\\n");
                                        ultimarutatomada = idady;
                                        
                                        paso = paso + 1;
                                    } else {
                                        if (search == 2) {
                                            descripcion = descripcion.concat(paso + ")" + " Camine " + pesoanterior.intValue() + " metros desde la " + this.realNodeLocation(Integer.parseInt(String.valueOf(ultimoconocido.getId()).substring(4))) + " hasta la " + this.realNodeLocation(Integer.parseInt(String.valueOf(adyacente.getId()).substring(4))) + "\\n");
                                            ultimarutatomada = idady;
                                            paso = paso + 1;

                                        }
                                    }
                                } else {
                                    //si tomé un bus
                                    if (Integer.parseInt(ultimarutatomada) < 4000) {
                                        descripcion = descripcion.concat(paso + ")" + " Tome la ruta de " + this.transport.findRoute(Integer.parseInt(ultimarutatomada)).getName() + " en sentido " + this.orientation(Integer.parseInt(String.valueOf(ultimoconocido.getId()).substring(4)), Integer.parseInt(String.valueOf(adyacente.getId()).substring(4))) + " desde la " + this.realNodeLocation(Integer.parseInt(String.valueOf(ultimoconocido.getId()).substring(4))) + " hasta la " + this.realNodeLocation(Integer.parseInt(String.valueOf(adyacente.getId()).substring(4))) + "\\n");
                                        ultimarutatomada = idady;
                                        paso = paso + 1;
                                    }
                                }
                            }
                            ultimonodoficticio = nodo;
                        }
                    }
                }


            }
            descripcion = descripcion.concat("Fin del viaje");
        }
        return descripcion;
    }

    public String orientation(int startID, int endID) {
        String orientacion = new String();
        double latitud = this.map.findNode(startID).getLatitude();
        double longitud = this.map.findNode(startID).getLongitude();
        double latit = this.map.findNode(endID).getLatitude();
        double longi = this.map.findNode(endID).getLongitude();

        if (latitud > latit && longitud > longi) {

            orientacion = "SUR-OESTE";

        }
        if (latitud > latit && longitud < longi) {

            orientacion = "SUR-ESTE";
        }
        if (latitud < latit && longitud < longi) {

            orientacion = "NOR-ESTE";
        }

        if (latitud < latit && longitud > longi) {

            orientacion = "NOR-OESTE";
        }
        return orientacion;
    }
}
