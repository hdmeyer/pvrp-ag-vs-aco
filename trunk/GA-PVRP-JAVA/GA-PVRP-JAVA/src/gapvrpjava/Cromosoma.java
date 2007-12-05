/*
 * Cromosoma.java
 *
 * Created on 16 de noviembre de 2007, 8:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gapvrpjava;

/**
 *
 * @author Huguis
 */
public class Cromosoma {
    int cromosoma [][][]=null;
    int fitness;
    int listaClientes[][] = null;
    /** Creates a new instance of Cromosoma */
    public Cromosoma() {
    }
    /** Usaremos aqui un indice que va desde uno hasta la cantidad de clientes!!!, para 
     *poder manejar con sus indices reales
     Tanto los clientes como los vehiculos van desde cero hasta su valor menos 1*/
    public Cromosoma(Conocimiento entrada) {
        this.cromosoma = new int [entrada.dias][entrada.cantVehiculos][entrada.cantClientes+1];
        this.listaClientes = new int [2][entrada.cantClientes+1];
    }
    public void construirCromosoma(Conocimiento entrada){
        int contador = 0;
        int vehiculos[] = new int[entrada.cantVehiculos];
        for (int i = 0; i < vehiculos.length; i++) {
            vehiculos[i] = entrada.capacidad;
        }
        for (int i = 0; i < entrada.dias; i++) {
            for (int j = 1; j < entrada.cantClientes+1; j++) {
                for (int k = 0; k < entrada.cantVehiculos; k++) {
                    /*INCREMENTAMOS UN CONTADOR PARA IR VISITANDO A LOS CLIENTES Y METIENDOLOS EN EL CROMOSOMA
                     LUEGO, SI LA CANTIDAD DE VISITAS QUE  NECSITAMOS PARA EL CLIENTE AUN NO FUE SATISFECHA,
                     LO VISITAMOS DE VUELTA Y DECREMENTAMOS LA CANTIDAD DE VISITAS NECESARIAS*/
                    /* En este if verificamos si la cantidad de visitas necesarias para ese cliente
                     *aun no fue satisfecha.*/
                    contador++;
                    if(entrada.clientes[contador][5] > 0){
                        
                        /*Verificamos si la demanda del cliente puede ser satisfecha por este vehiculo*/
                        if((vehiculos[k]-entrada.clientes[contador][4] >= 0)){
                            /*Si es asi, se carga en el cromosoma el nro de cliente a ser visitado*/
                            this.cromosoma[i][j][k] =(int)entrada.clientes[contador][0];
                            entrada.clientes[contador][5]--;
                            
                        }
                        
                    }
                    if(contador == entrada.cantClientes+1){
                        j = entrada.cantClientes+1;
                        k = entrada.cantVehiculos;
                        contador =0;
                    }
                }
            }
        }
    }
}
