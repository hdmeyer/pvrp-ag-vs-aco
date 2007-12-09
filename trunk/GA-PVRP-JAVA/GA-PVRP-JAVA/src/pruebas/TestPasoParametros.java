/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pruebas;

/**
 *
 * @author Cristhian Parra
 */
public class TestPasoParametros {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int [] visitasLocales = new int[10+1];        
        for (int k = 0; k < visitasLocales.length; k++) {
                     visitasLocales[k]= 0;
        }
        
        cambiarPrimeraMitad(visitasLocales);
        
        for (int k = 0; k < visitasLocales.length; k++) {
           System.out.print(visitasLocales[k]+"-");
        }
        
    }

    private static void cambiarPrimeraMitad(int[] visitasLocales) {
        
        double mitad = (double) visitasLocales.length/2;
                
        int corte = (int) Math.ceil(mitad);
        System.out.println("length: "+visitasLocales.length+"- corte: "+corte+"- mitad: "+mitad);
        for (int k = 0; k < corte; k++) {
           visitasLocales[k]++;
        }
        
    }

}
