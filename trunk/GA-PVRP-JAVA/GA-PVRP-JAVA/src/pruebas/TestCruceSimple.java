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
        int ITERACIONES = 10;
        
     
        prueba.CargarConocimiento(filename);                
        System.out.println("1. PRUEBA DE OPERCIÓN DE CRUCE");
        System.out.println("-* Población Inicial Generada =========================================");
        
        Poblacion p = new Poblacion(prueba, 2, 30);        
        p.evaluar();
        
        String popString = p.toStringImprimible();        
        System.out.print(popString);        
                
        // SELECCIÓN BÁSICA...        
        int i = 1;
        while (i<=ITERACIONES) {
            i++;
            
            // SELECCIÓN ABSOLUTA
            Cromosoma[] selectos = p.getIndividuos();
            p.cruce(selectos);
            //p.reemplazar();
            p.setIndividuos(p.getHijos());
            //p.evaluar();        
            popString = p.toStringImprimible();        
            System.out.println(popString);
            System.out.print("INVALIDOS: --> "+p.hayInvalidos()+": ");
            
            for (int j = 0; j < p.getInvalidos().length; j++) {
                System.out.print(" - "+p.getInvalidos()[j]+" - ");                
            }            
            
            System.out.println("\n\n");
            
            for (int j = 0; j < p.getTamanho(); j++) {
                
                System.out.print("ListaVisitasCromo "+j+": --> ");
                
                int [] visitasGlobales = p.getIndividuo(j).listaVisitasCromo[1];
                
                for (int k = 0; k < visitasGlobales.length; k++) {
                    int l = visitasGlobales[k];                
                    System.out.print(" - "+l+" - ");                
                }                
                System.out.println("\n\n");

            }
        }
        
        System.out.println("\n-* ("+i+") vueltas Población Cruzada  =========================================");
                
        
    }
    
}
