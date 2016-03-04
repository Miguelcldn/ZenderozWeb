/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package estructuras;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Admin
 */
public class Graph {

    private ArrayList nodes;

    public Graph() {
        this.nodes = new ArrayList();
    }

    public void setNodes(ArrayList nodes) {
        this.nodes = nodes;
    }

    public ArrayList getNodes() {
        return nodes;
    }

    public void setSingleNode(Node node) {
        if (!this.nodeExist(node.getId())) {
            this.nodes.add(node);
//            System.out.println("Node con id: " + nodo.getId() + " insertado en el grafo");
        } else {
            System.out.println("Este id de nodo ya existe");
        }
    }

    public Node findNode(int id) {
        if (!nodes.isEmpty()) {
            Iterator nodosIterator = nodes.iterator();
            while (nodosIterator.hasNext()) {
                Node nodoAlmacenado = (Node) nodosIterator.next();
                if (id == nodoAlmacenado.getId()) {
                    return nodoAlmacenado;
                }
            }
        }
//        System.out.println("Node no encontrado");
        return null;
    }

    public Node findEdgeInGraph(int id) {
        if (!nodes.isEmpty()) {
            Iterator nodosIterator = nodes.iterator();
            while (nodosIterator.hasNext()) {
                Node nodoAlmacenado = (Node) nodosIterator.next();
                Iterator adyacentesIterator = nodoAlmacenado.getEdges().iterator();
                while (adyacentesIterator.hasNext()) {
                    Edge adyacente = (Edge) adyacentesIterator.next();
                    if (id == adyacente.getId()) {
                        return nodoAlmacenado;
                    } else {
                        return null;
                    }
                }
            }
        }return null;
    }


    public int findNodeByIndex(int index) {
        int id = 0;
//        System.out.println("Buscando nodo de Index: " + index);
        if (!nodes.isEmpty()) {
            Iterator nodosIterator = nodes.iterator();
            while (nodosIterator.hasNext()) {
                Node nodoAlmacenado = (Node) nodosIterator.next();
                if (index == nodes.indexOf(nodoAlmacenado)) {
//                    System.out.println("Node encontrado");
                    id = nodoAlmacenado.getId();
                }
            }
        } else {
//            System.out.println("Node no encontrado");
        }
        return id;
    }

    public boolean nodeExist(int id) {
        boolean flag = false;
        Node nodo = this.findNode(id);
        if (nodo != null) {
            flag = true;
        }
        return flag;

    }

    public boolean removeNode(int id) {
        Node nodo = this.findNode(id);
        boolean flag = false;
        if (nodo != null) {
            flag = this.nodes.remove(nodo);
        }
        return flag;
    }

    public void printGraph() {
        if (this.nodes.isEmpty()) {
            System.out.println("ERROR: GRAFO VACIO");
        } else {
            Iterator nodosIterator = nodes.iterator();
            while (nodosIterator.hasNext()) {
                Node nodoActual = (Node) nodosIterator.next();
                nodoActual.printNode();

            }
        }
    }

    public int countNodes() {
        int cantidad = 0;
        if (nodes != null) {
            ArrayList nodosList = (ArrayList) nodes;
            cantidad = nodosList.size();
        }
        return cantidad;
    }


    public int minDistance(Distance distance) {

        float distanciaMinima = Float.POSITIVE_INFINITY;
        int idNodoMenor = -1;
        if (distance != null) {
            Iterator distanciasIterator = distance.nodes.iterator();
            while (distanciasIterator.hasNext()) {
                Node a = (Node) distanciasIterator.next();
                int i = a.getId();
                if (distance.getNodeDistance(i) < distanciaMinima) {
                    idNodoMenor = i;
                    distanciaMinima = (int) distance.getNodeDistance(i);

                }
            }

        }
        return idNodoMenor;
    }

    public Distance rAncho(int id,int id_final) {
        int index;
        Distance vectorDistancia = null;
        float[] distancia = new float[this.nodes.size()];
        int[] antecesor = new int[this.nodes.size()];
        boolean[] visitado = new boolean[this.nodes.size()];
        ArrayList nodosTodos = new ArrayList();
        if (nodes != null) {
            for (int i = 0; i < this.nodes.size(); i++) {
                distancia[i] = Float.POSITIVE_INFINITY;
                antecesor[i] = Integer.MIN_VALUE;
                visitado[i] = false;
            }
            index = nodes.indexOf(findNode(id));
            if (index != -1) {
                distancia[index] = 0;
                visitado[index] = true;
                LinkedList cola = new LinkedList();
                cola.add(index);
                while (cola.isEmpty() == false) {
                    int u = (Integer) cola.pop();
                    int x = this.findNodeByIndex(u);

                    if(this.findNodeByIndex(u)== id_final){
                        cola.clear();
                        System.out.println("Encontrado!");
                    }
                    else
                    {
                    ArrayList adyacentes = this.findNode(x).getEdges();
//                    this.findNode(x).printEdges();
                    if (adyacentes != null) {
                        Iterator adyacentesIterator = adyacentes.iterator();
                        while (adyacentesIterator.hasNext()) {
                            Edge a = (Edge) adyacentesIterator.next();
                            int v = nodes.indexOf(findNode(a.getId()));
                            if (visitado[v] == false) {
                                cola.addLast(v);
                                antecesor[v] = u;
                                distancia[v] = distancia[u] + 1;
                                visitado[v] = true;
                            } 
                        }
                    
                  }
                }
                }
            }
            int tamaño = 0;
            for (int r = 0; r < nodes.size(); r++) {
                if (distancia[r] != Float.POSITIVE_INFINITY) {
                    if (antecesor[r] != Integer.MIN_VALUE) {
                        tamaño++;
                    }
                }
            }
            System.out.println("Tamaño : " + tamaño);
            float[] distanciasTotal = new float[(tamaño + 1)];
            int[] padresTotal = new int[(tamaño + 1)];

            Iterator nods = nodes.iterator();
            distanciasTotal[0] = distancia[0];
            padresTotal[0] = antecesor[0];
            nodosTodos.add(id);
            int contador = 1;
            while (nods.hasNext()) {
                for (int y = 0; y < nodes.size(); y++) {
                    Node nodo = (Node) nods.next();

                    if (distancia[y] != Float.POSITIVE_INFINITY) {
                        if (antecesor[y] != Integer.MIN_VALUE) {
                            distanciasTotal[contador] = distancia[y];
                            padresTotal[contador] = this.findNodeByIndex(antecesor[y]);
                            contador++;

                            nodosTodos.add(this.findNodeByIndex(y));
                        }
                    }
                }
            }
            
            vectorDistancia = new Distance(nodosTodos, distanciasTotal, padresTotal);

        }
//
//
//        System.out.print("Nodos \t");
//        for (int i = 0; i < nodosTodos.size(); i++) {
//            System.out.print(i + " \t");
//            System.out.println("(" + nodosTodos.get(i).toString() + ")");
//        }
//        System.out.println("");
//        System.out.print("dist: \t");
//        for (int i = 0; i < nodosTodos.size(); i++) {
//            System.out.print(distancia[i] + " \t");
//        }
//        System.out.println("");
//        System.out.print("ante: \t");
//        for (int i = 0; i < nodosTodos.size(); i++) {
//            if (antecesor[i] != Integer.MIN_VALUE) {
//                System.out.print(antecesor[i] + " \t");
//            } else {
//                System.out.print(null + " \t");
//            }
//
//        }
//        System.out.println("");
//        System.out.print("visi: \t");
//        for (int i = 0; i < nodosTodos.size(); i++) {
//            System.out.print(visitado[i] + " \t");
//        }
        return vectorDistancia;

    }

    public Distance Dijkstra(int id,int id_final) {
        int index;
        Distance vectorDistancia = null;
        float[] distancia = new float[this.nodes.size()];
        int[] antecesor = new int[this.nodes.size()];
        ArrayList nodosTodos = new ArrayList();

        if (nodes != null) {
            for (int i = 0; i < this.nodes.size(); i++) {
                distancia[i] = Float.POSITIVE_INFINITY;
                antecesor[i] = Integer.MIN_VALUE;
            }
            index = nodes.indexOf(findNode(id));
            if (index != -1) {
               //System.out.println("=========================================");
                distancia[index] = 0;
                LinkedList cola = new LinkedList();
                cola.add(index);
                int u;
                while (cola.isEmpty() == false) {
                    Float menorpesoencola = Float.MAX_VALUE;
                    int indexmenorencolado=-1;
                    Iterator colaIterator = cola.iterator();
                    while(colaIterator.hasNext()){
                        int encolado = (Integer) colaIterator.next();

                        if(distancia[encolado]<menorpesoencola){
                            
                            menorpesoencola=distancia[encolado];
                            indexmenorencolado=cola.indexOf(encolado);
                            
                        }

                    }
                  
                    u = (Integer) cola.get(indexmenorencolado);
                    cola.remove(indexmenorencolado);
                   


                    //sacar el de menor peso. 

                    if(this.findNodeByIndex(u)== id_final){
                        cola.clear();
                        System.out.println("Cola size: " + cola.size() + " y nodo: " + this.findNodeByIndex(u));
                    }
                    else{
                    ArrayList adyacentes = this.findNode(this.findNodeByIndex(u)).getEdges();
                    if (adyacentes != null) {
                        Iterator adyacentesIterator = adyacentes.iterator();
                        while (adyacentesIterator.hasNext()) {
                            
                            Edge a = (Edge) adyacentesIterator.next();

                            int v = nodes.indexOf(findNode(a.getId()));
                            if (distancia[v] > distancia[u] + a.getWeight()) {
                                distancia[v] = (float) (distancia[u] + a.getWeight());
                                antecesor[v] = u;
                                cola.addLast(v);
                               }
                            }
                        }
                    }
                }
            }
            int tamaño = 0;
            for (int r = 0; r < nodes.size(); r++) {
                if (distancia[r] != Float.POSITIVE_INFINITY) {
                    if (antecesor[r] != Integer.MIN_VALUE) {
                        tamaño++;
                    }
                }
            }
            System.out.println("Tamaño : " + tamaño);
            float[] distanciasTotal = new float[(tamaño + 1)];
            int[] padresTotal = new int[(tamaño + 1)];
            
            Iterator nods = nodes.iterator();
            distanciasTotal[0] = distancia[0];
            padresTotal[0] = antecesor[0];
            nodosTodos.add(id);
            int contador = 1;
            while (nods.hasNext()) {
                for (int y = 0; y < nodes.size(); y++) {
                    Node nodo = (Node) nods.next();

                    if (distancia[y] != Float.POSITIVE_INFINITY) {
                        if (antecesor[y] != Integer.MIN_VALUE) {
                            distanciasTotal[contador] = distancia[y];
                            padresTotal[contador] = this.findNodeByIndex(antecesor[y]);
                            contador++;

                            nodosTodos.add(this.findNodeByIndex(y));
                        }
                    }
                }
            }
//            
            vectorDistancia = new Distance(nodosTodos, distanciasTotal, padresTotal);
        }
//        System.out.print("Id \t");
//        for (int i = 0; i < nodosTodos.size(); i++) {
//            if (distancia[i] != Float.POSITIVE_INFINITY) {
//                System.out.print(this.findNodeByIndex(i) + " \t");
//            }
//        }
//        System.out.println("");
//        System.out.print("Nodos \t");
//        for (int i = 0; i < nodosTodos.size(); i++) {
//            if (distancia[i] != Float.POSITIVE_INFINITY) {
//                System.out.print(i + " \t");
//            }
//        }
//        System.out.println("");
//        System.out.print("dist: \t");
//        for (int i = 0; i < nodosTodos.size(); i++) {
//            if (distancia[i] != Float.POSITIVE_INFINITY) {
//                System.out.print(distancia[i] + " \t");
//            }
//        }
//        System.out.println("");
//        System.out.print("ante: \t");
//        for (int i = 0; i < nodosTodos.size(); i++) {
//            if (distancia[i] != Float.POSITIVE_INFINITY) {
//                if (antecesor[i] != Integer.MIN_VALUE) {
//                    System.out.print(antecesor[i] + " \t");
//                } else {
//                    System.out.print(null + " \t");
//                }
//            }
//        }
//        System.out.println("");

        return vectorDistancia;
    }

    public Distance DijkstraTruncado(int id,int maxDistance) {
        int index;
        Distance vectorDistancia;
        float[] distancia = new float[this.nodes.size()];
        int[] antecesor = new int[this.nodes.size()];

        if (nodes != null) {
            for (int i = 0; i < this.nodes.size(); i++) {
                distancia[i] = Float.POSITIVE_INFINITY;
                antecesor[i] = Integer.MIN_VALUE;
            }
            index = nodes.indexOf(findNode(id));
            if (index != -1) {
//                System.out.println("=========================================");
                distancia[index] = 0;
                LinkedList cola = new LinkedList();
                cola.add(index);
                int u;
                while (cola.isEmpty() == false) {
                    Float menorpesoencola = Float.MAX_VALUE;
                    int indexmenorencolado=-1;
                    Iterator colaIterator = cola.iterator();
                    while(colaIterator.hasNext()){
                        int encolado = (Integer) colaIterator.next();

                        if(distancia[encolado]<menorpesoencola){
                            menorpesoencola=distancia[encolado];
                            indexmenorencolado=cola.indexOf(encolado);

                        }

                    }
                   
                    u = (Integer) cola.get(indexmenorencolado);
                    cola.remove(indexmenorencolado);
                   

                    ArrayList adyacentes = this.findNode(this.findNodeByIndex(u)).getEdges();
                    if (adyacentes != null) {
                        Iterator adyacentesIterator = adyacentes.iterator();
                        while (adyacentesIterator.hasNext()) {
//                            System.out.println("ENTRO AL WHILE--------->");
                            Edge a = (Edge) adyacentesIterator.next();
                            int v = nodes.indexOf(findNode(a.getId()));
                            if (distancia[v] > distancia[u] + a.getWeight() && maxDistance > distancia[u] + a.getWeight()) {
                                distancia[v] = (float) (distancia[u] + a.getWeight());
                                antecesor[v] = u;
                                cola.addLast(v);
//                                System.out.println("Ante: " + this.findNodeByIndex(antecesor[v]));
                            }
                        }
                    }

            }
            }
        }
        int tamaño = 0;
        for (int r = 0; r < nodes.size(); r++) {
            if (distancia[r] != Float.POSITIVE_INFINITY) {
                if (antecesor[r] != Integer.MIN_VALUE) {
                    tamaño++;
                }
            }
        }
        System.out.println("Tamaño : " + tamaño);
        float[] distanciasTotal = new float[(tamaño+1)];
        int[] padresTotal = new int[(tamaño+1)];
        ArrayList nodosTodos = new ArrayList();
        Iterator nods = nodes.iterator();
        distanciasTotal[0] = distancia[0];
        padresTotal[0] = antecesor[0];
        nodosTodos.add(id);
        int contador = 1;
        while (nods.hasNext()) {
            for (int y = 0; y < nodes.size(); y++) {
                Node nodo = (Node) nods.next();
                if (distancia[y] != Float.POSITIVE_INFINITY) {
                    if (antecesor[y] != Integer.MIN_VALUE) {
                        distanciasTotal[contador] = distancia[y];
                        padresTotal[contador] = this.findNodeByIndex(antecesor[y]);
                        contador++;
                        nodosTodos.add(this.findNodeByIndex(y));
                    }
                }
            }
        }

        vectorDistancia = new Distance(nodosTodos, distanciasTotal, padresTotal);

//        System.out.print("Id \t");
//        for (int i = 0; i < this.nodes.size(); i++) {
//            if (distancia[i] != Float.POSITIVE_INFINITY) {
//                System.out.print(this.findNodeByIndex(i) + " \t");
//            }
//        }
//        System.out.println("");
//        System.out.print("Nodos \t");
//        for (int i = 0; i < this.nodes.size(); i++) {
//            if (distancia[i] != Float.POSITIVE_INFINITY) {
//                System.out.print(i + " \t");
//            }
//        }
//        System.out.println("");
//        System.out.print("dist: \t");
//        for (int i = 0; i < this.nodes.size(); i++) {
//            if (distancia[i] != Float.POSITIVE_INFINITY) {
//                System.out.print(distancia[i] + " \t");
//            }
//        }
//        System.out.println("");
//        System.out.print("ante: \t");
//        for (int i = 0; i < this.nodes.size(); i++) {
//            if (distancia[i] != Float.POSITIVE_INFINITY) {
//                if (antecesor[i] != Integer.MIN_VALUE) {
//                    System.out.print(antecesor[i] + " \t");
//                } else {
//                    System.out.print(null + " \t");
//                }
//            }
//        }
//
//        System.out.println("");

     return vectorDistancia;
}

    public void checkGraph(){
        Iterator grafoIterator = this.getNodes().iterator();
        while(grafoIterator.hasNext()){
            Node nodo = (Node) grafoIterator.next();
            Iterator adyIterator = nodo.getEdges().iterator();
            while(adyIterator.hasNext()){
                Edge ady= (Edge) adyIterator.next();
                Node adyacente = this.findNode(ady.getId());
                
                   adyacente.findEdge(nodo.getId());
                   System.out.println("El nodo " + nodo.getId() + " está incompleto");
      

            }

        }


    }
    }


