/*
 * Main.java
 *
 * Created on 31 de octubre de 2007, 19:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gapvrpjava;

/**
 *
 * @author Huguis
 */
public class Main {
    
    /** Creates a new instance of Main */
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        // TODO code application logic here
        Conocimiento prueba= new Conocimiento();
        prueba.CargarConocimiento("C:\\p26");
        Cromosoma cromo = new Cromosoma(prueba);
        cromo.construirCromosoma(prueba);
        System.out.println(cromo.ImprimirCromo(cromo.toString()));
        
    }
    
}
