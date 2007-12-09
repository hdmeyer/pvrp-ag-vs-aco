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
public class TestCruce {
    
    /** Creates a new instance of TestCromosoma */
    public TestCruce() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        // TODO code application logic here
        Conocimiento prueba= new Conocimiento();
        String filename = "C:\\pvrp\\p26";
        
     
        prueba.CargarConocimiento(filename);
        Cromosoma cromo = new Cromosoma(prueba);
        cromo.construirCromosoma(prueba);
                
        System.out.println("1. PRUEBA DE OPERCIÓN DE CRUCE");
        System.out.println("-* Población Inicial Generada =========================================");
        
        Poblacion p = new Poblacion(prueba, 10, 30);
        
        p.evaluar();
        
        String popString = p.toStringImprimible();        
        System.out.print(popString);        
        
        
        // SELECCIÓN BÁSICA...
        
        int i = 0;
        while (i<=10) {
            i++;
            Cromosoma[] selectos = p.getIndividuos();
            p.cruce(selectos);
            System.out.println("Iteración "+i+": Invalidos -> "+p.hayInvalidos());
            //p.reemplazar();
            //p.evaluar();
            //System.out.println("Iteración "+i+": Invalidos -> "+p.hayInvalidos());
            
        
            popString = p.toStringImprimible();        
            System.out.println(popString);
            System.out.print("INVALIDOS: --> ");
            
            for (int j = 0; j < p.getInvalidos().length; j++) {
                
                System.out.print(" - "+p.getInvalidos()[j]+" - ");

                
            }

        }
        
        System.out.println("-* ("+i+") vueltas Población Cruzada  =========================================");
                
        
    }
    
}
