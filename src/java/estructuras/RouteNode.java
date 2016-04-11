/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package estructuras;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Ame
 */
public class RouteNode {

    private int id;
    private ArrayList edges;

    public RouteNode() {
        this.edges = new ArrayList();
            }

    public RouteNode(int id) {
        this.id = id;
        this.edges = new ArrayList();
    }

    public RouteNode(int id, ArrayList edges) {
        this.id = id;
        this.edges = edges;
    }

    public ArrayList getEdges() {
        return edges;
    }

    public int getId() {
        return id;
    }

    public void setEdges(ArrayList edges) {
        this.edges = edges;
    }

    public void setId(int id) {
        this.id = id;
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
        //System.out.println("Adyacente no encontrado!");
        return null;
    }

    public void setSingleEdge(Edge adyacente) {
        if (edgeExist(adyacente.getId()) == false) {
            this.edges.add(adyacente);
        } else {
            //System.out.println("id de adyacente ya existe");
        }

    }

    public boolean edgeExist(int id) {
        Edge adyacente = this.findEdge(id);
        return adyacente != null;
    }
}
