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

        // Realizar el cruce de c1 y c2, y producir el resultado en 
        // cruzado1 y cruzado2
        for (int j = 0; j < c1.cantVehiculos; j++) {
            for (int i = 0; i < c1.dias; i++) {
                
                int c1Size = c1.cromosoma[i][j].ruta.size();
                int c2Size = c2.cromosoma[i][j].ruta.size();
                
                if (c1Size > 0 && c2Size > 0) {
                    corte1 = (int)c1.cromosoma[i][j].ruta.size()/2;
                    corte2 = (int)c2.cromosoma[i][j].ruta.size()/2;

                    List<Integer> v1 = c1.cromosoma[i][j].ruta.subList(0, corte1);
                    cruzado1.cromosoma[i][j].ruta.addAll(v1);
                    List<Integer> v2 = c2.cromosoma[i][j].ruta.subList(0, corte2);
                    cruzado2.cromosoma[i][j].ruta.addAll(v2);
                    /*
                    List<Integer> v3 = c1.cromosoma[i][j].ruta.subList(corte1,c1.cromosoma[i][j].ruta.size());
                    List<Integer> v4 = c2.cromosoma[i][j].ruta.subList(corte2,c2.cromosoma[i][j].ruta.size());
                    */

                    Iterator<Integer> it1 = c1.cromosoma[i][j].ruta.iterator();
                    Iterator<Integer> it2 = c2.cromosoma[i][j].ruta.iterator();

                    while(it1.hasNext()){
                        valor = (int)it1.next();
                        if (!cruzado2.cromosoma[i][j].ruta.contains(valor)&& contador <= corte1) {
                            cruzado2.cromosoma[i][j].ruta.add(valor);
                            contador++;
                            cruzado2.listaVisitasCromo[1][valor]--;
                        }
                    }
                    contador =0;
                    while(it2.hasNext()){
                        valor = (int)it2.next();
                        if (!cruzado1.cromosoma[i][j].ruta.contains(valor) && contador <= corte2) {
                            cruzado1.cromosoma[i][j].ruta.add(valor);
                            contador++;
                            cruzado1.listaVisitasCromo[1][valor]--;
                        }
                    } 
                // si uno está vacío y el otro no, partir el que no está vacío 
                // en dos. Si ambos están vacío, yapiro forzado
                } else if (c1Size == 0 || c2Size == 0) {
                    
                    if (c1Size > 0) {
                        corte1 = c1Size/2;

                        List<Integer> v1 = c1.cromosoma[i][j].ruta.subList(0, corte1);
                        cruzado1.cromosoma[i][j].ruta.addAll(v1);
                        List<Integer> v2 = c1.cromosoma[i][j].ruta.subList(corte1, c1Size);
                        cruzado2.cromosoma[i][j].ruta.addAll(v2);
                        
                    } else if (c2Size > 0) {
                        
                        corte2 = c2Size/2;

                        List<Integer> v1 = c2.cromosoma[i][j].ruta.subList(0, corte2);
                        cruzado1.cromosoma[i][j].ruta.addAll(v1);
                        List<Integer> v2 = c2.cromosoma[i][j].ruta.subList(corte2, c2Size);
                        cruzado2.cromosoma[i][j].ruta.addAll(v2);
                    }                    
                }
                /*ACTUALIZAMOS LA CANTIDAD DE VECINOS*/
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
            }
        }
        
        cruzado1.evaluar(entrada);
        cruzado2.evaluar(entrada);
        
        cruceResult[0] = cruzado1;
        cruceResult[1] = cruzado2;
        
        return cruceResult;
    }
    
    public void crucePorPunto() {
    }
    
    public static Cromosoma Mutar(Cromosoma c1) {
        Cromosoma mutado = new Cromosoma();
        
        // Operar sobre c1 para mutarlo y colocar el resultado en mutado
        
        return mutado;
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
                double costo1 = p.getFitness(ind1);
                double costo2 = p.getFitness(ind2);

                // Competencia
                if (costo1>=costo2) { // Gan? individuo 1
                        Mejores[i]=p.getIndividuo(ind1);
                }
                else { // Gano individuo 2
                        Mejores[i]=p.getIndividuo(ind2);
                }
        }

        return Mejores;
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
