/*
 * FuncionesGA.java
 *
 * Created on 1 de noviembre de 2007, 12:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gapvrpjava;
import java.util.*;
/**
 * Librería de funciones utilizadas para realizar las operaciones genéticas
 * sobre los Cromosomas solución del problema PVRP
 * 
 * @author Cristhian Parra
 * @author Hugo Meyer
 */
public class FuncionesGACparra {

    private static void ajustarLocalmente(ruta RutaH1, ruta RutaH2, 
                                            int[] visitasGlobales1, 
                                            int[] visitasGlobales2, 
                                            int[] visitasLocales,
                                            int h1Size, int h2Size) {

        // calculamos espacios no ocupados en cada hijo
        int espacioLibre1 = h1Size - RutaH1.ruta.size();
        int espacioLibre2 = h2Size - RutaH2.ruta.size();
        
        for (int cliente = 1; cliente < visitasLocales.length; cliente++) {
            int cuentaVisitas = visitasLocales[cliente];
                        
            for (int i = 0; i < cuentaVisitas; i++) {
                // calculamos la cuenta de visitas gobales para cliente en cada
                // cromosoma
                int cuentaGlobal1 = visitasGlobales1[cliente];
                int cuentaGlobal2 = visitasGlobales2[cliente];

                // verificamos si alguna de las rutas contiene o no al cliente
                boolean contenidoEnH1 = RutaH1.ruta.contains(cliente);
                boolean contenidoEnH2 = RutaH2.ruta.contains(cliente);

                if (espacioLibre1 > 0 ) { // siel hijo 1 todavia tiene espacio libre
                    
                    if (contenidoEnH1) { // si ya esta en el hijo 1, hay que intentar meter en el otro
                        
                        if (cuentaGlobal2 > 0) { // si todavia se puede meter en 2, metemos,
                                                 // sino se deshecha totalmente
                                                 // hay un caso en el que esto va a pasar
                            RutaH2.ruta.add(cliente);
                            visitasGlobales2[cliente]--;
                            visitasLocales[cliente]--;
                            if (espacioLibre2 > 0) {
                                espacioLibre2--;
                            }
                            
                        }
                    } else {
                        
                        if (cuentaGlobal1 > 0) { // si todavia se puede meter en 2, metemos,
                                                 // sino se deshecha totalmente
                                                 // hay un caso en el que esto va a pasar
                            RutaH1.ruta.add(cliente);
                            visitasGlobales1[cliente]--;
                            visitasLocales[cliente]--;
                            espacioLibre1--;
                            
                        }
                    } 
                    
                } else if (espacioLibre2 > 0 ) {
                    
                    if (contenidoEnH2) { // si ya esta en el hijo 1, hay que intentar meter en el otro
                        
                        if (cuentaGlobal1 > 0) { // si todavia se puede meter en 2, metemos,
                                                 // sino se deshecha totalmente
                                                 // hay un caso en el que esto va a pasar
                            RutaH1.ruta.add(cliente);
                            visitasGlobales1[cliente]--;
                            visitasLocales[cliente]--;
                            if (espacioLibre1 > 0) {
                                espacioLibre1--;
                            }
                            
                        }
                    } else {
                        
                        if (cuentaGlobal2 > 0) { // si todavia se puede meter en 2, metemos,
                                                 // sino se deshecha totalmente
                                                 // hay un caso en el que esto va a pasar
                                                 // estos se penalizan a nivel de cromosoma
                            RutaH2.ruta.add(cliente);
                            visitasGlobales2[cliente]--;
                            visitasLocales[cliente]--;
                            espacioLibre2--;               
                        }
                    }
                }               
            }
        }
    }

    /**
     * Debe copiar la primera mitad de Ruta a RutaH y actualizar adecuadamente 
     * visitasLocales y visitasGlobales
     * @param Ruta
     * @param RutaH
     * @param visitasLocales
     * @param cruzado
     * @param requeridos
     */
    private static void copiarPrimeraMitad(ruta Ruta, ruta RutaH, int[] visitasLocales, int[] visitasGlobales,int corte) {

        
        // sublista de 0 a corte1--> o sea los corte1 primeros elementos
        List<Integer> v1 = Ruta.ruta.subList(0, corte);
        Iterator<Integer> itSublist = v1.iterator();

        int corteControl = 0;
        while (itSublist.hasNext() && corteControl < corte) {
            int cliente = (int) itSublist.next();
            
            if (visitasGlobales[cliente] > 0) {
                RutaH.ruta.add(cliente);
                corteControl++;

                // decrementamos de las visitas globales y locales
                visitasGlobales[cliente]--;
                visitasLocales[cliente]--;
            }
        }
    }

    private static void copiarSegundaMitad(ruta Ruta, ruta RutaH, int[] visitasLocales, int[] visitasGlobales, int requeridos) {
        
        Iterator<Integer> it = Ruta.ruta.iterator();

        // reiniciamos insertados para recorrer adecuadamente la siguiente ruta
        int insertados = 0;

        while(it.hasNext() && insertados < requeridos){
            int cliente = (int)it.next();
            if (!RutaH.ruta.contains(cliente) && insertados < requeridos) {
                 if(visitasGlobales[cliente] > 0 
                        && visitasLocales[cliente] > 0){
                    RutaH.ruta.add(cliente);
                    insertados++;
                    
                    visitasGlobales[cliente]--;
                    visitasLocales[cliente]--;
                 }
            }
        } 

    }

    private static void inicializarVisitasLocales(ruta Ruta1, ruta Ruta2,int[] visitasLocales) {
        
        Iterator<Integer> it1 = Ruta1.ruta.iterator();
        Iterator<Integer> it2 = Ruta2.ruta.iterator();
        
        while (it1.hasNext()) {
            int cliente = (Integer) it1.next();
            visitasLocales[cliente]++;
        }
        
        while (it2.hasNext()) {
            int cliente = (Integer) it2.next();
            visitasLocales[cliente]++;
        }
    }
    /** Creates a new instance of FuncionesGA */
        
    public FuncionesGACparra() {
    }
    
    
    public static Cromosoma[] seleccionar(Poblacion p) {
        
        if (p==null || p.getTamanho()<=0)
            return null;
        
        int cantMejores = p.getTamanho();//tamanho de poblaci?n seleccionada
        
        Cromosoma Mejores[] = new Cromosoma[cantMejores];
        Random rand = new Random();
        rand.nextInt();
        
        int [] listaParticipacion = new int[p.getTamanho()];
        
               
        
        for (int i=0; i<cantMejores; i++) {

                int ind1 = rand.nextInt(p.getTamanho()); // se elige un individuo
                /*
                // mientras salgan indices ya elegidos
                while (listaParticipacion[ind1] == 1) {
                    ind1 = rand.nextInt(p.getTamanho()); // se elige un individuo
                }
                listaParticipacion[ind1] = 1;
                */
                
                int ind2 = rand.nextInt(p.getTamanho()); // se elige un individuo
                
                // mientras salgan indices ya elegidos
                while (/*listaParticipacion[ind2] == 1 &&*/ ind2 == ind1) {
                    ind2 = rand.nextInt(p.getTamanho()); // se elige un individuo
                }
                
                //listaParticipacion[ind2] = 1;
                

                // Se extrae los fitness de los correspondientes individuos 
                double costo1 = p.getFitness(ind1);
                double costo2 = p.getFitness(ind2);

                // Competencia
                if (costo1>=costo2) { // Gano individuo 1
                        Mejores[i]=p.getIndividuo(ind1);
                }
                else { // Gano individuo 2
                        Mejores[i]=p.getIndividuo(ind2);
                }
        }

        return Mejores;
    }

    public static Cromosoma[] Cruzar(Cromosoma c1, Cromosoma c2, Conocimiento entrada){
        
        // nuevos cromosomas producidos a partir del cruce
        Cromosoma cruzado1 = new Cromosoma(entrada);
        Cromosoma cruzado2 = new Cromosoma(entrada);

        
        Cromosoma[] cruceResult = new Cromosoma[2];
        
        // variables que indican donde realizar el requeridos para el cruce
        int corte1;
        int corte2;
        
        int valor = 0;
        
        int contador =0;
        

        // Realizar el cruce de c1 y c2, y producir el resultado en 
        // cruzado1 y cruzado2
        for (int j = 0; j < c1.cantVehiculos; j++) {
            for (int i = 0; i < c1.dias; i++) {
                
                contador = 0;
                // Rutas a cruzar en esta iteración. 
                ruta Ruta1 = c1.getRuta(i,j);
                ruta Ruta2 = c2.getRuta(i,j);   
                
                // rutas resultado en los cromosomas hijos
                ruta RutaH1 = cruzado1.getRuta(i,j);
                ruta RutaH2 = cruzado2.getRuta(i,j);
                
                int c1Size = c1.getRutaSize(i,j); // cantidad de elementos en c1
                int c2Size = c2.getRutaSize(i,j); // cantidad de elementos en c2
        
                // control de cantidad de visitas locales --> 
                // va de 1 a cantClientes
                int [] visitasLocales = new int[entrada.cantClientes+1];
                
                // inicializamos visitasLocales para que incluya adecuadamente la informacion
                // de las rutas a cruzar
                inicializarVisitasLocales(Ruta1,Ruta2,visitasLocales);
                
               
                // Si ninguna ruta esta vacía. 
                if (c1Size > 0 && c2Size > 0) {
                
                    // puntos de requeridos
                    double aux = (double) c1Size/2;
                    corte1 = (int) Math.ceil(aux);
                    
                    aux = (double)c2Size/2;
                    corte2 = (int) Math.ceil(aux);
                    
                    // tamaños requeridos para nuevos hijos
                    int h1Size = corte1+(c2Size-corte2);
                    int h2Size = corte2+(c1Size-corte1);
                    
                    // copiar primeras mitades
                    copiarPrimeraMitad(Ruta1,RutaH1,visitasLocales,
                                        cruzado1.listaVisitasCromo[1],corte1);
                    copiarPrimeraMitad(Ruta2,RutaH2,visitasLocales,
                                        cruzado2.listaVisitasCromo[1],corte2);   
                    
                    int cantACopiar = c2Size - corte2; 
                    // completar segundas mitades
                    copiarSegundaMitad(Ruta2,RutaH1,visitasLocales,
                                        cruzado1.listaVisitasCromo[1],
                                        cantACopiar);  
                    
                    cantACopiar = c1Size - corte1; 
                    copiarSegundaMitad(Ruta1,RutaH2,visitasLocales,
                                        cruzado2.listaVisitasCromo[1],
                                        cantACopiar);
  
                    
                    // ahora debemos ajustar nuestras rutas generadas, segun 
                    // lo que faltó visitar localmente. 
                    ajustarLocalmente(RutaH1,RutaH2,
                                        cruzado1.listaVisitasCromo[1],
                                        cruzado2.listaVisitasCromo[1],
                                        visitasLocales,
                                        h1Size,h2Size);
                    
                        
                // si uno está vacío y el otro no, partir el que no está vacío 
                // en dos. Si ambos están vacío, yapiro forzado
                } else if (c1Size == 0 || c2Size == 0) {
                    
                    if (c1Size > 0) {
                        corte1 = (int) Math.floor(c1Size/2)+1;

                        List<Integer> v1 = c1.cromosoma[i][j].ruta.subList(0, corte1);
                        Iterator<Integer> itSublist = v1.iterator();
                        contador = 0; 
                        
                        while (itSublist.hasNext() ) {
                            valor = (int) itSublist.next();

                            if (cruzado1.listaVisitasCromo[1][valor] > 0) {
                                cruzado1.cromosoma[i][j].ruta.add(valor);

                                // decrementamos de las visitas globales
                                cruzado1.listaVisitasCromo[1][valor]--;
                            }
                        }

                        List<Integer> v2 = c1.cromosoma[i][j].ruta.subList(corte1, c1Size);

                        itSublist = v2.iterator();

                        while (itSublist.hasNext() ) {
                            valor = (int) itSublist.next();
                            if (cruzado2.listaVisitasCromo[1][valor] > 0) {
                                cruzado2.cromosoma[i][j].ruta.add(valor);


                                // decrementamos visitas globales
                                cruzado2.listaVisitasCromo[1][valor]--;
                            }

                        }
                    } else if (c2Size > 0) {
                        
                        corte2 = (int)Math.floor(c2Size/2)+1;

                        List<Integer> v1 = c2.cromosoma[i][j].ruta.subList(0, corte2);
                        Iterator<Integer> itSublist = v1.iterator();
                        
                        while (itSublist.hasNext()) {
                            valor = (int) itSublist.next();

                            if (cruzado1.listaVisitasCromo[1][valor] > 0) {
                                cruzado1.cromosoma[i][j].ruta.add(valor);

                                // decrementamos de las visitas globales
                                cruzado1.listaVisitasCromo[1][valor]--;
                            }
                        }

                        List<Integer> v2 = c2.cromosoma[i][j].ruta.subList(corte2, c2Size);
                        
                        itSublist = v2.iterator();

                        while (itSublist.hasNext()) {
                            valor = (int) itSublist.next();
                            if (cruzado2.listaVisitasCromo[1][valor] > 0) {
                                cruzado2.cromosoma[i][j].ruta.add(valor);

                                // decrementamos visitas globales
                                cruzado2.listaVisitasCromo[1][valor]--;
                            }
                        }
                    }                    
                }
                /*ACTUALIZAMOS LA CANTIDAD DE VECINOS*/
                /*
                Iterator<Integer> it1 = cruzado1.cromosoma[i][j].ruta.iterator();
                while (it1.hasNext()) {
                    cliente = (int) it1.next();
                    cruzado1.listaVisitasCromo[1][cliente]--;
                }
                Iterator<Integer> it = cruzado2.cromosoma[i][j].ruta.iterator();
                while (it.hasNext()) {
                    cliente = (int) it.next();
                    cruzado2.listaVisitasCromo[1][cliente]--;
                }
                 */
            }
        }
        evaluarListaVisitasCromo(cruzado1, entrada);
        evaluarListaVisitasCromo(cruzado2,entrada);
        cruceResult[0] = cruzado1;
        cruceResult[1] = cruzado2;
        
        return cruceResult;
    }
 
    public static void evaluarListaVisitasCromo(Cromosoma c1,Conocimiento entrada){
        c1.copiar(entrada.listaVisitas);
        for (int i = 0; i < entrada.dias; i++) {
            for (int j = 1; j < entrada.cantVehiculos; j++) {
                Iterator<Integer> it = c1.cromosoma[i][j].ruta.iterator();
                while(it.hasNext()){
                    c1.listaVisitasCromo[1][it.next()]--;
                }
            }
        }
    }
    
    
}
