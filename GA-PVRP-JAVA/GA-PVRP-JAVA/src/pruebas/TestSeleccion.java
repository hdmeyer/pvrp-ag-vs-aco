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
public class TestSeleccion {
    
    /** Creates a new instance of TestCromosoma */
    public TestSeleccion() {
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
                
        System.out.println("1. PRUEBA DE INICIALIZAR POBLACION");
        System.out.println("-* Población Inicial Generada =========================================");
        
        Poblacion p = new Poblacion(prueba, 30, 30);
        
        p.evaluar(prueba);
        
        String popString = p.toStringPorCromosoma(p.toString());
        System.out.print(popString);        
        
        Cromosoma[] selectos = p.seleccion();
        
        System.out.println("-* Emparejamientos =========================================");
        
        for (int i = 0; i < selectos.length; i++) {
            Cromosoma cromosoma = selectos[i];
            
            System.out.println("Individuo "+(i+1)+": "+cromosoma.toString());
            System.out.println("Fitness "+(i+1)+": "+cromosoma.getFitness());
            System.out.println("------------------------------------------------------------");

        }
        
        
                
             
        
    }
    
}
