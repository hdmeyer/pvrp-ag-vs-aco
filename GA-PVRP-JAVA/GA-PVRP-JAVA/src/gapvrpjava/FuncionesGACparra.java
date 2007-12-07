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
}
