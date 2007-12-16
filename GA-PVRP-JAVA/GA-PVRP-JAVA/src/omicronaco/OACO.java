/*
 * OACO.java
 *
 * Created on 11 de diciembre de 2007, 17:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package omicronaco;

import java.util.Iterator;
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
    private int megatron;
    private Hormiga hActual;
    private int generaciones = 100;
    
    /** Creates a new instance of OACO */
    public OACO(Conocimiento entrada) {
        this.matrizFeromonas = new double [entrada.cantClientes+1][entrada.cantClientes+1];
        this.inicializarMatFeromonas();
        this.setTamanoPoblacion(entrada.cantidadHormigas);
        this.setMegatron(entrada.megatron);
        this.hActual = new Hormiga(entrada);
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
        Iterator<Hormiga> itHormiga = sols.iterator();
        while(itHormiga.hasNext()){
            this.sethActual((Hormiga)itHormiga.next());
            
            for (int i = 0; i < this.gethActual().getCaminos().length; i++) {
                
                for (int j = 0; j < this.gethActual().getCaminos()[i].length; j++) {
                    
                    if(!this.gethActual().getCaminos()[i][j].getRuta().isEmpty()){
                        
                        Iterator<Integer> itRuta = this.gethActual().getCaminos()[i][j].getRuta().iterator();
                        int actual = (int) itRuta.next();
                        int posterior = 0;
                        while(itRuta.hasNext()){
                            
                            posterior = (int) itRuta.next();
                            this.matrizFeromonas[actual][posterior] += this.getMegatron()/this.tamanoPoblacion;
                            actual = posterior;
                            
                        }
                    }
                }
            }
        }
    }

    public int getTamanoPoblacion() {
        return tamanoPoblacion;
    }

    public void setTamanoPoblacion(int tamanoPoblacion) {
        this.tamanoPoblacion = tamanoPoblacion;
    }

    public int getMegatron() {
        return megatron;
    }

    public void setMegatron(int megatron) {
        this.megatron = megatron;
    }

    public Hormiga gethActual() {
        return hActual;
    }

    public void sethActual(Hormiga hActual) {
        this.hActual = hActual;
    }
    
    public void elPurete(Conocimiento entrada, int corridas){
        System.out.println(" CANTIDAD DE CORRIDAS" + corridas);
        System.out.println(" CANTIDAD DE GENERACIONES" + this.generaciones);
        this.inicializarMatFeromonas();
        Poblacion P= new Poblacion(entrada,this.matrizFeromonas);
        P.inicializarPoblacion();
        /*ya se hace en inicializar poblacion*/
        //P.sortSoluciones();
        
        int x =0;
        int y = 0;
        while ( x < corridas){
            while(y < this.generaciones){
                
                P.construirHormiga();
                
                if(P.getNuevaHormiga().getCostoTotal() < ((Hormiga)P.getSoluciones().get(0)).getCostoTotal()){
                    if(!P.estaContenido(P.getNuevaHormiga())){
                        P.actualizarSoluciones();
                        y++;
                    }
                }
            }
            this.actualizarMatrizFeromonas(P.getSoluciones());
            x++;
        }
        System.out.println(P.toString());
    }
    
}
