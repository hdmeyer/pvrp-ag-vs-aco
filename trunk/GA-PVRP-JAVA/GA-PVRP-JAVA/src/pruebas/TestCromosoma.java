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
public class TestCromosoma {
    
    /** Creates a new instance of TestCromosoma */
    public TestCromosoma() {
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
                
        System.out.println("1. PRUEBA DE GENERACIÓN DEL CROMOSOMA");
        System.out.println("-* Cromosoma Generado ================================================");
        
        System.out.println(cromo.ImprimirCromo(cromo.toString()));
        cromo.fObjetivo(prueba);
        System.out.println("=======================================================================");
        System.out.println("| FITNESS: "+cromo.getFitness()+"                                          |");
        System.out.println("=======================================================================");
        System.out.println();
        System.out.println("2. PRUEBA DE INICIALIZAR POBLACION");
        System.out.println("-* Población Inicial Generada =========================================");
        
        Poblacion p = new Poblacion(prueba, 30, 30);
        
        p.evaluar();
        
        String popString = p.toStringImprimible();
        
        System.out.print(popString);        
        
        System.out.println("-* Imprimir población versión 2 =========================================");
        
        popString = p.toStringPorCromosoma(p.toString());
        
        System.out.print(popString);        
        
    }
    
}
