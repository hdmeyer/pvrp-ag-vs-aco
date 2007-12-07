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
    
    
    public static Cromosoma[] Cruzar(Cromosoma c1, Cromosoma c2){
        Cromosoma cruzado1 = new Cromosoma();
        Cromosoma cruzado2 = new Cromosoma();
        Cromosoma[] cruceResult = new Cromosoma[2];
        
        // Realizar el cruce de c1 y c2, y producir el resultado en 
        // cruzado1 y cruzado2
        
        cruceResult[0] = cruzado1;
        cruceResult[1] = cruzado2;
        
        return cruceResult;
    }
    
    public static Cromosoma Mutar (Cromosoma c1){
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
}
