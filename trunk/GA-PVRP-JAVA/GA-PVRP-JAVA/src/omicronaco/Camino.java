/*
 * Camino.java
 *
 * Created on 11 de diciembre de 2007, 10:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package omicronaco;

import java.util.Vector;

/**
 *
 * @author Hugo Meyer- Cristhian Parra
 */
public class Camino {
    private Vector<Integer> ruta;
    private double costo;
    /** Creates a new instance of Camino */
    public Camino(Conocimiento entrada) {
        ruta = new Vector(entrada.cantClientes);
        ruta.add(0);
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
    
}
