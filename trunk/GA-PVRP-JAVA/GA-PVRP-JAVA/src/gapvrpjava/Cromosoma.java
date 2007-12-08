/*
 * Cromosoma.java
 *
 * Created on 16 de noviembre de 2007, 8:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gapvrpjava;
import java.util.Iterator;
import java.util.StringTokenizer;
/**
 *
 * @author Huguis
 */
public class Cromosoma {
    public ruta cromosoma [][]=null;
    private double fitness;
    private double costo;
    /** Indica el orden en que se van tomando los clientes, estos se generan automaticamente*/
    public int ordenVisitas[];
    public int clienteActual;
    public int listaVisitasCromo [][];
    public int cantVehiculos;
    public int cantClientes;
    public int dias;
    public int vehiculos[];
    public int capacidad;
    
    /** Creates a new instance of Cromosoma */
    public Cromosoma() {
    }
    /** Usaremos aqui un indice que va desde uno hasta la cantidad de clientes!!!, para 
     *poder manejar con sus indices reales
     Tanto los clientes como los vehiculos van desde cero hasta su valor menos 1*/
    public Cromosoma(Conocimiento entrada) {
        /*INICIALIZAMOS LA MATRIZ*/
        this.cromosoma = new ruta [entrada.dias][entrada.cantVehiculos];
        for (int i = 0; i < entrada.dias; i++) {
            for (int j = 0; j < entrada.cantVehiculos; j++) {
                this.cromosoma[i][j]= new ruta(entrada);
            }
        }

        this.ordenVisitas = new int[entrada.cantClientes+1];
        for (int i = 0; i < this.ordenVisitas.length; i++) {
            this.ordenVisitas[i] = i;
        }
        this.cantVehiculos = entrada.cantVehiculos;
        this.cantClientes = entrada.cantClientes;
        this.dias = entrada.dias;
        this.listaVisitasCromo =new int [2][this.cantClientes+1];
        copiar(entrada.listaVisitas);
        this.vehiculos = new int[entrada.cantVehiculos];
        this.capacidad = entrada.capacidad;
    }
    public void construirCromosoma(Conocimiento entrada){
        int contador = 0;
        /*GENERA UN VECTOR QUE LO USAMOS PARA CONSTRUIR el cromosoma*/
        this.aleatorio();
        for (int i = 0; i < this.dias; i++) {
            this.InicializarListaVisitas();
            this.InicializarVehiculos();
            for (int j = 1; j < this.cantClientes+1; j++) {
                for (int k = 0; k < this.cantVehiculos; k++) {
                    /*INCREMENTAMOS UN CONTADOR PARA IR VISITANDO A LOS CLIENTES Y METIENDOLOS EN EL CROMOSOMA
                     LUEGO, SI LA CANTIDAD DE VISITAS QUE  NECSITAMOS PARA EL CLIENTE AUN NO FUE SATISFECHA,
                     LO VISITAMOS DE VUELTA Y DECREMENTAMOS LA CANTIDAD DE VISITAS NECESARIAS*/
                    /* En este if verificamos si la cantidad de visitas necesarias para ese cliente
                     *aun no fue satisfecha y si no fue visitado en ese dia.*/
                    contador++;
                    clienteActual = this.ordenVisitas[contador];
                    if(this.listaVisitasCromo[1][clienteActual] > 0 && this.listaVisitasCromo[0][clienteActual] == 0){
                        
                        /*Verificamos si la demanda del cliente puede ser satisfecha por este vehiculo*/
                        if((this.vehiculos[k]-entrada.clientes[clienteActual][4] >= 0)){
                            /*Si es asi, se carga en el cromosoma el nro de cliente a ser visitado*/
                            this.cromosoma[i][k].ruta.add((int)entrada.clientes[clienteActual][0]);
                            this.listaVisitasCromo[1][clienteActual]--;
                            this.listaVisitasCromo[0][clienteActual] = 1;
                            /*Actualizamos la capacidad del cliente*/
                            //this.vehiculos[k] -= (int)entrada.clientes[clienteActual][4];
                        }
                    }
                    if(contador == this.cantClientes){
                        //j = entrada.cantClientes+1;
                        //k = entrada.cantVehiculos;
                        contador =0;
                    }
                }
            }
        }
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
    
    public double evaluar(Conocimiento entrada) {
        
        this.fObjetivo(entrada);        
        return this.getFitness();
    }
    
    public boolean isValido() {
        
        for (int i = 0; i < listaVisitasCromo.length; i++) {
            if (this.listaVisitasCromo[1][i] > 0) {
                return false;
            }
        }        
        return true;

    }
    
    public void fObjetivo(Conocimiento entrada){
        int ultimoVisitado = 0;
        int penalizacion=0;
        int actual;
        int posterior;
        this.setCosto(0);
        for (int i = 0; i < entrada.dias; i++){
            for (int j = 0; j < entrada.cantVehiculos; j++){
                /*no nos vamos hasta el cliente mas 1 porque
                 * vamos haciendo cada valor con el sgte para calcular el
                 * costo, entonces cuando llegamos al n-1, tomamos con el valor
                 * de n*/
                if (cromosoma[i][j].ruta.size() > 0) {
                    Iterator<Integer> it = cromosoma[i][j].ruta.iterator();
                    actual = (int) it.next();
                    while(it.hasNext()){

                        posterior = (int) it.next();
                        this.cromosoma[i][j].costo += entrada.matrizCostos[actual][posterior];
                        actual = posterior;

                        ultimoVisitado = posterior;

                    }

                    /*Sumamos las cantidades para ir de 0 al primero y para ir del ultimo
                     * al deposito de vuelta...*/
                    this.cromosoma[i][j].costo += entrada.matrizCostos[0][(Integer)cromosoma[i][j].ruta.firstElement()];
                    this.cromosoma[i][j].costo += entrada.matrizCostos[0][(Integer)cromosoma[i][j].ruta.lastElement()];
                    this.setCosto(this.getCosto() + this.cromosoma[i][j].costo);
                }
            }
        }
        /** AQUI PENALIZAMOS SI ES QUE NO SE VISITO A ALGUIEN*/
        for (int i = 0; i < this.cantClientes+1; i++) {
            if(this.listaVisitasCromo[1][i] > 0){
                penalizacion += entrada.matrizCostos[0][i]*this.listaVisitasCromo[1][i];
            }
        }

        this.setCosto(this.getCosto() + penalizacion);
        double fit = (double) (1/this.getCosto());
        this.setFitness(fit);
        
    }
    
    @Override
    public String toString(){
        String granLinea = "";
        for (int j = 0; j < this.cantVehiculos; j++) {
            for (int i = 0; i < this.dias; i++) {
                granLinea += "[";
                
                Iterator<Integer> it = cromosoma[i][j].ruta.iterator();
                while(it.hasNext()){
                    
                    granLinea += it.next() + "-";
                    
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
    
    /** INICIALIZ LA LISTA DE VISITAS POSIBLES QUE ES UN VECTOR DE 2 DIMENSIONES
     * DONDE LA PRIMERA DIMENSION INDICA SI SE VISITO O NO A UN CLIENTE EN UN DIA
     * ESPECIFICO, Y LA SEGUNDA DIMENSION LA CANTIDAD DE VISITAS NECESARIAS DE 
     * CADA CLIENTE...
     */
    public void InicializarListaVisitas(){
        for (int i = 0; i < this.cantClientes+1; i++) {
            this.listaVisitasCromo[0][i] = 0;
        }
    }

    private void copiar(int[][] listaVisitas) {
        for (int i = 0; i < this.cantClientes+1; i++) {
            this.listaVisitasCromo[1][i] = listaVisitas[1][i];
            this.listaVisitasCromo[0][i] = 0;
        }
    }
    public void InicializarVehiculos(){
        for (int i = 0; i < vehiculos.length; i++) {
            vehiculos[i] = this.capacidad;
        }
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }
                
}
