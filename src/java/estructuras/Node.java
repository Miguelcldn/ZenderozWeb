/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package estructuras;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Admin
 */
public class Node {

    private int id;
    private double longitude;
    private double latitude;
    private ArrayList edges;

    public Node(int id, ArrayList edges) {
        this.id = id;
        if (edges == null) {
            this.edges = new ArrayList();
        } else {
            this.edges = edges;
        }
    }

    public Node(int id) {
        this.id = id;
        this.edges = new ArrayList();
        }

    public Node() {
     this.id = Integer.MIN_VALUE;
     this.edges = new ArrayList();
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void printNode() {
        //System.out.println("IDNODO: " + this.id);
        //System.out.println("INDEX: " );
        this.printEdges();
        //System.out.println(")");
    }

    public ArrayList getEdges() {
        return edges;
    }

    public void setEdges(ArrayList edges) {
        this.edges = edges;
    }

    public void setSingleEdge(Edge edge) {
        if (this.edgeExists(edge.getId()) == false) {
            this.edges.add(edge);
        } else {
//            //System.out.println("id de adyacente ya existe");
        }

    }

    public Edge findEdge(int id) {
        if (!this.edges.isEmpty()) {
            Iterator adyacentesIterator = this.edges.iterator();
            while (adyacentesIterator.hasNext()) {
                Edge adyacente = (Edge) adyacentesIterator.next();
                if (id == adyacente.getId()) {
                    return adyacente;
                }
            }
        }
        return null;
    }

    public boolean edgeExists(int id) {
        Edge adyacente = this.findEdge(id);
        if (adyacente != null) {
            return true;
        }
        return false;

    }

    public boolean removeEdge(int id) {
        Edge adyacente = this.findEdge(id);
        if (adyacente != null) {
            return this.edges.remove(adyacente);
        }
        return false;
    }

    public void printEdges() {
        if (this.edges.isEmpty()) {
            //System.out.println("VACIO");
        } else {
            Iterator adyacentesIterator = edges.iterator();
              while (adyacentesIterator.hasNext()) {
                Edge adyacenteActual = (Edge) adyacentesIterator.next();
                adyacenteActual.printEdge();
            }
        }
    }
}
