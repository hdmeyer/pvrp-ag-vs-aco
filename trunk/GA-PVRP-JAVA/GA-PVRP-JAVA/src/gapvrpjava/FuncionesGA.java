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
        int corte = (int) c1.cantClientes/2;
        int relleno = 0;
        
        // Realizar el cruce de c1 y c2, y producir el resultado en 
        // cruzado1 y cruzado2
        for (int j = 0; j < c1.cantVehiculos; j++) {
            for (int i = 0; i < c1.dias; i++) {
                relleno =0;
                for (int k = 1; k < c1.cantClientes + 1; k++) {
                    if(k < corte){
                        if(!revisarRepetido(cruzado1, c1.cromosoma[i][j].ruta[k])){
                            cruzado1.cromosoma[i][j].ruta[k] = c1.cromosoma[i][j].ruta[k];
                            cruzado1.listaVisitasCromo[1][k]--;
                        }
                        if(!revisarRepetido(cruzado2, c2.cromosoma[i][j].ruta[k])){
                            cruzado2.cromosoma[i][j].ruta[k] = c2.cromosoma[i][j].ruta[k];
                            cruzado2.listaVisitasCromo[1][k]--;
                        }
                        
                    }else{
                        relleno++;
                        if(!revisarRepetido(cruzado1, c2.cromosoma[i][j].ruta[relleno])){
                            cruzado1.cromosoma[i][j].ruta[k] = c2.cromosoma[i][j].ruta[relleno];
                            cruzado1.listaVisitasCromo[1][relleno]--;
                        }
                        if(!revisarRepetido(cruzado2, c1.cromosoma[i][j].ruta[relleno])){
                            cruzado2.cromosoma[i][j].ruta[k] = c1.cromosoma[i][j].ruta[relleno];
                            cruzado2.listaVisitasCromo[1][relleno]--;
                        }
                    }
                }
            }
        }
        
        
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
    
    
    public static Cromosoma[] seleccion(Poblacion p) {
        /** 
         * @TODO 
         * 
         */
        return new Cromosoma[p.getTamanho()];
    }
    
    public static boolean revisarRepetido(Cromosoma cromo, int nro){
        
        return true;
    }
}
