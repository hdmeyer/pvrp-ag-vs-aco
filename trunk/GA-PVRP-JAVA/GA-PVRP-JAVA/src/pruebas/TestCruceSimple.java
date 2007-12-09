/*
 * TestCromosoma.java
 *
 * Created on 31 de octubre de 2007, 19:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package pruebas;

import gapvrpjava.*;

/**
 *
 * @author Huguis
 */
public class TestCruceSimple {
    
    /** Creates a new instance of TestCromosoma */
    public TestCruceSimple() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        // TODO code application logic here
        Conocimiento prueba= new Conocimiento();
        String filename = "C:\\pvrp\\p26";
        int ITERACIONES = 1;
        
     
        prueba.CargarConocimiento(filename);                
        System.out.println("1. PRUEBA DE OPERCI�N DE CRUCE");
        System.out.println("-* Poblaci�n Inicial Generada =========================================");
        
        Poblacion p = new Poblacion(prueba, 2, 30);        
        p.evaluar();
        
        String popString = p.toStringImprimible();        
        System.out.print(popString);        
                
        // SELECCI�N B�SICA...        
        int i = 0;
        while (i<=ITERACIONES) {
            i++;
            
            // SELECCI�N ABSOLUTA
            Cromosoma[] selectos = p.getIndividuos();
            p.cruce(selectos);
            //p.reemplazar();
            p.setIndividuos(p.getHijos());
            p.evaluar();        
            popString = p.toStringImprimible();        
            System.out.println(popString);
            System.out.print("INVALIDOS: --> "+p.hayInvalidos()+": ");
            
            for (int j = 0; j < p.getInvalidos().length; j++) {
                System.out.print(" - "+p.getInvalidos()[j]+" - ");                
            }
        }
        
        System.out.println("\n-* ("+i+") vueltas Poblaci�n Cruzada  =========================================");
                
        
    }
    
}
