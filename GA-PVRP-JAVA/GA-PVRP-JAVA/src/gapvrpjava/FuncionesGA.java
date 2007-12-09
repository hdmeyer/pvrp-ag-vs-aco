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
public class FuncionesGA {

    private static boolean meterSiEspacio(ruta RutaH, int[] visitasGlobales, int[] visitasLocales, int espacioLibre,int cliente) {
        if (espacioLibre > 0) {
            if (!RutaH.ruta.contains(cliente) ) {
                if(visitasGlobales[cliente]>0) {
                    RutaH.ruta.add(cliente);
                    visitasGlobales[cliente]--;
                    visitasLocales[cliente]--;
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }            
        } else {
            return false;
        }
    }

    private static boolean meterSiNoContenido(ruta RutaH, int[] visitasGlobales, int[] visitasLocales,int cliente) {
        
        if(!RutaH.ruta.contains(cliente)) {
            if(visitasGlobales[cliente]>0) {
                RutaH.ruta.add(cliente);
                visitasGlobales[cliente]--;
                visitasLocales[cliente]--;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
        
    }

    private static void splitRuta(ruta Ruta, ruta RutaH1, ruta RutaH2, int[] visitasGlobales1,int[] visitasGlobales2) {
        
        Iterator<Integer> itSublist = Ruta.ruta.iterator();

        ruta ref1 = RutaH1;
        ruta ref2 = RutaH2;
        
        int[] refVisitas1 = visitasGlobales1;
        int[] refVisitas2 = visitasGlobales2;
        
        while (itSublist.hasNext() ) {
            int cliente = (int) itSublist.next();

            if (refVisitas1[cliente] > 0 && !ref1.ruta.contains(cliente)) {
                ref1.ruta.add(cliente);

                // decrementamos de las visitas globales
                refVisitas1[cliente]--;
            } else {
                ref2.ruta.add(cliente);

                // decrementamos de las visitas globales
                refVisitas2[cliente]--;
            }
            
            // intercambiar referencias para siguiente intento.
            ruta aux = ref1;
            int[] visitasaux = refVisitas1;
            
            ref1 = ref2;
            refVisitas1 = refVisitas2;          
            
            ref2 = aux;
            refVisitas2 = visitasaux;            
        }
    }

    
    /** Creates a new instance of FuncionesGA */
        
    public FuncionesGA() {
    }
    /*PODRIAMOS PONER A CERO CUANDO INICIALIZAMOS EL CROMOSOMA*/    
    
    
    public static Cromosoma[] CruzarOld(Cromosoma c1, Cromosoma c2, Conocimiento entrada){
        Cromosoma cruzado1 = new Cromosoma(entrada);
        Cromosoma cruzado2 = new Cromosoma(entrada);
        Cromosoma[] cruceResult = new Cromosoma[2];
        int corte1;
        int corte2;
        int valor = 0;
        int contador =0;
        int [] visitasLocales = new int[entrada.cantClientes+1];

        // Realizar el cruce de c1 y c2, y producir el resultado en 
        // cruzado1 y cruzado2
        for (int j = 0; j < c1.cantVehiculos; j++) {
            for (int i = 0; i < c1.dias; i++) {
                contador = 0;
                int c1Size = c1.cromosoma[i][j].ruta.size(); // cantidad de elementos en c1
                int c2Size = c2.cromosoma[i][j].ruta.size(); // cantidad de elementos en c2
                
                // inicializamos visitasLocales a todo cero
                for (int k = 0; k < visitasLocales.length; k++) {
                     visitasLocales[k]= 0;

                }
                
                
                if (c1Size > 0 && c2Size > 0) {
                
                    corte1 = (int) Math.ceil(c1Size/2);
                    corte2 = (int) Math.ceil(c2Size/2);
                    
                    // tamaaños requeridos para nuevos hijos
                    int h1Size = corte1+c1Size-corte2;
                    int h2Size = corte2+c2Size-corte1;
                    
                    // sublista de 0 a corte1--> o sea los corte1 primeros elementos
                    List<Integer> v1 = c1.cromosoma[i][j].ruta.subList(0, corte1);
                    Iterator<Integer> itSublist = v1.iterator();
                    
                    while (itSublist.hasNext() && contador < corte1) {
                        valor = (int) itSublist.next();
                        visitasLocales[valor]++;
                        if (cruzado1.listaVisitasCromo[1][valor] > 0) {
                            cruzado1.cromosoma[i][j].ruta.add(valor);
                            contador++;

                            // decrementamos de las visitas globales
                            cruzado1.listaVisitasCromo[1][valor]--;
                            visitasLocales[valor]--;
                        }
                    }
                    
                    contador =0;
                    
                    List<Integer> v2 = c2.cromosoma[i][j].ruta.subList(0, corte2);
                    
                    itSublist = v2.iterator();
                    
                    while (itSublist.hasNext() && contador < corte2) {
                        valor = (int) itSublist.next();
                        visitasLocales[valor]++;
                         if (cruzado2.listaVisitasCromo[1][valor] > 0) {
                            cruzado2.cromosoma[i][j].ruta.add(valor);
                            contador++;

                            // decrementamos visitas globales
                            cruzado2.listaVisitasCromo[1][valor]--;
                            visitasLocales[valor]--;
                         }
                                
                    }
                    
                    contador = 0;
                    
                    List<Integer> v3 = c1.cromosoma[i][j].ruta.subList(corte1,c1.cromosoma[i][j].ruta.size());
                    List<Integer> v4 = c2.cromosoma[i][j].ruta.subList(corte2,c2.cromosoma[i][j].ruta.size());
                                   
                    Iterator<Integer> it3 = v3.iterator();
                    
                    while(it3.hasNext()){
                        visitasLocales[(int)it3.next()]++;
                    }
                    
                    Iterator<Integer> it4 = v4.iterator();
                    
                    while(it4.hasNext()){
                        visitasLocales[(int)it4.next()]++;
                    }
                    
                    Iterator<Integer> it1 = c1.cromosoma[i][j].ruta.iterator();
                    Iterator<Integer> it2 = c2.cromosoma[i][j].ruta.iterator();
                    
                    /*AHORA CONTROLAMOS TB LA LISTAVISITASCROMO*/
                    
                    while(it1.hasNext() && contador <= corte1){
                        valor = (int)it1.next();
                        if (!cruzado2.cromosoma[i][j].ruta.contains(valor)&& contador <= corte1) {
                            if(cruzado2.listaVisitasCromo[1][valor] > 0 
                                    && visitasLocales[valor] > 0){
                                cruzado2.cromosoma[i][j].ruta.add(valor);
                                contador++;
                                cruzado2.listaVisitasCromo[1][valor]--;
                                visitasLocales[valor]--;
                            }                      
                        }           
                    }
                    
                    // reiniciamos insertados para recorrer adecuadamente la siguiente ruta
                    contador = 0;
                    
                    while(it2.hasNext() && contador <= corte2){
                        valor = (int)it2.next();
                        if (!cruzado1.cromosoma[i][j].ruta.contains(valor) && contador <= corte2) {
                             if(cruzado1.listaVisitasCromo[1][valor] > 0 
                                    && visitasLocales[valor] > 0){
                                cruzado1.cromosoma[i][j].ruta.add(valor);
                                contador++;
                                cruzado1.listaVisitasCromo[1][valor]--;
                                visitasLocales[valor]--;
                             }
                        }
                    } 
                    
                    for (int k = 1; k < visitasLocales.length; k++) {
                        int l = visitasLocales[k];
                        
                        if (l==2) {  
                            
                            if (cruzado1.listaVisitasCromo[1][k] > 0) {
                                cruzado1.cromosoma[i][j].ruta.add(k);
                                visitasLocales[k]--;
                                cruzado1.listaVisitasCromo[1][k]--;
                            }
                            if (cruzado2.listaVisitasCromo[1][k] > 0) {                            
                                cruzado2.cromosoma[i][j].ruta.add(k);
                                visitasLocales[k]--;
                                cruzado2.listaVisitasCromo[1][k]--;
                            }
                            
                        } else if (l==1) {
                            int espacio1 = h1Size - cruzado1.cromosoma[i][j].ruta.size();
                            int espacio2 = h2Size - cruzado2.cromosoma[i][j].ruta.size();
                            
                            if (espacio1 > 0 && cruzado1.listaVisitasCromo[1][k] > 0) {
                                
                                    cruzado1.cromosoma[i][j].ruta.add(k);
                                    visitasLocales[k]--;
                                    cruzado1.listaVisitasCromo[1][k]--;                                
                                
                            } else if (espacio2 > 0 && cruzado2.listaVisitasCromo[1][k] > 0){
                                
                                    cruzado2.cromosoma[i][j].ruta.add(k);
                                    visitasLocales[k]--;
                                    cruzado2.listaVisitasCromo[1][k]--;                                                                
                                
                            } else {
                                if (cruzado1.listaVisitasCromo[1][k] > 0 
                                        && !cruzado1.cromosoma[i][j].ruta.contains(k)) {
                                    cruzado1.cromosoma[i][j].ruta.add(k);
                                    visitasLocales[k]--;
                                    cruzado1.listaVisitasCromo[1][k]--;                                                                
                                } else if (cruzado2.listaVisitasCromo[1][k] > 0 
                                        && !cruzado2.cromosoma[i][j].ruta.contains(k)) {
                                    cruzado2.cromosoma[i][j].ruta.add(k);
                                    visitasLocales[k]--;
                                    cruzado2.listaVisitasCromo[1][k]--;                                                                
                                }
                            }
                        }
                    }                    
                        
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
                Iterator<Integer> it2 = cruzado2.cromosoma[i][j].ruta.iterator();
                while (it2.hasNext()) {
                    cliente = (int) it2.next();
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
    
    
    public static Cromosoma Mutar(Cromosoma c1,Conocimiento entrada) {
        
        for (int i = 0; i < entrada.dias; i++) {
            mezclarCamiones(c1.cromosoma[i]);
        }

        return c1;
    }
    public static void mezclarCamiones(ruta[] camiones){
        int mitad = (int) Math.ceil(camiones.length/2);
        for (int k = 0; k < camiones.length; k++) {
            if(k + mitad >= camiones.length){
                mezclarRutas(camiones[k],camiones[0]);
                break;
            }else{
                mezclarRutas(camiones[k],camiones[k+mitad]);
            }
        }
    }
    
    private static void mezclarRutas(ruta ruta1, ruta ruta2) {
        int cantCruces =0;
        int vecSize;
        int temp;
        int[][] intercambios;
        if(ruta1.ruta.size() > ruta2.ruta.size()){
            vecSize = ruta1.ruta.size();
        }else{
            vecSize = ruta2.ruta.size();
        }
        vecSize = (int)(Math.random()* (vecSize));
        intercambios = new int [2][vecSize];
        for (int i = 0; i < vecSize; i++) {
            intercambios[0][i] = (int)(Math.random()* (ruta1.ruta.size()));
            intercambios[1][i] = (int)(Math.random()* (ruta2.ruta.size()));
        }
        
        for (int i = 0; i < vecSize; i++) {
            if(ruta1.ruta.size() != 0 && ruta2.ruta.size() !=0){
                temp =(Integer) ruta1.ruta.get(intercambios[0][i]);
                ruta1.ruta.setElementAt(ruta2.ruta.get(intercambios[1][i]),intercambios[0][i]);
                ruta2.ruta.setElementAt(temp,intercambios[1][i]);
            }
        }

    }
    
        
    public static Cromosoma[] seleccionar(Poblacion p) {
        
        if (p==null || p.getTamanho()<=0)
            return null;
        
        int cantMejores = p.getTamanho();//tamanho de poblaci?n seleccionada
        
        Cromosoma Mejores[] = new Cromosoma[cantMejores];
        Random rand = new Random();
        rand.nextInt();

        for (int i=0; i<cantMejores; i++) {

                int ind1 = rand.nextInt(p.getTamanho()); // se elige un individuo
                int ind2 = rand.nextInt(p.getTamanho()); // se elige un individuo
                while (ind2==ind1) {
                        ind2 = rand.nextInt(p.getTamanho()); // se reelige un individuo
                }

                // Se extrae los fitness de los correspondientes individuos 
                double fitness1 = p.getFitness(ind1);
                double fitness2 = p.getFitness(ind2);

                if(fitness1<0 && fitness2<0) {
                    i--;
                    break;
                }
                // Competencia
                if (fitness1>=fitness2) { // Gan? individuo 1
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
        
        // Realizar el cruce de c1 y c2, y producir el resultado en 
        // cruzado1 y cruzado2
        for (int j = 0; j < c1.cantVehiculos; j++) {
            for (int i = 0; i < c1.dias; i++) {
                
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
                    
                    // copiar primeras mitades, de RutaX a RutaHX, 
                    // actualizar listaVisitasCromo de RutaHX
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
                    
                    // dividimos en dos 
                    if (c1Size > 0) {

                        splitRuta(Ruta1,RutaH1,RutaH2,cruzado1.listaVisitasCromo[1],cruzado2.listaVisitasCromo[1]);
                        
                    } else if (c2Size > 0) {
                        
                        splitRuta(Ruta2,RutaH1,RutaH2,cruzado1.listaVisitasCromo[1],cruzado2.listaVisitasCromo[1]);
                        
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
        /*
        evaluarListaVisitasCromo(cruzado1, entrada);
        evaluarListaVisitasCromo(cruzado2,entrada);
         */
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

                boolean insertado = meterSiEspacio(RutaH1, visitasGlobales1, 
                                                    visitasLocales,espacioLibre1,cliente);

                if(!insertado) {
                    insertado = meterSiEspacio(RutaH2, visitasGlobales2, 
                                                    visitasLocales,espacioLibre1,cliente);
                } else {
                    espacioLibre1--;
                }

                if(!insertado) {
                    insertado = meterSiNoContenido(RutaH1,visitasGlobales1,
                                                    visitasLocales,cliente);
                } else {
                    espacioLibre2--;
                }

                if(!insertado) {
                    insertado = meterSiNoContenido(RutaH2,visitasGlobales2,
                                                    visitasLocales,cliente);
                } 

            }
        }
    }

    /**
     * Debe copiar la primera mitad de Ruta a RutaH y actualizar adecuadamente 
     * visitasLocales y visitasGlobales1
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
    
//    public static boolean revisarRepetido(Cromosoma cromo, int nro, int i, int j){
//        
//        for (int l = 1; l < cromo.cantClientes+1; l++) {
//            if(cromo.cromosoma[i][j].ruta[l] == nro){
//                return true;
//            }
//        }
//
//        return false;
//    }
}
