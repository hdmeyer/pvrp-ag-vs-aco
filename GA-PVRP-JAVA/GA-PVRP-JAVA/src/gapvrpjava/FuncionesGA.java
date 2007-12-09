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

    
    /** Creates a new instance of FuncionesGA */
        
    public FuncionesGA() {
    }
    /*PODRIAMOS PONER A CERO CUANDO INICIALIZAMOS EL CROMOSOMA*/    
    
    
    public static Cromosoma[] Cruzar(Cromosoma c1, Cromosoma c2, Conocimiento entrada){
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
                    
                    // reiniciamos contador para recorrer adecuadamente la siguiente ruta
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
                    valor = (int) it1.next();
                    cruzado1.listaVisitasCromo[1][valor]--;
                }
                Iterator<Integer> it2 = cruzado2.cromosoma[i][j].ruta.iterator();
                while (it2.hasNext()) {
                    valor = (int) it2.next();
                    cruzado2.listaVisitasCromo[1][valor]--;
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
    
    public void crucePorPunto() {
    }
    /**
     * // Operar sobre c1 para mutarlo y colocar el resultado en mutado
     * @param c1
     * @param entrada
     * @return
     */
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
