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
public class Route {

    private int id;
    private ArrayList routeNode;
    private int mode;
    private String name;

    public Route() {
        this.routeNode = new ArrayList();
    }

    public Route(ArrayList routeNode, int mode, int id) {
        this.id=id;
        this.routeNode = routeNode;
        this.mode = mode;
        this.name = "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Route(int mode, int id) {
        this.id=id;
        this.routeNode = new ArrayList();
        this.mode = mode;
    }
    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Route(int id, ArrayList routes) {
        this.id = id;
        this.routeNode = routes;
    }

    public Route(ArrayList routes) {
        this.routeNode = routes;
    }

    public ArrayList getRoutes() {
        return routeNode;
    }

    public void setRoutes(ArrayList routes) {
        this.routeNode = routes;
    }

    public void printRoute() {
        if (this.routeNode.isEmpty()) {
            //System.out.println("Epa, no teneis un sevillo en el grafo");
        } else {
            Iterator nodosIterator = routeNode.iterator();
            //System.out.println("Ruta: " + this.getId() + " Modo :" + this.getMode() + "Nombre: " + this.getName());
            while (nodosIterator.hasNext()) {
                //System.out.println("************************");
                RouteNode nodoActual = (RouteNode) nodosIterator.next();
                //System.out.println("Nodo ID: " + nodoActual.getId() + " Index: " + this.routeNode.indexOf(nodoActual));
                Iterator adys = nodoActual.getEdges().iterator();
                while(adys.hasNext()){
                    Edge adyace = (Edge) adys.next();
                    //System.out.println("Adyacentes: " + adyace.getId() + " PESO: " + adyace.getWeight());
                  
                }

            }
        }
    }

    public RouteNode findRouteNode(int id) {
        if (!routeNode.isEmpty()) {
            Iterator nodosIterator = routeNode.iterator();
            while (nodosIterator.hasNext()) {
                RouteNode nodoAlmacenado = (RouteNode) nodosIterator.next();
                if (id == nodoAlmacenado.getId()) {
                    return nodoAlmacenado;
                }
            }
        }
        //System.out.println("Nodo no encontrado");
        return null;

    }

        public RouteNode findEdgeInRoute(int id) {
        if (routeNode.size() != 0) {
            Iterator nodosIterator = routeNode.iterator();
            while (nodosIterator.hasNext()) {
                RouteNode nodoAlmacenado = (RouteNode) nodosIterator.next();
                Iterator adyacentesIterator = nodoAlmacenado.getEdges().iterator();
                while (adyacentesIterator.hasNext()) {
                    Edge adyacente = (Edge) adyacentesIterator.next();
                    if (id == adyacente.getId()) {
                        //System.out.println("Nodo88: " + nodoAlmacenado.getId() + " Adyacente88: " + adyacente.getId());
                        return nodoAlmacenado;
                    } else {
                        return null;
                    }
                }
            }
        }return null;
    }
    public void setSingleNode(RouteNode node) {
        if (!this.nodeExist(node.getId())) {
            this.routeNode.add(node);
//            //System.out.println("Nodo con id: " + nodo.getId() + " insertado en el grafo");
        } else {
            //System.out.println("Este id de nodo ya existe");
        }
    }
        public boolean nodeExist(int id) {
        boolean flag = false;
        RouteNode nodo = this.findRouteNode(id);
        if (nodo != null) {
            flag = true;
        }
        return flag;

    }


}
