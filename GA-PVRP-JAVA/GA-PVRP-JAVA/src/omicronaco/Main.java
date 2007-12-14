/*
 * Main.java
 *
 * Created on 11 de diciembre de 2007, 8:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package omicronaco;

import java.io.IOException;

/**
 *
 * @author Propietario
 */
public class Main {
    
    /** Creates a new instance of Main */
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Conocimiento prueba = new Conocimiento();
        try {
            prueba.CargarConocimiento("C:\\p26");
            OACO optimusPrime = new OACO(prueba);
            optimusPrime.elPurete(prueba,100);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("No se pudo leer correctamente el archivo");
        }
    }
    
}
