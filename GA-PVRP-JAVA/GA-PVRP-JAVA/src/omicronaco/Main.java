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
        
        // OJO! Los nombres utilizados en algunas secciones del trabajo son 
        // producto de interminables madrugadas sin dormir y por lo tanto
        // pueden estar cargados de ironía, humor negro, rabia, y otros calificativos
        // poco felices...
        
        String problemInstanceFile;
        String bestSolutionFile;
        String testNumber;        
        String instanceNumber;
        String solutionNumber;
       
        /**
         * lectura de parámatros iniciales, preparación del algoritmo. 
         * 
         */
        
        if (args.length == 3) {
            problemInstanceFile = args[0];
            bestSolutionFile = args[1];
            testNumber = args[2];
            instanceNumber = args[0];
            solutionNumber = args[1];
            
        } else if (args.length == 2) {
            instanceNumber = args[0];
            solutionNumber = args[0];
            problemInstanceFile = "pvrp\\p"+args[0];
            bestSolutionFile = "pvrp-sols\\p"+args[0]+".res";
            testNumber = args[1];
        } else {
            problemInstanceFile = "pvrp\\p26";
            bestSolutionFile = "pvrp-sols\\p26.res";
            instanceNumber = "26";
            solutionNumber = "26";
            testNumber = "PRUEBA";
        }

        //impresión de título
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("-----------------> Periodic Vehicle Routing Problem <------------------\n");
        
        System.out.println("I. CARGANDO EL CONOCIMIENTO DE LA INSTANCIA '"+problemInstanceFile+"'\n");
              
        Conocimiento conocimiento = new Conocimiento();
        try {
            conocimiento.CargarConocimiento(problemInstanceFile);
            conocimiento.LeerSolucion(bestSolutionFile);
            
            // en homenaje a un héroe de nuestra infancia
            OACO optimusPrime = new OACO(conocimiento);
            
            // paraguayización de nuestro héroe
            for (int prueba = 0; prueba < 10; prueba++) {
        
                System.out.println("############################# -- PRUEBA NRO. " + prueba + " -- #############################");

                optimusPrime.elPuretePorTiempo(conocimiento,5, prueba, instanceNumber);
                
                System.out.println("######################## -- PRUEBA NRO. " + prueba + " FINALIZADO -- ########################");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("No se pudo leer correctamente el archivo");
        }
    }
    
}
