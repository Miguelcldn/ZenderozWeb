/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package estructuras;

/**
 *
 * @author Admin
 */
public class Edge {

    private int id;
    private float weight;

    public Edge() {
        this.id = Integer.MIN_VALUE;
        this.weight = Float.MIN_VALUE;
    }

    public Edge(int id) {
        this.id = id;
        this.weight = Float.POSITIVE_INFINITY;
    }

    public Edge(int id, float peso) {
        this.id = id;
        this.weight = peso;
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void printEdge() {
        System.out.println("(id: " + this.id + " peso: " + this.weight + ")");
    }
}
