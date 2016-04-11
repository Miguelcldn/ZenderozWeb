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
public class Routes {

    private ArrayList routes;

    public Routes() {
        this.routes = new ArrayList();
    }

    public Routes(ArrayList routes) {
        this.routes = routes;
    }

    public ArrayList getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList routes) {
        this.routes = routes;
    }

    public void printRoutes() {

        Iterator rutasTodas = this.routes.iterator();
        while (rutasTodas.hasNext()) {
            Route ruta = (Route) rutasTodas.next();
            //System.out.println("=====================");
            //System.out.println("Ruta ID: " + ruta.getId() + " Size: " + ruta.getRoutes().size());
            ruta.printRoute();
//            Iterator rutaIterator = ruta.getRoutes().iterator();
//            while (rutaIterator.hasNext()) {
//                NodoRuta nodo = (NodoRuta) rutaIterator.next();
//                 //System.out.println("Nodo: " + nodo.getId());
//                Iterator adyacenteIterator = nodo.getAdyacent().iterator();
//                while (adyacenteIterator.hasNext()) {
//                    Adyacente adyacente = (Adyacente) adyacenteIterator.next();
//                    //System.out.println("        Adyacente: " + adyacente.getId());
//                    //System.out.println("        Peso: " + adyacente.getPeso());
//
//                }
//            }
            //System.out.println("==========================");

        }
    }

    public boolean routeExist(int id) {
        boolean flag = false;
        Route ruta = this.findRoute(id);
        if (ruta != null) {
            flag = true;
        }
        return flag;

    }

    public void setSingleRoute(Route ruta) {
        if (!this.routeExist(ruta.getId())) {
            this.routes.add(ruta);
            //System.out.println("RUTA con id: " + ruta.getId() + " insertado en las RUTAS");
        } else {
            //System.out.println("Este id de ruta ya existe");
        }
    }

    public void replaceRoute(Route newRoute) {
        int id = newRoute.getId();
        if (this.findRoute(id)!=null) {
            int index = this.getRoutes().indexOf(findRoute(id));
            this.getRoutes().set(index, newRoute);
            //System.out.println("Reemplazada!");
        } else {
            this.setSingleRoute(newRoute);
        }
        //System.out.println("mmmmmmmm");
    }

    public Route findRoute(int routeID) {
        if (this.routes != null) {
            Iterator rutasTodas = this.routes.iterator();
            while (rutasTodas.hasNext()) {
                Route rutaActual = (Route) rutasTodas.next();
                if (routeID == rutaActual.getId()) {
                    return rutaActual;
                }
            }
        }
        return null;
    }
}
