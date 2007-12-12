/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gapvrpjava;

import java.io.IOException;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.FileWriter;
/**
 *
 * @author Cristhian Parra
 */
public class PVRP_AG_Main {
    private static String SOL_DIR = "D:\\cparra\\Cparra\\Estudios\\FACU\\2007\\8voSemestre\\ia\\tp-ia\\fuentes\\GA-PVRP-JAVA\\GA-PVRP-JAVA\\sols";

    private static int CANT_GENERACIONES = 50000;
    private static int PROB_MUTACION = 30;
    private static int TAM_POBLACION = 40;
    private static int MAX_TIEMPO = 5; // EN MINUTOS

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        Poblacion poblacion;
        Conocimiento conocimiento;
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
            problemInstanceFile = "C:\\pvrp\\p"+args[0];
            bestSolutionFile = "C:\\pvrp-sols\\p"+args[0]+".res";
            testNumber = args[1];
        } else {
            problemInstanceFile = "C:\\pvrp\\p26";
            bestSolutionFile = "C:\\pvrp-sols\\p26.res";
            instanceNumber = "26";
            solutionNumber = "26";
            testNumber = "PRUEBA";
        }


        /*
         * Variables para calculo de Resultados.
         *  Correr por: 5 min (300 seg)
         *  Tomar muestra cada: 5 seg.
         */
        long muestra = 5000; 
        String[] tiempos = new String[70];
        String[]  fitness = new String[70];
        String[]  generaciones = new String[70];
        tiempos[0] = "Tiempo";
        fitness[0] = "Costo";
        generaciones[0] = "Generacion";
        int medida = 1;
        
        long iteradorTiempo = muestra; // se evalua de a 5 segundos	
        long maxTiempo = 60000L * MAX_TIEMPO;
        
        //impresión de título
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("-----------------> Periodic Vehicle Routing Problem <------------------\n");
        
        System.out.println("I. CARGANDO EL CONOCIMIENTO DE LA INSTANCIA '"+problemInstanceFile+"'\n");
        
        // lectura del conocimiento de la instancia del problema elegida
        conocimiento = new Conocimiento();
        conocimiento.CargarConocimiento(problemInstanceFile);
        // lectura de la mejor soluciòn conocida
        conocimiento.LeerSolucion(bestSolutionFile);
        
        String conocimientoString = conocimiento.toString();
        System.out.println(conocimientoString);
        
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("II. INICALIZANDO POBLACIÓN \n");
        
        poblacion = new Poblacion(conocimiento,TAM_POBLACION,PROB_MUTACION);
        poblacion.evaluar();
        
        System.out.println("  2.1. Población Inicial: \n");     
        String popInicial = poblacion.toStringImprimible();        
        System.out.println(popInicial);
        
        
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("III. GENERACIONES \n");
        

        boolean parada = false;

        
        // medida de tiempo
        long tiempoActual;
        long inicio = System.currentTimeMillis();
                
        while (!parada) {
            String generacion = "--> POBGEN ["+poblacion.getGeneracion()+"]::";
            
            // iteraciones del algoritmo genetico
            Cromosoma [] selectos = poblacion.seleccion();
            poblacion.cruce(selectos);
            
            //poblacion.setHijos(poblacion.getIndividuos());
            poblacion.mutar();
            poblacion.reemplazar();
            boolean newBestGlobal = poblacion.evaluar();
            
            System.out.println(" Inválidos en la Población -> "+poblacion.hayInvalidos());
            System.out.println(generacion+"BEST Local  -> "+poblacion.toStringMejorActual());
            System.out.println(generacion+"BEST Global -> "+poblacion.toStringMejorHistorico());
            if (newBestGlobal) {
                System.out.println(generacion+"NUEVO BEST Global!!!!");
            }

            //System.out.println(generacion+"POBLACIÓN GENERACIÓN: \n");
            //String ultimo = poblacion.toStringImprimible();
            //System.out.println(generacion+ultimo);
            System.out.println(generacion+"END ----------------------------------------------------------->");
            
            tiempoActual = System.currentTimeMillis();

            if (tiempoActual - inicio >= maxTiempo)
                    parada = true;
            else if (tiempoActual - inicio >= iteradorTiempo){
                    //System.out.println("Generacion: "+iteraciones);
                    //imprimirMejor(poblacion);
                    tiempos[medida] = ""+(tiempoActual - inicio);
                    fitness[medida] = ""+poblacion.getMejorCostoHistorico();
                    generaciones[medida] = ""+poblacion.getMejorGeneracion();
                    medida++;
                    iteradorTiempo += muestra; 		
            }            
        }
        
        
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("IV. FIN DEL ALGORITMO \n");
        System.out.println("Mejor Solución Global -> "+poblacion.toStringMejorHistorico());
        System.out.println("Generación de la Mejor Solución Global -> " + poblacion.getMejorGeneracion());
        System.out.println(" Es la mejor solución válida? "+poblacion.getMejorHistorico().isValido(conocimiento));
        System.out.println(" Costo de la Mejor solución Conocida: "+conocimiento.mejorSol);
        double relacionMejorSol = conocimiento.mejorSol/poblacion.getMejorCostoHistorico();
        System.out.println(" Mejor solución conocida / Mejor solución encontrada: "+relacionMejorSol);
        
        System.out.println("-------------------------------------------------------------------------");
        
        System.out.println("Escribiendo resultados a archivo...");
        
        CSVWriter writer = null;
        try {
                String path = SOL_DIR+instanceNumber+"_SOL"+testNumber+"_"+
                        TAM_POBLACION+"IND_"+MAX_TIEMPO+"MIN_"+PROB_MUTACION+"MUT.csv";
                writer = new CSVWriter(new FileWriter(path));
        } catch (IOException e){
                System.out.println("Error de escritura del archivo historico");
                e.printStackTrace();
                System.exit(0);			
        }
        
        writer.writeNext(tiempos);
        writer.writeNext(fitness);
        writer.writeNext(generaciones);

        try {
            writer.close();	
        } catch (IOException e){
            System.out.println("Error para cerrar archivo historico");
            e.printStackTrace();
            System.exit(0);			
        }		
		
        
        /*
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("ÚLTIMA GENERACIÓN: \n");
        String ultimo = poblacion.toStringImprimible();
        System.out.println(ultimo);
        System.out.println("-------------------------------------------------------------------------");
        */
        
    }

}
