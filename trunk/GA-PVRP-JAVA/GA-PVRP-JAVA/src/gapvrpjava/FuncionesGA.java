/*
 * FuncionesGA.java
 *
 * Created on 1 de noviembre de 2007, 12:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gapvrpjava;

/**
 *
 * @author Propietario
 */
public class FuncionesGA {
    int cromosoma[][][] = null;
    /** Creates a new instance of FuncionesGA */
    public FuncionesGA() {
    }
    /*PODRIAMOS PONER A CERO CUANDO INICIALIZAMOS EL CROMOSOMA*/    
    public void inicializarCromosoma(Lector entrada){
        this.cromosoma = new int [entrada.dias][entrada.cantVehiculos][entrada.cantClientes];
    }
    public void construirCromosoma(Lector entrada){
        int contador = 0;
        int vehiculos[] = new int[entrada.cantVehiculos];
        for (int i = 0; i < vehiculos.length; i++) {
            vehiculos[i] = entrada.capacidad;
        }
        for (int i = 0; i < entrada.dias; i++) {
            for (int j = 0; j < entrada.cantClientes; j++) {
                for (int k = 0; k < entrada.cantVehiculos; k++) {
                    /*INCREMENTAMOS UN CONTADOR PARA IR VISITANDO A LOS CLIENTES Y METIENDOLOS EN EL CROMOSOMA
                     LUEGO, SI LA CANTIDAD DE VISITAS QUE LE NECSITAMOS PARA EL CLIENTE AUN NO FUE SATISFECHA,
                     LO VISITAMOS DE VUELTA Y DECREMENTAMOS LA CANTIDAD DE VISITAS NECESARIAS*/
                    contador++;
                    if(entrada.inicial[contador][5] > 0){
                        if((vehiculos[k]-entrada.inicial[contador][4] >= 0)){
                            this.cromosoma[i][j][k] =(int)entrada.inicial[contador][0];
                            entrada.inicial[contador][5]--;
                        }// falta ver cuando no asignamos al cliente....
                        
                    }
                    if(contador == entrada.cantClientes){
                        j = entrada.cantClientes;
                        k = entrada.cantVehiculos;
                        contador =0;
                    }
                }
                
            }
            
        }
    }
}
