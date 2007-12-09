/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gapvrpjava;

import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author Cristhian Parra-Hugo Meyer
 */
public class ruta {
    Vector ruta;
    double costo;
    public void getCosto(){};
    public ruta(Conocimiento entrada){
        ruta = new Vector(entrada.cantClientes);        
    }
    
    public double calcularCosto(double[][] matrizCostos) {
        this.costo = 0;

        if (this.ruta.size() > 0) {
            Iterator<Integer> it = this.ruta.iterator();
            int actual = 0;
            int posterior = (int) it.next();
            while(it.hasNext()){

                this.costo += matrizCostos[actual][posterior];
                actual = posterior;
                posterior = (int) it.next();
                
            }           
            
            this.costo += matrizCostos[actual][posterior] + matrizCostos[posterior][0];

        }
        
        return this.costo;          
    }
    

}
