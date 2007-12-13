/*
 * OACO.java
 *
 * Created on 11 de diciembre de 2007, 17:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package omicronaco;

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
    
    private int tamanoPoblacion;
    /** Creates a new instance of OACO */
    public OACO(Conocimiento entrada) {
        this.matrizFeromonas = new double [entrada.cantClientes+1][entrada.cantClientes+1];
        this.inicializarMatFeromonas();
        this.setTamanoPoblacion(entrada.cantidadHormigas);
        
    }
    
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
    
    public void actualizarMatrizFeromonas(Vector<Hormiga> sols){
        this.inicializarMatFeromonas();
        int x = 0;
        //ACA DEBEMOS ACTUALIZAR LAS FEROMONAS POR CADA ARCO QUE 
        // SE HAYA TOCADO DENTRO DE LAS SOLUCIONES...
        while(x < this.getTamanoPoblacion()){
            this.setNuevaHormiga(this.construirSolucion());
            
            if(this.estaContenido(this.getNuevaHormiga())){
                this.getSoluciones().add(this.getNuevaHormiga());
                x++;
            }
        }
    }

    public int getTamanoPoblacion() {
        return tamanoPoblacion;
    }

    public void setTamanoPoblacion(int tamanoPoblacion) {
        this.tamanoPoblacion = tamanoPoblacion;
    }
    
}
