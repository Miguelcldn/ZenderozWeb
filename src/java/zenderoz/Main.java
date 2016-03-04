/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Zenderoz;

import estructuras.Path;
import estructuras.Distance;
import estructuras.Graph;
import estructuras.Route;
import estructuras.Routes;
import geocodificador.Geotagger;
import geocodificador.Renderer;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import manejador.Paths;
import pathmanager.KmlHandler;
import pathmanager.PathManager;

/**
 *
 * @author Admin
 */
public class Main {
    
    KmlHandler manejadorKml = new KmlHandler();
    PathManager pathmanager = new PathManager();
    Graph mapa = manejadorKml.loadGraph();
    Paths calles = manejadorKml.loadStreets();
    Paths avenidas = manejadorKml.loadAvenues();
    Geotagger geoetiquetador = new Geotagger(calles, avenidas);
    Renderer graficador = new Renderer();
    String[] comboboxcalle = geoetiquetador.listAll(calles);
    JScrollPane scrollPane;
    ImageIcon icon;
    Image image;
    
    public void planificador(int nodoInicio, int nodoFin, int rango, int tipodebusqueda, String archivo) {
        pathmanager.weightGraph(mapa);
        pathmanager.setMap(mapa);
        Routes caminos = new Routes();
        manejadorKml.loadProcessedRoutes(archivo, caminos);
        geoetiquetador.setTransport(caminos);
        pathmanager.setFakeID((manejadorKml.getFakeIDCount() + 1));
        pathmanager.setWalkableID((manejadorKml.getWalkableCount() + 1));
        pathmanager.simulateRoutesStartEnd(caminos, nodoInicio, nodoFin);
        Routes copiacaminos = new Routes();
        copiacaminos.setRoutes(caminos.getRoutes());
        Routes tramosencont = pathmanager.pathsInRoutes2(rango, copiacaminos, caminos);
        Graph grafofinal = pathmanager.convertRoutesToGraph(tramosencont);
        graficador.setMap(mapa);
        Distance finale = null;
        if (tipodebusqueda == 2) {
            finale = grafofinal.Dijkstra(Integer.parseInt((String.valueOf(7500).concat(Integer.toString(nodoInicio)))), Integer.parseInt((String.valueOf(8500).concat(Integer.toString(nodoFin)))));
        } else if (tipodebusqueda == 1) {
            finale = grafofinal.rAncho(Integer.parseInt((String.valueOf(7500).concat(Integer.toString(nodoInicio)))), Integer.parseInt((String.valueOf(8500).concat(Integer.toString(nodoFin)))));
        }
        try {
            Route busquedaresult = pathmanager.rebuildCompleteDijkstraPath2(finale, Integer.parseInt((String.valueOf(7500).concat(Integer.toString(nodoInicio)))), Integer.parseInt((String.valueOf(8500).concat(Integer.toString(nodoFin)))));
            busquedaresult.setId(999);
            busquedaresult = pathmanager.sortRoute(busquedaresult);
            geoetiquetador.setMap(mapa);
            GuiResult(busquedaresult, mapa, calles, avenidas, caminos, tipodebusqueda, true);
            //TODO: Fix this
            //this.jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/mapazona.jpg")));
        } catch (ArrayIndexOutOfBoundsException e) {
            //TODO: Error msg here
            //JOptionPane.showMessageDialog(this, "El punto de origen y destino son iguales", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String[] selectStreet(String streetName) {                                           
        // TODO add your handling code here:
        String calleseleccionada = streetName;
        System.out.println("CALLESELECT: " + calleseleccionada);
        ArrayList avenida = geoetiquetador.findStreetNodesList(calleseleccionada);
        Path av = new Path();
        av.setNodes(avenida);
        System.out.println("AV SIZE: " + av.getNodes().size());
        ArrayList listaAvenidas = geoetiquetador.findSpot(av);
//        Paths avenid = new Paths(listaAvenidas);
        String[] listadeAv = geoetiquetador.findIntersections(listaAvenidas);
        //Cambiaba el otro combobox
        //this.jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(listadeAv));
        //this.jComboBox2.setEnabled(true);
        listaAvenidas.clear();
//        this.jComboBox2.repaint();
        return listadeAv;
    }
    
    private void planRoute(Integer range,Integer type, String start_av, String start_street, String end_av, String end_street) {
        //range : 300, 500, 700
        //type: 1, 2
        Thread th;
        th = new Thread(){
            @Override
            public void run(){
                
                geoetiquetador.setMap(mapa);
                String archivo = range.toString() + ".txt";
                int inicio = geoetiquetador.findStreetAvenueNodeID(start_av, start_street);
                int fin = geoetiquetador.findStreetAvenueNodeID(end_av, end_street);
                System.out.println("INICIO: " + inicio + " FIN: " + fin);
                
                
                if (range != 0 && type != 0) {
                    if (inicio != fin) {
                        System.out.println("Busqueda = " + range + " Inicio: " + inicio + " fin: " + fin);
                        planificador(inicio, fin, range, type, archivo);
                        System.out.println("Archivo = " + archivo);
                    } else {
                        //Error "El punto de origen y destino son iguales"
                    }
                } else {
                    //Error: "Por favor complete el formulario"
                }
            }};
        th.start();
    }     

    //<editor-fold defaultstate="collapsed" desc="Unknown function">
    /*
    public static Route Zenderoz(int nodoInicio, int nodoFin) throws IOException {
        KmlHandler manejadorKML = new KmlHandler();
        Graph mapa = manejadorKML.loadGraph();
        Paths calles = manejadorKML.loadStreets();
        Paths avenidas = manejadorKML.loadAvenues();
        PathManager pathmanager = new PathManager();
        pathmanager.weightGraph(mapa);
        mapa.printGraph();
        Geotagger geoetiquetador = new Geotagger(calles, avenidas);
        Routes rutita3 = manejadorKML.loadAllModes();
        pathmanager.setTransport(rutita3);
        pathmanager.setMap(mapa);
        pathmanager.weightGraph(mapa);
        pathmanager.weightTransportRoutes(rutita3, mapa);
        Routes prueba2 = pathmanager.completeTransportRoutes();
        pathmanager.setTransport(prueba2);
        Renderer graficador = new Renderer();
        Routes caminosdos = pathmanager.pathsInRoutes(700, prueba2);
        System.out.println("ESCRIBIENDO EN EL ARCHIVO...");
        manejadorKML.writeToFile("700.txt", caminosdos);
            return null;
    }*/
    //</editor-fold>
    
    public void GuiResult(Route resultante, Graph mapa, Paths calles, Paths avenidas, Routes transporte, int tipodebusqueda, boolean modal) {
        Geotagger resultados= new Geotagger(calles,avenidas);
        Renderer resultando = new Renderer();
        resultando.setMap(mapa);
        resultados.setMap(mapa);
        resultados.setTransport(transporte);

        String narration = resultados.constructor(resultante, 7500, 8500, tipodebusqueda);
        String result = resultando.URLConstructor(resultante, 7500, 8500);
        //this.jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/fondo1024.jpg")));
//        this.jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/regresar.gif")));
        //this.jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/regresar.gif")));
        //this.jEditorPane1.setContentType("text/html");
        //this.jEditorPane1.setText(result);
        //this.jEditorPane2.setText(narration);
        //this.setVisible(true);
        System.out.println("Printed GuiResult...");
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Ejecución");
    }
}
