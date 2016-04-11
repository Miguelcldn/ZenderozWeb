/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package manejador;

import estructuras.Path;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Admin
 */
public class Paths {

    private ArrayList paths;

    public Paths(ArrayList paths) {
        this.paths = paths;
    }

    public Paths() {
        this.paths = new ArrayList();
    }
    
    public ArrayList getPaths() {
        return paths;
    }

    public void setPaths(ArrayList paths) {
        this.paths = paths;
    }

    public boolean pathExist(int id){
        boolean flag = false;
        Path camino = this.findPath(id);
        if(camino != null){
            flag = true;
        }
        return flag;

    }

    public Path findPath(int id){
        //System.out.println("Buscando camino de ID: " + id);
        if(!paths.isEmpty()){
           Iterator caminosIterator = paths.iterator();
            while (caminosIterator.hasNext()) {
                Path caminoAlmacenado = (Path) caminosIterator.next();
                if (id == caminoAlmacenado.getId()){
                    //System.out.println("Camino encontrado");
                    return caminoAlmacenado;
                }
            }
        }
        //System.out.println("Camino no encontrado");
        return null;
    }

    public void setSinglePath(Path camino){
       if (!this.pathExist(camino.getId())) {
           this.paths.add(camino);
           //System.out.println("Camino con id: " + camino.getId() + " insertado en la lista");
       }
       else
           System.out.println("Este id de camino ya existe");
    }

    public boolean removePath (int id){
       Path camino = this.findPath(id);
       boolean flag = false;
       if(camino != null){
       flag = this.paths.remove(camino);
       }
       return flag;
   }

    public void printPaths(){

           Iterator caminosIterator = paths.iterator();
            while (caminosIterator.hasNext()) {
                Path camino = (Path) caminosIterator.next();
                //System.out.println("  "+ camino.getName() + " " + camino.getNodes());

                
       
            }
    }

}
