/*
 * OACO.java
 *
 * Created on 11 de diciembre de 2007, 17:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package omicronaco;

import au.com.bytecode.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author Huguis
 *En esta clase contaremos con todos los metodos y datos necesarios para realizar 
 *una solucion utilizando el OMICRON ACO, es decir, matriz de feromonas,
 *de costos, alfa, beta, etc.
 */
public class OACO {
    // Su tamaño sera inicializado a la cantidad de clientes mas 1 para tener
    // en cuenta tambien al deposito
    private double matrizFeromonas[][];   //matriz de feromonas (memoristica)
    
    //private static String SOL_DIR = "D:\\cparra\\Cparra\\Estudios\\FACU\\2007\\8voSemestre\\ia\\tp-ia\\fuentes\\GA-PVRP-JAVA\\GA-PVRP-JAVA\\sols-aco\\runs";
    private String SOL_DIR = "..\\sols-aco\\runs";
    private static int MAX_TIEMPO = 5; // EN MINUTOS
    
    private int tamanoPoblacion;
    private int megatron;
    private Hormiga hActual;
    private int generaciones = 100;
    
    /** Creates a new instance of OACO 
     *Inicializamos todos los datos necesarios.
     */
    public OACO(Conocimiento entrada) {
        
        this.matrizFeromonas = new double [entrada.cantClientes+1][entrada.cantClientes+1];
        this.inicializarMatFeromonas();
        this.setTamanoPoblacion(entrada.cantidadHormigas);
        this.setMegatron(entrada.megatron);
        this.hActual = new Hormiga(entrada);
    }
    /*Inicializa Matriz de feromonas con los valores a 1*/
    public void inicializarMatFeromonas(){
        for (int i = 0; i < matrizFeromonas.length; i++) {
            for (int j = 0; j < matrizFeromonas[i].length; j++) {
                if (i == j) {
                    this.matrizFeromonas[i][j] = 0;
                }else{
                    this.matrizFeromonas[i][j] = 1;
                }
            }
        }
        
    }

    public double[][] getMatrizFeromonas() {
        return matrizFeromonas;
    }

    public void setMatrizFeromonas(double[][] matrizFeromonas) {
        this.matrizFeromonas = matrizFeromonas;
        
    }
    
    /*Actualizamos la matriz de feromonas
     *de acuerdo a las rutas que fueron visitadas por las Hormigas
     */
    public void actualizarMatrizFeromonas(Vector<Hormiga> sols){
        this.inicializarMatFeromonas();
        int x = 0;
        
        //ACA DEBEMOS ACTUALIZAR LAS FEROMONAS POR CADA ARCO QUE 
        // SE HAYA TOCADO DENTRO DE LAS SOLUCIONES...
        Iterator<Hormiga> itHormiga = sols.iterator();
        while(itHormiga.hasNext()){
            this.sethActual((Hormiga)itHormiga.next());
            
            for (int i = 0; i < this.gethActual().getCaminos().length; i++) {
                
                for (int j = 0; j < this.gethActual().getCaminos()[i].length; j++) {
                    
                    if(!this.gethActual().getCaminos()[i][j].getRuta().isEmpty()){
                        
                        Iterator<Integer> itRuta = this.gethActual().getCaminos()[i][j].getRuta().iterator();
                        int actual = (int) itRuta.next();
                        int posterior = 0;
                        while(itRuta.hasNext()){
                            
                            posterior = (int) itRuta.next();
                            this.matrizFeromonas[actual][posterior] += this.getMegatron()/this.tamanoPoblacion;
                            actual = posterior;
                            
                        }
                    }
                }
            }
        }
    }

    public int getTamanoPoblacion() {
        return tamanoPoblacion;
    }

    public void setTamanoPoblacion(int tamanoPoblacion) {
        this.tamanoPoblacion = tamanoPoblacion;
    }

    public int getMegatron() {
        return megatron;
    }

    public void setMegatron(int megatron) {
        this.megatron = megatron;
    }

    public Hormiga gethActual() {
        return hActual;
    }

    public void sethActual(Hormiga hActual) {
        this.hActual = hActual;
    }
    
    /**
     *Se le añadio este nombre jocoso solo para amenizar el trabajo debido a las largas madrugadas
     *que llevamos desarrollando, este es el algoritmo principal que esta descripto en el paper
     */
    public void elPurete(Conocimiento entrada, int corridas){
        System.out.println(" CANTIDAD DE CORRIDAS" + corridas);
        System.out.println(" CANTIDAD DE GENERACIONES" + this.generaciones);
        this.inicializarMatFeromonas();
        Poblacion P= new Poblacion(entrada,this.matrizFeromonas);
        P.inicializarPoblacion();
        /*ya se hace en inicializar poblacion*/
        //P.sortSoluciones();
        
        int x =0;
        int y = 0;
        while ( x < corridas){
            while(y < this.generaciones){
                
                P.construirHormiga();
                
                if(P.getNuevaHormiga().getCostoTotal() < ((Hormiga)P.getSoluciones().get(0)).getCostoTotal()){
                    if(!P.estaContenido(P.getNuevaHormiga())){
                        P.actualizarSoluciones();
                        y++;
                    }
                }
                
                
            }
            this.actualizarMatrizFeromonas(P.getSoluciones());
            x++;
        }
        System.out.println(P.toString());
    }
    
    /**
     *IDEM anterior pero utilizando condicion de tiempo para parada.
     */
    public void elPuretePorTiempo(Conocimiento entrada, int tiempoMax, int prueba,String instanceNumber){
        
        
        
        /*
        * Variables para calculo de Resultados.
         *  Correr por: 5 min (300 seg)
         *  Tomar muestra cada: 5 seg.
         */
        long muestra = 5000; 

        long iteradorTiempo = muestra; // se evalua de a 5 segundos	
        long maxTiempo = 60000L * tiempoMax;

        int longSalida = (int)( maxTiempo / muestra) + 2;

        String[] tiempos = new String[longSalida]; 
        String[] fitness = new String[longSalida];
        String[] iteraciones = new String[longSalida];    // --- revisar 
        tiempos[0] = "Tiempo";
        fitness[0] = "Costo";
        iteraciones[0] = "Generacion";
        int medida = 1;

        String headPrueba = "####PRUEBA "+prueba+":: ";
        
        System.out.println(" CANTIDAD DE HORMIGAS" + this.generaciones);
        
        this.inicializarMatFeromonas();
        Poblacion P= new Poblacion(entrada,this.matrizFeromonas);
        
        // construye las primeras soluciones. 
        // la cantidad de hormigas está determinado por la propiedad this.generaciones
        // El algoritmo presentado es una adaptación de la propuesta de
        // Barán y Gómez en su paper "Omicron ACO"
                
        System.out.println(headPrueba+":: INICALIZANDO POBLACIÓN... \n");
        System.out.println(headPrueba+":: --> Población inicial:  \n");
        
        P.inicializarPoblacion();
        /*ya se hace en inicializar poblacion*/
        //P.sortSoluciones();

        System.out.println(headPrueba+":: -->   "+P.toString());

        // Mantenemos la nomeclatura del Algoritmo genético, para establecer un 
        // paralelo entre ambos algoritmos. 
        // En este caso, cada generación corresponde a una iteración del algoritmo
        // en la que producimos K hormigas. 
        
        System.out.println("\n-------------------------------------------------------------------------");
        System.out.println(headPrueba+"GENERACIONES \n");

        boolean parada = false;

        // medida de tiempo
        long tiempoActual;
        long inicio = System.currentTimeMillis();
        
        int y = 0;
        int x = 1;
        boolean newBest = false;  // Variable utilizada para detectar cada vez
                                  // que encontramos una nueva mejor solucion global
        int mejorGeneracion = x;  // Indicador de la última generación en la que 
                                  // encontramos una mejor solución
        while (!parada){            
            
                
            String generacion = headPrueba+"--> POBGEN ["+x+"]::";
            
            
            // construcción de hormigas...
            // 1. Se construyen this.generacion hormigas, cada uno con una solucion en particular
            // 2. Luego de cada nueva hormiga construida, se reemplaza la peor hormiga por la nueva
            //    si esta es mejor. 
            // 3. Luego, se reordenan las soluciones. 
            y = 0; 
            
            // con esta variable controlamos el caso en el que no se hayan 
            // generado this.generaciones nuevas mejores soluciones al momento
            // de exceder el tiempo de la prueba. 
            boolean tiempoExcedido = false;
            
            while(y < this.generaciones && !tiempoExcedido ) {
                
                // generar nueva hormiga
                P.construirHormiga();
                
                Hormiga mejorActual = (Hormiga) P.getSoluciones().lastElement();
                Hormiga nueva = P.getNuevaHormiga();
                
                if (mejorActual.getCostoTotal() > nueva.getCostoTotal()) {
                    newBest = true;
                }
                
                if(P.getNuevaHormiga().getCostoTotal() < ((Hormiga)P.getSoluciones().get(0)).getCostoTotal()){
                    if(!P.estaContenido(P.getNuevaHormiga())){
                        P.actualizarSoluciones();
                        y++;
                    }
                }
                
            
                Vector<Hormiga> currentSolutionsLocal = P.getSoluciones();    
                Hormiga mejorLocal = currentSolutionsLocal.lastElement();
                System.out.println(generacion+"BEST Actual  -> COSTO: "+mejorLocal.getCostoTotal());
                System.out.print(generacion+"BEST Actual  -> "+mejorLocal.toString());

                if (newBest) {
                    System.out.println(generacion+"NUEVO BEST Global!!!!");
                    mejorGeneracion = x;
                    newBest = false; 
                }

                
                // medición de tiempo para cortar este ciclo interno cuando se 
                // excede el tiempo de prueba.
                long tiempoCorte = System.currentTimeMillis();
                
                if (tiempoCorte - inicio >= maxTiempo) {
                    tiempoExcedido = true;
                } else if (tiempoCorte - inicio >= iteradorTiempo){
                    //System.out.println("Generacion: "+iteraciones);
                    //imprimirMejor(poblacion);
                    tiempos[medida] = ""+(tiempoCorte - inicio);
                    fitness[medida] = ""+mejorLocal.getCostoTotal();
                    iteraciones[medida] = ""+x;                     

                    medida++;
                    iteradorTiempo += muestra; 		
                }
                
            }
            
            Vector<Hormiga> currentSolutions = P.getSoluciones();
            this.actualizarMatrizFeromonas(currentSolutions);
            
            Hormiga mejor = currentSolutions.lastElement();
            System.out.println(generacion+"BEST Actual  -> COSTO: "+mejor.getCostoTotal());
            System.out.print(generacion+"BEST Actual  -> "+mejor.toString());
            
            if (newBest) {
                System.out.println(generacion+"NUEVO BEST Global!!!!");
                mejorGeneracion = x;
                newBest = false; 
            }

            System.out.println(generacion+"END ----------------------------------------------------------->");

            tiempoActual = System.currentTimeMillis();

            if (tiempoActual - inicio >= maxTiempo)
                    parada = true;
            else if (tiempoActual - inicio >= iteradorTiempo){
                    //System.out.println("Generacion: "+iteraciones);
                    //imprimirMejor(poblacion);
                    tiempos[medida] = ""+(tiempoActual - inicio);
                    fitness[medida] = ""+mejor.getCostoTotal();
                    iteraciones[medida] = ""+x;                     

                    medida++;
                    iteradorTiempo += muestra; 		
            }         

            x++;                        
        }
        
        
        Hormiga mejorFinal = (Hormiga) P.getSoluciones().lastElement();
        
        System.out.println(headPrueba+"-------------------------------------------------------------------------");
        System.out.println(headPrueba+"FIN DEL ALGORITMO \n");
        System.out.println(headPrueba+"FIN -> Mejor Solución Global COSTO: -> "+mejorFinal.getCostoTotal());
        System.out.println(headPrueba+"FIN -> Mejor Solución Global -> "+mejorFinal.toString());
        System.out.println(headPrueba+"FIN -> Generación de la Mejor Solución Global -> " + mejorGeneracion);
        System.out.println(headPrueba+"FIN -> Costo de la Mejor solución Conocida: "+entrada.mejorSol);
        double relacionMejorSol = entrada.mejorSol/mejorFinal.getCostoTotal();
        System.out.println(headPrueba+"FIN -> Mejor solución conocida / Mejor solución encontrada: "+relacionMejorSol);

        System.out.println("-------------------------------------------------------------------------");

        System.out.println("Escribiendo resultados a archivo...");

        CSVWriter writer = null;
        
        
        
        try {
                String barra = "\\";
                
                String separator = File.separator;
                if (separator.compareTo("/") == 0 ) {
                    barra = "/";
                    this.SOL_DIR = "../sols-aco/runs";
                }
            
                String path = SOL_DIR+"-"+instanceNumber+barra+instanceNumber+"_TEST"+prueba+"_"+
                        +MAX_TIEMPO+"MIN_"+this.generaciones+"HORMIGAS.csv";
                writer = new CSVWriter(new FileWriter(path));
        } catch (IOException e){
                System.out.println("Error de escritura del archivo historico");
                e.printStackTrace();
                System.exit(0);			
        }

        writer.writeNext(tiempos);
        writer.writeNext(fitness);
        writer.writeNext(iteraciones);

        try {
            writer.flush();
            writer.close();	
        } catch (IOException e){
            System.out.println("Error para cerrar archivo historico");
            e.printStackTrace();
            System.exit(0);			
        }		

        System.out.println(headPrueba+"ÚLTIMA POBLACIÓN: ");
        System.out.println(headPrueba+P.toString());
        
    }
    
}
