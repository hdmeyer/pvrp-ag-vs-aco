/*
 * Nodo.java
 *
 * Created on 12 de diciembre de 2007, 11:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package omicronaco;

/**
 *
 * @author Propietario
 */
public class Nodo {
    
    private int idNodo;                
    private double probabilidad;     
    private boolean disponible;
    
    public Nodo() {
        this.idNodo = -1;
        this.probabilidad = -1;
        this.setDisponible(true);
    }
    
    public Nodo (int nodo, double probabilidad) {
        this.idNodo = nodo;
        this.probabilidad = probabilidad;
        this.setDisponible(true);
    }

    public int getIdNodo() {
        return idNodo;
    }

    public void setIdNodo(int idNodo) {
        this.idNodo = idNodo;
    }

    public double getProbabilidad() {
        return probabilidad;
    }

    public void setProbabilidad(double probabilidad) {
        this.probabilidad = probabilidad;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    public int comparaNodos(Nodo n){
        
        if(this.getProbabilidad() > n.getProbabilidad()){
            return 1;
        }else if(this.getProbabilidad() < n.getProbabilidad()){
            return -1;
        }else{
            return 0;
        }
    }
}
