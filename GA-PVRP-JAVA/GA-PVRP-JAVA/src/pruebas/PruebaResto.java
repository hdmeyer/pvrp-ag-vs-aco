/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pruebas;

import gapvrpjava.ruta;

/**
 *
 * @author Cristhian Parra
 */
public class PruebaResto {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ruta[] camiones = new ruta[9];
        
        double aux = (double)camiones.length/2;
        int mitad = (int) Math.ceil(aux);
        
        
        for (int k = 0; k < camiones.length; k++) {
        
            int ind1 = k%9;
            int ind2 = (k+1)%9;
            
            System.out.println("En la iteración "+k+" intercambiamos "+ind1+" con "+ind2);
            
            
            
            /*    if(k + mitad >= camiones.length){
                mezclarRutas(camiones[k],camiones[0]);
                break;
            }else{
                mezclarRutas(camiones[k],camiones[k+mitad]);
            }*/
        }
    }

}
