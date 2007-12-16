/*
 * Camino.java
 *
 * Created on 11 de diciembre de 2007, 10:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package omicronaco;

import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author Hugo Meyer- Cristhian Parra
 */
public class Camino {
    private Vector<Integer> ruta;
    private double costo;
    private double [][] matrizCostos;
    /** Creates a new instance of Camino */
    public Camino(Conocimiento entrada) {
        ruta = new Vector(entrada.cantClientes);
        ruta.add(0);
        this.setMatrizCostos(entrada.matrizCostos);
        this.costo = 0;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public Vector<Integer> getRuta() {
        return ruta;
    }

    public void setRuta(Vector<Integer> ruta) {
        this.ruta = ruta;
    }
    
    public void calcularCosto(){
        
        if (this.getRuta().size() > 0) {
            
            Iterator<Integer> it = this.getRuta().iterator();
            int actual = (int) it.next();
            int posterior = 0;
            while(it.hasNext()){
                
                posterior = (int) it.next();
                this.costo += this.getMatrizCostos()[actual][posterior];
                actual = posterior;
            }        
            /*Sumamos tb el trayecto desde el penultimo al ultimo
             *y desde el ultimo al nodo 0*/
            this.costo += matrizCostos[actual][posterior] + matrizCostos[posterior][0];
        }
    }

    public double[][] getMatrizCostos() {
        return matrizCostos;
    }

    public void setMatrizCostos(double[][] matrizCostos) {
        this.matrizCostos = matrizCostos;
    }
    
    public String toString(){
        String retorno = "[";
        Iterator<Integer> it = this.getRuta().iterator();
        while(it.hasNext()){
            retorno += it.next().toString() + ",";
        }
        retorno += "]";
        return retorno;
    }
    
}
