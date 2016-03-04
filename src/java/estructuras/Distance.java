/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package estructuras;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Admin
 */
public class Distance {

    List nodes;
    float[] distances;
    int[] predecessors;

    public Distance() {
        this.nodes=null;
        this.distances=null;
        this.predecessors=null;
    }



    public Distance(List nodes, float[] distances, int[] predecessors) {
        this.nodes = nodes;
        this.distances = distances;
        this.predecessors = predecessors;
    }

    public int[] getPredecessors() {
        return predecessors;
    }

    public void setPredecessors(int[] predecessors) {
        this.predecessors = predecessors;
    }

    public void setPredecessorNode(int node, int antecesor) {
        this.predecessors[node] = antecesor;
    }

    public float[] getDistances() {
        return distances;
    }

    public float getNodeDistance(int node) {
        return distances[node];
    }

    public void setDistances(float[] distances) {
        this.distances = distances;
    }

    public void setNodeDistance(int node, float distance) {
        this.distances[node] = distance;
    }

    public List getNodes() {
        return nodes;
    }

    public void setNodes(List nodes) {
        this.nodes = nodes;
    }

    public void printDistances() {
        if (this.nodes.isEmpty()) {
            System.out.println("Grafo vacío");
        } else {
            Iterator nodosIterator = nodes.iterator();
            while (nodosIterator.hasNext()) {
                Node nodoActual = (Node) nodosIterator.next();
                nodoActual.printNode();

            }
        }
    }
}
