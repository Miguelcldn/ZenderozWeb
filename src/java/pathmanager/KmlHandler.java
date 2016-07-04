/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pathmanager;

import estructuras.Edge;
import estructuras.Path;
import estructuras.Graph;
//import estructuras.Node;
import estructuras.RouteNode;
import estructuras.Route;
import estructuras.Routes;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import manejador.Paths;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author Admin
 */
public class KmlHandler {

    private final String userHome;
    private static final String KML_FILE_NAME = "mapa.kml";
    private static final String ADJ_FILE_NAME = "adyacentesmapa.txt";
    private static final String AV_FILE_NAME = "avenidas.txt";
    private static final String ROUTES_FILE_NAME = "modo microbus3.txt";
    private static final String STREETS_FILE_NAME = "Calles.txt";
    private final String fileSeparator;
    private int fakeIDCount;
    private int walkableCount;
    public int transportID;

    
    //<editor-fold defaultstate="collapsed" desc="Getters and setters">
    public int getWalkableCount() {
        return walkableCount;
    }
    
    public void setWalkableCount(int walkableCount) {
        this.walkableCount = walkableCount;
    }
    
    public int getFakeIDCount() {
        return fakeIDCount;
    }
    
    public void setFakeIDCount(int fakeIDCount) {
        this.fakeIDCount = fakeIDCount;
    }
    
    public void setTransportID(int transportID) {
        this.transportID = transportID;
    }
    
//</editor-fold>
    
    public KmlHandler() {
        this.userHome = ""; //System.getProperty("user.home");
        this.fileSeparator = System.getProperty("file.separator");
        this.fakeIDCount = 0;
        this.transportID = 1000;
    }

    public Graph loadGraph() {

        Graph mapa = null;
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(userHome + fileSeparator + KML_FILE_NAME));
            Document docx = docBuilder.parse(new File(userHome + fileSeparator + ADJ_FILE_NAME));
            //System.out.println("Leyendo archivo Kml ubicado en: " + userHome + fileSeparator + KML_FILE_NAME);
            //System.out.println("Leyendo archivo TXT ubicado en: " + userHome + fileSeparator + ADJ_FILE_NAME);
            doc.getDocumentElement().normalize();
            docx.getDocumentElement().normalize();

            NodeList folderList = doc.getElementsByTagName("Folder");
            Node folderNode = folderList.item(0);
            if (folderNode.getNodeType() == Node.ELEMENT_NODE) {
                Element folderElement = (Element) folderNode;
                //---
                NodeList placemarkList = folderElement.getElementsByTagName("Placemark");
                mapa = new Graph();
                for (int i = 0; i < placemarkList.getLength(); i++) {
                    Node placemarkNode = placemarkList.item(i);
                    Element placemarkElement = (Element) placemarkNode;
                    NodeList nameList = placemarkElement.getElementsByTagName("name");
                    Node nameNode = nameList.item(0);
                    NodeList pointList = placemarkElement.getElementsByTagName("Point");
                    Element pointElement = (Element) pointList.item(0);
                    NodeList coordinatesList = pointElement.getElementsByTagName("coordinates");
                    Node coordinatesNode = coordinatesList.item(0);
                    String[] coordenadas = coordinatesNode.getTextContent().split(",");
                    double longi = Double.parseDouble(coordenadas[0]);
                    double lati = Double.parseDouble(coordenadas[1]);

                    estructuras.Node nodo = new estructuras.Node(Integer.parseInt(nameNode.getTextContent()));
                    nodo.setLongitude(longi);
                    nodo.setLatitude(lati);
                    mapa.setSingleNode(nodo);
                }
            }

            NodeList adyacentesList = docx.getElementsByTagName("ADYACENCIAS");
            Node adyacentesNode = adyacentesList.item(0);
            if (adyacentesNode.getNodeType() == Node.ELEMENT_NODE) {
                Element adyacentesElement = (Element) adyacentesNode;
                //---
                NodeList nodoList = adyacentesElement.getElementsByTagName("nodo");

                for (int x = 0; x < nodoList.getLength(); x++) {
                    Node nodoNode = nodoList.item(x);
                    Element nodoElement = (Element) nodoNode;
                    NodeList idList = nodoElement.getElementsByTagName("id");
                    Node idNode = idList.item(0);
                    NodeList siguienteList = nodoElement.getElementsByTagName("siguiente");
                    estructuras.Node nodox = mapa.findNode(Integer.parseInt(idNode.getTextContent()));
                    ArrayList adyacentes = new ArrayList();
                    if (siguienteList != null) {
                        for (int y = 0; y < siguienteList.getLength(); y++) {
                            Edge a = new Edge(Integer.parseInt(siguienteList.item(y).getTextContent()));
                            adyacentes.add(a);
                        }
                        nodox.setEdges(adyacentes);
                    }
                }
            }

        } catch (SAXParseException err) {
            //System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            //System.out.println(" " + err.getMessage());
        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        } catch (ParserConfigurationException | IOException | DOMException | NumberFormatException t) {
            t.printStackTrace();
        }
        
        return mapa;
    }

    public Paths loadStreets() {
        Paths calles = new Paths();
        ArrayList caminos = new ArrayList();

        try {
            DocumentBuilderFactory dBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dBuilderFactory.newDocumentBuilder();
            Document cll = dBuilder.parse(new File(userHome + fileSeparator + STREETS_FILE_NAME));
            //System.out.println("Leyendo archivo TXT ubicado en: " + userHome + fileSeparator + STREETS_FILE_NAME);
            cll.getDocumentElement().normalize();


            NodeList callesList = cll.getElementsByTagName("CALLES");
            Node callesNode = callesList.item(0);
            if (callesNode.getNodeType() == Node.ELEMENT_NODE) {
                Element callesElement = (Element) callesNode;
                //---
                NodeList calleList = callesElement.getElementsByTagName("calle");

                for (int x = 0; x < calleList.getLength(); x++) {
                    Path call = new Path(x);
                    Node calleNode = calleList.item(x);
                    Element calleElement = (Element) calleNode;
                    NodeList numeroList = calleElement.getElementsByTagName("numero");
                    Node numeroNode = numeroList.item(0);
                    NodeList puntoList = calleElement.getElementsByTagName("punto");
                    ArrayList calls = new ArrayList();
                    if (puntoList != null) {
                        for (int y = 0; y < puntoList.getLength(); y++) {
                            int a = Integer.parseInt(puntoList.item(y).getTextContent());
                            calls.add(a);
                        }
                    }
                    call.setNodes(calls);
                    call.setName(numeroNode.getTextContent());
                    caminos.add(call);
                }
                calles.setPaths(caminos);
            }
        } catch (SAXParseException err) {
            //System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            //System.out.println(" " + err.getMessage());
        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        } catch (Exception t) {
            t.printStackTrace();
        } finally {
            return calles;
        }

    }

    public Paths loadAvenues() {

        Paths avenidas = new Paths();
        ArrayList caminosav = new ArrayList();

        try {
            DocumentBuilderFactory dBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dBuilderFactory.newDocumentBuilder();
            Document av = dBuilder.parse(new File(userHome + fileSeparator + AV_FILE_NAME));
            av.getDocumentElement().normalize();

            NodeList avenidasList = av.getElementsByTagName("AVENIDAS");
            Node avenidasNode = avenidasList.item(0);
            if (avenidasNode.getNodeType() == Node.ELEMENT_NODE) {
                Element avenidasElement = (Element) avenidasNode;
                //---
                NodeList avenidaList = avenidasElement.getElementsByTagName("avenida");

                for (int x = 0; x < avenidaList.getLength(); x++) {
                    Path ave = new Path(x);
                    Node avenidaNode = avenidaList.item(x);
                    Element avenidaElement = (Element) avenidaNode;
                    NodeList numeroavList = avenidaElement.getElementsByTagName("numero");
                    Node numeroavNode = numeroavList.item(0);
                    NodeList puntoavList = avenidaElement.getElementsByTagName("punto");
                    ArrayList avs = new ArrayList();
                    if (puntoavList != null) {
                        for (int y = 0; y < puntoavList.getLength(); y++) {

                            int a = Integer.parseInt(puntoavList.item(y).getTextContent());

                            avs.add(a);
                        }
                    }
                    ave.setNodes(avs);
                    ave.setName(numeroavNode.getTextContent());
                    caminosav.add(ave);
                }
                avenidas.setPaths(caminosav);
            }

        } catch (SAXParseException err) {
            //System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            //System.out.println(" " + err.getMessage());
        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        } catch (Exception t) {
            t.printStackTrace();
        } finally {
            return avenidas;
        }
    }

    public ArrayList loadRoutes(String routesFileName, int mode, String principalType, String type, ArrayList allRoutes) {

        try {
            DocumentBuilderFactory rutasBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder rutasBuilder = rutasBuilderFactory.newDocumentBuilder();
            Document rut = rutasBuilder.parse(new File(userHome + fileSeparator + routesFileName));
            rut.getDocumentElement().normalize();

            NodeList microsList = rut.getElementsByTagName(principalType);
            Node microsNode = microsList.item(0);
            if (microsNode.getNodeType() == Node.ELEMENT_NODE) {
                Element microsElement = (Element) microsNode;
                //---
                NodeList microList = microsElement.getElementsByTagName(type);

                for (int x = 0; x < microList.getLength(); x++) {
                    Route nodorutas = new Route();
                    ArrayList nodos = new ArrayList();
                    Node microNode = microList.item(x);
                    Element microElement = (Element) microNode;
                    NodeList nombreList = microElement.getElementsByTagName("nombre");
                    Node nombreNode = nombreList.item(0);
                    nodorutas.setName(nombreNode.getTextContent());
                    nodorutas.setId(this.transportID);
                    nodorutas.setMode(mode);
                    NodeList puntorutaList = microElement.getElementsByTagName("punto");
                    if (puntorutaList != null) {
                        for (int y = 0; y < puntorutaList.getLength(); y++) {
                            RouteNode ruta = new RouteNode();

                            if ((y + 1) < puntorutaList.getLength()) {
                                ArrayList adya = new ArrayList();
                                Edge nuevo = new Edge();
                                nuevo.setId(Integer.parseInt(puntorutaList.item(y + 1).getTextContent()));
                                adya.add(nuevo);
                                ruta.setId(Integer.parseInt(puntorutaList.item(y).getTextContent()));
                                ruta.setEdges(adya);
                                nodos.add(ruta);
                            }
                            nodorutas.setRoutes(nodos);
                        }
                        allRoutes.add(nodorutas);
                    }
                    this.transportID = this.transportID + 1;
                }

            }
        } catch (SAXParseException err) {
            //System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            //System.out.println(" " + err.getMessage());
        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        } catch (Exception t) {
            t.printStackTrace();
        } finally {
            return allRoutes;
        }

    }

    public void writeToFile(String filename, Routes routes) throws IOException {

        BufferedWriter bufferedWriter = null;

        try {

            //Construct the BufferedWriter object
            bufferedWriter = new BufferedWriter(new FileWriter(userHome + fileSeparator + filename));

            //Start writing to the output stream
            bufferedWriter.write("<RUTAS>");
            bufferedWriter.newLine();
            Iterator rutasTodas = routes.getRoutes().iterator();
            while (rutasTodas.hasNext()) {
                Route ruta = (Route) rutasTodas.next();
                bufferedWriter.write("  <ruta>");
                bufferedWriter.newLine();
                bufferedWriter.write("      <id>" + ruta.getId() + "</id>");
                bufferedWriter.newLine();
                bufferedWriter.write("      <nombre>" + ruta.getName() + "</nombre>");
                bufferedWriter.newLine();
                bufferedWriter.write("      <modo>" + ruta.getMode() + "</modo>");
                bufferedWriter.newLine();
                Iterator nodosIterator = ruta.getRoutes().iterator();
                bufferedWriter.write("      <NODOS>");
                bufferedWriter.newLine();
                while (nodosIterator.hasNext()) {
                    RouteNode nodo = (RouteNode) nodosIterator.next();
                    bufferedWriter.write("          <nodo>");
                    bufferedWriter.newLine();
                    bufferedWriter.write("              <id>" + nodo.getId() + "</id>");
                    Iterator adyacenteIterator = nodo.getEdges().iterator();
                    bufferedWriter.newLine();
                    bufferedWriter.write("              <ADYACENTES>");
                    bufferedWriter.newLine();
                    while (adyacenteIterator.hasNext()) {
                        Edge ady = (Edge) adyacenteIterator.next();
                        bufferedWriter.write("                  <adyacente>");
                        bufferedWriter.newLine();
                        bufferedWriter.write("                      <ide>" + ady.getId() + "</ide>");
                        bufferedWriter.newLine();
                        bufferedWriter.write("                      <peso>" + ady.getWeight() + "</peso>");
                        bufferedWriter.newLine();
                        bufferedWriter.write("                  </adyacente>");
                        bufferedWriter.newLine();
                    }
                    bufferedWriter.write("              </ADYACENTES>");
                    bufferedWriter.newLine();
                    bufferedWriter.write("          </nodo>");
                    bufferedWriter.newLine();
                }
                bufferedWriter.write("      </NODOS>");
                bufferedWriter.newLine();
                bufferedWriter.write("  </ruta>");
                bufferedWriter.newLine();

            }
            bufferedWriter.write("</RUTAS>");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Close the BufferedWriter
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public Routes loadProcessedRoutes(String routes, Routes processed) {


        try {
            DocumentBuilderFactory dBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dBuilderFactory.newDocumentBuilder();
            Document rutaspreprocesadas = dBuilder.parse(new File(userHome + fileSeparator + routes));
            rutaspreprocesadas.getDocumentElement().normalize();

            NodeList rutasList = rutaspreprocesadas.getElementsByTagName("RUTAS");
            Node rutasNode = rutasList.item(0);
            if (rutasNode.getNodeType() == Node.ELEMENT_NODE) {
                Element rutasElement = (Element) rutasNode;
                NodeList rutaList = rutasElement.getElementsByTagName("ruta");

                for (int x = 0; x < rutaList.getLength(); x++) {
                    Route ruta = new Route();
                    Node rutaNode = rutaList.item(x);
                    Element rutaElement = (Element) rutaNode;
                    NodeList idrutaList = rutaElement.getElementsByTagName("id");
                    Node idrutaNode = idrutaList.item(0);
                    ruta.setId(Integer.parseInt(idrutaNode.getTextContent()));
                    NodeList nombrerutaList = rutaElement.getElementsByTagName("nombre");
                    Node nombrerutaNode = nombrerutaList.item(0);
                    ruta.setName(nombrerutaNode.getTextContent());
                    NodeList modorutaList = rutaElement.getElementsByTagName("modo");
                    Node modorutaNode = modorutaList.item(0);
                    ruta.setMode(Integer.parseInt(modorutaNode.getTextContent()));
                    if (ruta.getMode() == 4) {
                        this.walkableCount = ruta.getId();
                    }
                    NodeList nodosList = rutaElement.getElementsByTagName("NODOS");
                    if (nodosList != null) {
                        Node nodosNode = nodosList.item(0);
                        Element nodoElement = (Element) nodosNode;
                        NodeList nodoList = nodoElement.getElementsByTagName("nodo");
                        for (int c = 0; c < nodoList.getLength(); c++) {
                            Node nodoNode = nodoList.item(c);
                            Element nodoxElement = (Element) nodoNode;
                            NodeList nodoactual = nodoxElement.getElementsByTagName("id");
                            Node nodoidNode = nodoactual.item(0);
                            int a = Integer.parseInt(nodoidNode.getTextContent());

                            RouteNode nodo = new RouteNode(a);
                            if (nodo.getId() > 6000) {
                                if (this.fakeIDCount < nodo.getId()) {
                                    this.fakeIDCount = nodo.getId();
                                }
                            }
                            NodeList adyacentesList = nodoxElement.getElementsByTagName("ADYACENTES");
                            Node adyacentesNode = adyacentesList.item(0);
                            Element adyacentesElement = (Element) adyacentesNode;
                            NodeList adyList = adyacentesElement.getElementsByTagName("adyacente");
                            for (int z = 0; z < adyList.getLength(); z++) {
                                Node adyacenteNode = adyList.item(z);
                                Element adyElement = (Element) adyacenteNode;
                                NodeList idadyList = adyElement.getElementsByTagName("ide");
                                if (idadyList != null) {
                                    Node adyNode = idadyList.item(0);
                                    int b = Integer.parseInt(adyNode.getTextContent());

                                    Edge ady = new Edge(b);
                                    if (ady.getId() > 6000) {
                                        if (this.fakeIDCount < ady.getId()) {
                                            this.fakeIDCount = ady.getId();
                                        }
                                    }
                                    NodeList pesoNode = adyElement.getElementsByTagName("peso");
                                    Node peso = pesoNode.item(0);
                                    ady.setWeight(Float.parseFloat(peso.getTextContent()));
                                    nodo.setSingleEdge(ady);

                                }
                            }
                            ruta.setSingleNode(nodo);


                        }
                        processed.setSingleRoute(ruta);
                        //System.out.println("Tamaño de preprocesadas.... " + processed.getRoutes().size());
                    }
                }

            }

        } catch (SAXParseException err) {
            //System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            //System.out.println(" " + err.getMessage());
        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        } catch (Exception t) {
            t.printStackTrace();
        } finally {
            return processed;
        }
    }

    public Routes loadAllModes() {
        Routes transporte = new Routes();
        ArrayList rutasTodas = new ArrayList();
        rutasTodas = this.loadRoutes("CARRITO.txt", 1, "CARRITOS", "carrito", rutasTodas);
        rutasTodas=this.loadRoutes("MICRO.txt", 2, "MICROBUSES", "microbus", rutasTodas);
        rutasTodas=this.loadRoutes("BUS.txt", 3, "BUSES", "bus", rutasTodas);
        transporte.setRoutes(rutasTodas);
        return transporte;
    }
}
