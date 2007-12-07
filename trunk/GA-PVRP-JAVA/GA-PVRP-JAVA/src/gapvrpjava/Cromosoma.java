/*
 * Cromosoma.java
 *
 * Created on 16 de noviembre de 2007, 8:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gapvrpjava;
import java.util.StringTokenizer;
/**
 *
 * @author Huguis
 */
public class Cromosoma {
    int cromosoma [][][]=null;
    double fitness;
    /** Indica el orden en que se van tomando los clientes, estos se generan automaticamente*/
    int ordenVisitas[];
    int clienteActual;
    
    /** Creates a new instance of Cromosoma */
    public Cromosoma() {
    }
    /** Usaremos aqui un indice que va desde uno hasta la cantidad de clientes!!!, para 
     *poder manejar con sus indices reales
     Tanto los clientes como los vehiculos van desde cero hasta su valor menos 1*/
    public Cromosoma(Conocimiento entrada) {
        this.cromosoma = new int [entrada.dias][entrada.cantVehiculos][entrada.cantClientes+1];
        this.ordenVisitas = new int[entrada.cantClientes+1];
        for (int i = 0; i < this.ordenVisitas.length; i++) {
            this.ordenVisitas[i] = i;
        }
    }
    public void construirCromosoma(Conocimiento entrada){
        int contador = 0;
        int vehiculos[] = new int[entrada.cantVehiculos];
        this.aleatorio();
        
        for (int i = 0; i < vehiculos.length; i++) {
            vehiculos[i] = entrada.capacidad;
        }
        for (int i = 0; i < entrada.dias; i++) {
            entrada.InicializarListaVisitas();
            for (int j = 1; j < entrada.cantClientes+1; j++) {
                for (int k = 0; k < entrada.cantVehiculos; k++) {
                    /*INCREMENTAMOS UN CONTADOR PARA IR VISITANDO A LOS CLIENTES Y METIENDOLOS EN EL CROMOSOMA
                     LUEGO, SI LA CANTIDAD DE VISITAS QUE  NECSITAMOS PARA EL CLIENTE AUN NO FUE SATISFECHA,
                     LO VISITAMOS DE VUELTA Y DECREMENTAMOS LA CANTIDAD DE VISITAS NECESARIAS*/
                    /* En este if verificamos si la cantidad de visitas necesarias para ese cliente
                     *aun no fue satisfecha y si no fue visitado en ese dia.*/
                    contador++;
                    clienteActual = this.ordenVisitas[contador];
                    if(entrada.listaVisitas[1][clienteActual] > 0 && entrada.listaVisitas[0][clienteActual] == 0){
                        
                        /*Verificamos si la demanda del cliente puede ser satisfecha por este vehiculo*/
                        if((vehiculos[k]-entrada.clientes[clienteActual][4] >= 0)){
                            /*Si es asi, se carga en el cromosoma el nro de cliente a ser visitado*/
                            this.cromosoma[i][k][j] =(int)entrada.clientes[clienteActual][0];
                            entrada.listaVisitas[1][clienteActual]--;
                            entrada.listaVisitas[0][clienteActual] = 1;
                            
                        }
                        
                    }
                    if(contador == entrada.cantClientes){
                        //j = entrada.cantClientes+1;
                        //k = entrada.cantVehiculos;
                        contador =0;
                    }
                }
            }
        }
        entrada.FormatearListaVisitas();
    }
    public void aleatorio(){
        /*AHORA LO QUE HACEMOS ES ORDENAR ALEATORIAMENTE EL VECTOr*/
        int indice;
        for (int i = 2; i < ordenVisitas.length; i++) {
            do{
                indice =(int)(Math.random()* (i));
            }while(indice == 0);
            /*Asi aleatorizamos el vector ORDEN VISITAS lo que nos permite
             *usar el orden del vector para visitar a los clientes*/
            this.swap(i,indice);
        }
    }
    public void swap(int x, int y) {
        int temp = this.ordenVisitas[x];
        this.ordenVisitas[x] = this.ordenVisitas[y];
        this.ordenVisitas[y] = temp;
        
    }
    
    public void fObjetivo(Conocimiento entrada){
        int suma =0;
        for (int i = 0; i < entrada.dias; i++){
            for (int j = 0; j < entrada.cantVehiculos; j++){
                /*no nos vamos hasta el cliente mas 1 porque
                 * vamos haciendo cada valor con el sgte para calcular el
                 * costo, entonces cuando llegamos al n-1, tomamos con el valor
                 * de n*/
                for (int k = 1; k < entrada.cantClientes; k++) {
                    if(cromosoma[i][j][k] != 0){
                        suma += entrada.matrizCostos[cromosoma[i][j][k]][cromosoma[i][j][k+1]];
                    }
                }
            }
        }
        this.fitness = 1/suma;
        
    }
    public String toString(Conocimiento entrada){
        String granLinea = "";
        for (int j = 0; j < entrada.cantVehiculos; j++) {
            for (int i = 0; i < entrada.dias; i++) {
                granLinea += "[";
                for (int k = 1; k < entrada.cantClientes + 1; k++) {
                    if (cromosoma[i][j][k] != 0) {
                        granLinea += cromosoma[i][j][k] +"-";
                    }
                }
                granLinea += "]";
            }
            granLinea += "#";
        }
        
        return granLinea;
    }
    
    public String ImprimirCromo(String granLinea){
        String granMatriz ="";
            for (StringTokenizer stringTokenizer = new StringTokenizer(granLinea,"#"); stringTokenizer.hasMoreTokens();) {
                String token = stringTokenizer.nextToken();
                granMatriz += token + "\n";
            }
            
        return granMatriz;
    }
                
}