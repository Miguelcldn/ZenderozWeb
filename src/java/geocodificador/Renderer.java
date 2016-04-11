/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geocodificador;

import estructuras.Edge;
import estructuras.Graph;
import estructuras.RouteNode;
import estructuras.Route;
import java.util.Iterator;

/**
 *
 * @author Ame
 */
public class Renderer {

    private Graph map;

    public void setMap(Graph map) {
        this.map = map;
    }

    public Renderer() {}

    public String constructor(Route result, int start, int end) {

        String ultimarutatomada = null;
        int paso = 1;
        String id;
        String idady;
        String idnodo = new String();
        Double latitud;
        Double longitud;
        String latit;
        String longi;
        String urlcompleto = "<img src=\"http://maps.google.com/staticmap?";
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

                    latitud = this.map.findNode(Integer.parseInt(idnodo)).getLatitude();
                    longitud = this.map.findNode(Integer.parseInt(idnodo)).getLongitude();
                    latit = Double.toString(latitud);
                    longi = Double.toString(longitud);
                    urlcompleto = urlcompleto.concat("markers=");
                    urlcompleto = urlcompleto.concat(latit);
                    urlcompleto = urlcompleto.concat(",");
                    urlcompleto = urlcompleto.concat(longi);
                    urlcompleto = urlcompleto.concat(",");
                    urlcompleto = urlcompleto.concat("greeni");
                    urlcompleto = urlcompleto.concat("|");
                    if (Integer.parseInt(idady) < 4000) {
                        urlcompleto = urlcompleto.concat("&path=rgba:0xff6600ff,weight:6|");
                    } else {
                        urlcompleto = urlcompleto.concat("&path=rgba:0x3366CCff,weight:6|");
                    }


                }
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

                                urlcompleto = urlcompleto.concat("&path=rgba:0xff6600ff,weight:6|");
                            } else {
                                //si tomé un bus
                                if (Integer.parseInt(ultimarutatomada) < 4000) {

                                    urlcompleto = urlcompleto.concat("&path=rgba:0x3366CCff,weight:6|");
                                    ultimarutatomada = idady;
                                    paso = paso + 1;
                                }
                            }
                        }
                        ultimonodoficticio = nodo;
                    }
                } else {
                    idnodo = String.valueOf(nodo.getId()).substring(4);
                    latitud = this.map.findNode(Integer.parseInt(idnodo)).getLatitude();
                    longitud = this.map.findNode(Integer.parseInt(idnodo)).getLongitude();
                    latit = Double.toString(latitud);
                    longi = Double.toString(longitud);
                    urlcompleto = urlcompleto.concat(latit);
                    urlcompleto = urlcompleto.concat(",");
                    urlcompleto = urlcompleto.concat(longi);
                    urlcompleto = urlcompleto.concat("|");
                }
            }
            latitud = this.map.findNode(Integer.parseInt(idnodo)).getLatitude();
            longitud = this.map.findNode(Integer.parseInt(idnodo)).getLongitude();
            latit = Double.toString(latitud);
            longi = Double.toString(longitud);
            urlcompleto = urlcompleto.concat("&markers=");
            urlcompleto = urlcompleto.concat(latit);
            urlcompleto = urlcompleto.concat(",");
            urlcompleto = urlcompleto.concat(longi);
            urlcompleto = urlcompleto.concat(",");
            urlcompleto = urlcompleto.concat("redf");
            urlcompleto = urlcompleto.concat("&size=550x550&key=ABQIAAAAFPvplsJRVLg84E-dBUXf-xTgj3-QiSCkxBpNIuyJ69pKv5QlrxQcjkUOgrmJQdBx_3Y5ilBvgJx03w\"></img>");

        }
        //System.out.println("URL: " + urlcompleto);
        return urlcompleto;
    }

    public String URLConstructor(Route result, int start, int end) {
        Double latitud;
        Double longitud;
        String latit;
        String longi;


        String id;
        String idnodo = new String();
        String urlcompleto = "https://maps.googleapis.com/maps/api/staticmap?scale=2&";
        if (result.getRoutes().isEmpty() == false) {
            Iterator resultadoIterator = result.getRoutes().iterator();
            while (resultadoIterator.hasNext()) {

                RouteNode nodo = (RouteNode) resultadoIterator.next();
                id = String.valueOf(nodo.getId()).substring(0, 4);


                if (result.getRoutes().indexOf(nodo) == 0) {
                    idnodo = String.valueOf(nodo.getId()).substring(4);
                    latitud = this.map.findNode(Integer.parseInt(idnodo)).getLatitude();
                    longitud = this.map.findNode(Integer.parseInt(idnodo)).getLongitude();
                    latit = Double.toString(latitud);
                    longi = Double.toString(longitud);
                    urlcompleto = urlcompleto.concat("markers=");
                    urlcompleto = urlcompleto.concat("color:green%7Clabel:I%7C");
                    urlcompleto = urlcompleto.concat(latit);
                    urlcompleto = urlcompleto.concat(",");
                    urlcompleto = urlcompleto.concat(longi);
                    //urlcompleto = urlcompleto.concat(",");
                    //urlcompleto = urlcompleto.concat("%7C");


                }
                if (Integer.parseInt(id) > 6000 && Integer.parseInt(id) != start && Integer.parseInt(id) != end) {
                    Edge transferencia = (Edge) nodo.getEdges().get(0);
                    String transfer = String.valueOf(transferencia.getId()).substring(0, 4);
                    if (Integer.parseInt(transfer) >= 4000 && Integer.parseInt(id) != start && Integer.parseInt(id) != end && Integer.parseInt(transfer) != end) {
                        urlcompleto = urlcompleto.concat("&path=color:0xff66007f%7Cweight:6");

                    }
                    if (Integer.parseInt(transfer) < 4000) {
                        urlcompleto = urlcompleto.concat("&path=color:0x3366CC7f%7Cweight:6");
                    }
                } else {

                    if (Integer.parseInt(id) < 6000 && Integer.parseInt(id) != start && Integer.parseInt(id) != end) {
//                   

                        idnodo = String.valueOf(nodo.getId()).substring(4);
                        latitud = this.map.findNode(Integer.parseInt(idnodo)).getLatitude();
                        longitud = this.map.findNode(Integer.parseInt(idnodo)).getLongitude();
                        latit = Double.toString(latitud);
                        longi = Double.toString(longitud);
                        urlcompleto = urlcompleto.concat("%7C");
                        urlcompleto = urlcompleto.concat(latit);
                        urlcompleto = urlcompleto.concat(",");
                        urlcompleto = urlcompleto.concat(longi);
                        //urlcompleto = urlcompleto.concat("%7C");

                    }
                }
                if (!resultadoIterator.hasNext()) {
                    latitud = this.map.findNode(Integer.parseInt(idnodo)).getLatitude();
                    longitud = this.map.findNode(Integer.parseInt(idnodo)).getLongitude();
                    latit = Double.toString(latitud);
                    longi = Double.toString(longitud);
                    urlcompleto = urlcompleto.concat("&markers=");
                    urlcompleto = urlcompleto.concat("color:red%7Clabel:F%7C");
                    urlcompleto = urlcompleto.concat(latit);
                    urlcompleto = urlcompleto.concat(",");
                    urlcompleto = urlcompleto.concat(longi);
                    //urlcompleto = urlcompleto.concat(",");
                }
            }
            urlcompleto = urlcompleto.concat("&size=800x600&key=ABQIAAAAFPvplsJRVLg84E-dBUXf-xTgj3-QiSCkxBpNIuyJ69pKv5QlrxQcjkUOgrmJQdBx_3Y5ilBvgJx03w");
        }
        //System.out.println("" + urlcompleto);
        return urlcompleto;
    }
}
