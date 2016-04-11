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
public class Path {

    private int id;
    private ArrayList nodes;
    private String name;

    public Path(int id) {
        this.id = id;
        this.nodes = new ArrayList();
        this.name = null;
    }

    public Path(ArrayList nodes) {
        this.nodes = nodes;
    }

    public Path() {
        this.nodes = null;
        this.id=-4;
        this.name=null;
    }



    public int getId() {
        return id;
    }

    public ArrayList getNodes() {
        return nodes;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNodes(ArrayList nodes) {
        this.nodes = nodes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSingleNode(int node) {

        this.nodes.add(node);
        //System.out.println("Nodo con id: " + node + " insertado en el camino");

    }

    // ***************hacer bien este metodo**************
    public void printPath() {
        //System.out.println(" " + this.getNodes());

    }
}
