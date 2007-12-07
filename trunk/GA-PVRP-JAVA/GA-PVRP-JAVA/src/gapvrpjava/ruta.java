/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gapvrpjava;

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
    

}
