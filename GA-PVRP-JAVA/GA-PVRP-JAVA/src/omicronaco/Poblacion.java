/*
 * Poblacion.java
 *
 * Created on 11 de diciembre de 2007, 13:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package omicronaco;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author Propietario
 */
public class Poblacion {
    private Vector<Hormiga> soluciones;
    private int tamanoPoblacion;
    private Hormiga nuevaHormiga;
    private Vector<Nodo> clientes;
    private int [] visitasGlobales;
    private int dias;
    private int camiones;
    private int cantClientes;
    private double [][] matrizFeromonas;
    private Nodo nActual;
    private Nodo nProbabilidad;
    private double [][] matrizCostos;
    private double alfa =1;
    private double beta =2;
    private Conocimiento conocPob;
    
    /** Creates a new instance of Poblacion 
     * Se carga todos los datos necesarios a partir de los datos
     * de entrada.
     */
    public Poblacion(Conocimiento entrada, double [][] matrizFeromonas) {
        
        this.setSoluciones(new Vector(entrada.cantidadHormigas));
        this.setTamanoPoblacion(entrada.cantidadHormigas);
        this.setCantClientes(entrada.cantClientes);
        this.setCamiones(entrada.cantVehiculos);
        this.setDias(entrada.dias);
        this.setNuevaHormiga(new Hormiga(entrada));
        
        this.clientes = new Vector(this.cantClientes);
        this.inicializarClientesDisp();
        this.matrizFeromonas = matrizFeromonas;
        this.setNActual(new Nodo());
        this.setNProbabilidad(new Nodo());
        this.setMatrizCostos(entrada.matrizCostos);
        this.setVisitasGlobales(new int[this.cantClientes+1]);
        this.copiar(entrada.listaVisitas);
        
        this.setConocPob(entrada);
        
    }

    public Vector getSoluciones() {
        return soluciones;
    }

    public void setSoluciones(Vector soluciones) {
        this.soluciones = soluciones;
    }
    /**
     *Construye una poblacion inicial de hormigas.
     */
    public void inicializarPoblacion(){
        
        int x = 0;
        while(x < this.getTamanoPoblacion()){
            this.construirHormiga();
            
            if(!this.estaContenido(this.getNuevaHormiga())){
                this.getSoluciones().add(this.getNuevaHormiga());
                x++;
            }else{
                
            }
        }
        this.sortSoluciones();
    }
    /**
     *Actualiza la poblaci�n de hormigas con una nueva hormiga si
     *se cumple con las restricciones necesarias
     */
    public void actualizarSoluciones(){
        this.soluciones.remove(0);
        this.soluciones.add(0,this.nuevaHormiga);
        this.sortSoluciones();
    }

    public int getTamanoPoblacion() {
        return tamanoPoblacion;
    }

    public void setTamanoPoblacion(int tamanoPoblacion) {
        this.tamanoPoblacion = tamanoPoblacion;
    }

    public Hormiga getNuevaHormiga() {
        return nuevaHormiga;
    }

    public void setNuevaHormiga(Hormiga nuevaHormiga) {
        this.nuevaHormiga = nuevaHormiga;
    }

    /**
     *Verifica si una hormiga esta contenida ya dentro de la poblacion
     *fijandose solo en el costo total, porque recorrer toda la hormiga
     *seria computacionalmente mas costoso.
     */
    public boolean estaContenido(Hormiga hormiga) {
        Iterator<Hormiga> it = this.getSoluciones().iterator();
        while(it.hasNext()){
            if(hormiga.getCostoTotal() == ((Hormiga)it.next()).getCostoTotal()){
                return true;
            }
        }
        return false;
    }
    /**
     *Ordena las hormigas o soluciones con quicksort
     */
    public void sortSoluciones() {
        QuickSort.quicksort(this.soluciones,0,this.soluciones.size()-1,true,true);
    }
    /**
     *Construye una hormiga completa con todas sus rutas para cada
     *dia y camion respectivamente
     */
    public void construirHormiga(){
        this.setNuevaHormiga(new Hormiga(this.getConocPob()));
        this.copiar(this.getConocPob().listaVisitas);
        int contClientes = 0;
        int elegido =0;
        /*antes de iniciar el ciclo debemos elegir un cliente al azar*/
        
        /*GENERA UN VECTOR QUE LO USAMOS PARA CONSTRUIR el cromosoma*/
        for (int i = 0; i < this.getDias(); i++) {
            
            //* reinicializar posicion de la hormiga a cero 
            // El cliente cero debe estar si o si al inicio y al final de cada ruta
            // incluirlos sin hacerles pasar por el proceso de selecci�n ni de c�lculo de probabilidades
            
            this.reiniciarClientes();
            
            //this.InicializarVehiculos();
            for(int j = 0; j<this.getCantClientes();j++){
                
                
                for (int k = 0; k < this.getCamiones(); k++) {
                    
                    //PRIMERO CALCULAMOS LAS PROBABILIDADES DE TODOS LOS VECINOS
                    //DE LA POSICION ACTUAL DE MI HORMIGA.
                    /*AK ITERAMOS POR TODOS LOS CLIENTES QUE HAY*/
                    
                    
                    
                    this.calcularProbabilidades();
                    this.normalizarProbabilidades();
                    this.ordenarProbabilidades();
                    int posCElegido = this.seleccionarCliente();
                    if(posCElegido != -1 &&  this.clientes.get(posCElegido).getIdNodo() != 0){
                        elegido = this.clientes.get(posCElegido).getIdNodo();
                        if(this.clientes.get(posCElegido).isDisponible()){
                            this.getNuevaHormiga().getCaminos()[i][k].getRuta().add(elegido);
                            this.getNuevaHormiga().setPosicion(elegido);
                            this.visitasGlobales[elegido]--;
                            this.clientes.get(posCElegido).setDisponible(false);
                        }
                    }else{
                        
                        k = this.getCamiones();
                        j = this.getCantClientes();
                        
                        break;
                    }
                                        
                    if(posCElegido == -1){
                        break;
                    }
                }       
            }         
             
            
        }
        this.getNuevaHormiga().calcularCostoTotal();
        //System.out.println(this.getNuevaHormiga().toString());
    }
         
    /**
     *Creamos nodos nuevos y los vamos insertando en el VectorClientes
     **/
    public void inicializarClientesDisp(){
        //desde el 1 al 51
        int x = 1;
        while(x <= this.getCantClientes()){
            this.getClientes().add(new Nodo(x,0));
            x++;
        }
    }
    /**
     *Se setea de vuelta la disponibilidad
     *de los clientes de acuerdo a la cantidad de visitas
     *que requiere cada uno
     */
    public void reiniciarClientes(){
        Iterator it = this.clientes.iterator();
        while(it.hasNext()){
            this.setNActual((Nodo)it.next());
            this.getNActual().setProbabilidad(0);
            //Solo reiniciamos si todavia no cumplio con sus necesidades
            //de visitas.
            if(this.visitasGlobales[this.getNActual().getIdNodo()] > 0){
                this.getNActual().setDisponible(true);
            }
        }
        this.getNuevaHormiga().setPosicion(0);
    }
    
    public void copiar(int[][] listaVisitas) {
        for (int i = 0; i < listaVisitas[1].length; i++) {
            this.getVisitasGlobales()[i] = listaVisitas[1][i];
        }
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public int getCamiones() {
        return camiones;
    }

    public void setCamiones(int camiones) {
        this.camiones = camiones;
    }

    public int getCantClientes() {
        return cantClientes;
    }

    public void setCantClientes(int cantClientes) {
        this.cantClientes = cantClientes;
    }

    public Vector<Nodo> getClientes() {
        return clientes;
    }

    public void setClientes(Vector<Nodo> clientesDisponiblesDia) {
        this.clientes = clientesDisponiblesDia;
    }

    public int[] getVisitasGlobales() {
        return visitasGlobales;
    }

    public void setVisitasGlobales(int[] visitasGlobales) {
        this.visitasGlobales = visitasGlobales;
    }

    public double[][] getMatrizFeromonas() {
        return matrizFeromonas;
    }

    public void setMatrizFeromonas(double[][] matrizFeromonas) {
        this.matrizFeromonas = matrizFeromonas;
    }
    
    public void calcularProbabilidades(){
        /*ACA DEBEMOS CALCULAR LA PROBABILIDAD DE TODOS LOS NODOS DISPONIBLES EN 
         *ESE DIA para el nodo en el que estamos actualmente, 
         *PARA ESO CON EL PASO DE CADA DIA TENEMOS QUE REINICIALIZAR
         *EL VALOR DISPONIBLE DEL NODO A TRUE E IR VERIFICANDO LA CANTIDAD DE 
         *VISITAS QUE REQUIERE
         */
        Iterator it = this.clientes.iterator();
        while(it.hasNext()){
            double parteArriba = 0;
            double sumatoria = 0;
            this.setNActual((Nodo) it.next());
            
            /*ACA VERIFICAMOS SI EL NODO AUN ESTA DISPONIBLE PARA SER
             *VISITADO*/
            if(this.getNActual().isDisponible()){
                
                // sumatoria las informaciones en los dem�s clientes
                sumatoria = this.calcularSumatoria();
                
                // primero calculamos todo lo relativo a la matriz de feromonas
                Hormiga hormigaActual = this.getNuevaHormiga();
                Nodo nodoActual = this.getNActual();
                
                int posActual = hormigaActual.getPosicion();
                int posNodo = nodoActual.getIdNodo();
                
                double feromonas = matrizFeromonas[posActual][posNodo];                
                double infoHistorica = Math.pow(feromonas,alfa);

                // luego, calculamos la informaci�n heur�stica                
                double costo = this.getMatrizCostos()[posActual][posNodo];                
                double inversaCosto = (double) 1/costo;
                double infoHeuristica = Math.pow(inversaCosto,beta); 
                
                //
                if (posActual == posNodo) {
                    System.out.println("SUMATORIA-->"+inversaCosto+" infoHeuristica -->"+infoHistorica);   
                }
                parteArriba = infoHistorica * infoHeuristica;
            }
            
            if(this.getNActual().isDisponible()){
                double probabilidad = parteArriba/sumatoria;
                this.getNActual().setProbabilidad(probabilidad);
            }else{
                this.getNActual().setProbabilidad(0);
            }
            
        }
    }

    /**
     *AQUI CALCULAMOS EL DENOMINADOR DE LA FUNCION DE PROBABILIDAD
     *PARA CADA UNA DE LAS POSIBLES VISITAS.**/
    public double calcularSumatoria() {
        double resultado=0;
        Iterator iter = this.clientes.iterator();
        
        while(iter.hasNext()) {

            this.setNProbabilidad((Nodo)iter.next());

            // primero calculamos todo lo relativo a la matriz de feromonas
            Hormiga hormigaActual = this.getNuevaHormiga();
            Nodo nodoActual = this.getNProbabilidad();

            int posActual = hormigaActual.getPosicion();
            int posNodo = nodoActual.getIdNodo();

            double feromonas = matrizFeromonas[posActual][posNodo];                
            double infoHistorica = Math.pow(feromonas,alfa);

            // luego, calculamos la informaci�n heur�stica                
            double costo = this.getMatrizCostos()[posActual][posNodo];                
            double inversaCosto = 1/costo;
            double infoHeuristica = Math.pow(inversaCosto,beta);               

            if ( nodoActual.isDisponible() && 
                 posActual != posNodo ) {
                
                resultado += infoHeuristica * infoHistorica;
            }
        }
        
        return resultado;
    }

    public Nodo getNActual() {
        return nActual;
    }

    public void setNActual(Nodo nActual) {
        this.nActual = nActual;
    }

    public Nodo getNProbabilidad() {
        return nProbabilidad;
    }

    public void setNProbabilidad(Nodo nProbabilidad) {
        this.nProbabilidad = nProbabilidad;
    }

    public double[][] getMatrizCostos() {
        return matrizCostos;
    }

    public void setMatrizCostos(double[][] matrizCostos) {
        this.matrizCostos = matrizCostos;
    }
    /**
     *LO UNICO QUE HARA ESTE METODO ES CALCULAR LA SUMA DE TODAS LAS PROBABILIDADES
     *EXISTENTES PARA TODOS LOS NODOS CALCULADOS Y LUEGO PROCEDERA A DIVIDIR
     *CADA UNA DE ESTAS PROBABILIDADES PARA QUE ASI EN SU TOTALIDAD SUMEN 1.
     **/
    private void normalizarProbabilidades() {
        double totalProbabilidades =0;
        Iterator it = this.clientes.iterator();
        
        while(it.hasNext()){
            
            this.setNActual((Nodo)it.next());
            totalProbabilidades += (double)this.getNActual().getProbabilidad();
        }
        
        Iterator it1 = this.clientes.iterator();
        
        while (it1.hasNext()){

            this.setNActual((Nodo) it1.next());
            
            double nuevaProb = (double) this.getNActual().getProbabilidad()/totalProbabilidades;
            
            this.getNActual().setProbabilidad(nuevaProb);
        }
    }

    public void ordenarProbabilidades() {
        QuickSort.quicksort(this.clientes,0,this.clientes.size()-1,true,false);
    }
    /**
     *Se utiliza un metodo de ruleta con prioridades, tratando
     *de favorecer a los nodos menos costosos
     */
    public int seleccionarCliente (){
        
        double aleatorio=(double)(Math.random());
        //System.out.println("Aleatorio en seleccionar cliente SALE: "+aleatorio+"");
        double suma=0;
        suma += clientes.get(0).getProbabilidad();
        for (int i=1;i < this.cantClientes;i++){
            
            if (suma > aleatorio){
                if (i != 1 && this.clientes.get(i-1).isDisponible()){
                    return i-1;//this.clientes.get(i-1).getIdNodo();
                }
                if(this.clientes.get(i).isDisponible()){
                    return i;//this.clientes.get(i).getIdNodo();    
                }
            }
            suma += clientes.get(i).getProbabilidad();
        }
//        for (int i=0;i < this.cantClientes;i++){
//            if(this.clientes.get(i).isDisponible()){
//                return i;
//            }
//        }
        if(this.clientes.get(this.cantClientes-1).isDisponible()){
            //System.out.println("DEVOLVIO EL ULTIMO");
            return this.cantClientes-1;//this.clientes.get(this.cantClientes - 1).getIdNodo();
        }
        return -1;
    }
    /* ELEGIMOS EL NODO A VISITAR*/
//    public int seleccionarCliente (){
//        
//        double aleatorio= (double) Math.random();
//        
//        //System.out.println("Aleatorio en seleccionar cliente SALE: "+aleatorio+"");
//        Nodo primerCliente = this.clientes.get(0);
//        
//        double suma = 0;
//        if (primerCliente.isDisponible()) {
//            suma = primerCliente.getProbabilidad();
//        } else {
//            return -1;
//        }
//        
//        
//        for (int i=1;i <= this.cantClientes;i++){
//            Nodo clienteActual = clientes.get(i-1);
//            
//            if (suma > aleatorio){
//                if(clienteActual.isDisponible()){
//                    return i-1;//this.clientes.get(i).getIdNodo();    
//                }
//            }
//            
//            if (i<cantClientes) {
//               suma += clientes.get(i).getProbabilidad();
//            }
//        }
//        
////        for (int i=0;i < this.cantClientes;i++){
////            if(this.clientes.get(i).isDisponible()){
////                return i;
////            }
////        }
////        if(this.clientes.get(this.cantClientes-1).isDisponible()){
////            return this.cantClientes;//this.clientes.get(this.cantClientes - 1).getIdNodo();
////        }
//        return -1;
//    }
    
    public String toString(){
        String retorno = "";
        Iterator<Hormiga> it = this.getSoluciones().iterator();
        Hormiga hormigaImpr;
        while(it.hasNext()){
            hormigaImpr = it.next();
            retorno += hormigaImpr.toString();
            retorno += "\n";
            retorno += "COSTO: "+ hormigaImpr.getCostoTotal() + "\n";
            retorno += "----------------------------------------------------------------------------------------------";
            retorno += "\n";
        }
        return retorno;
    }

    public Conocimiento getConocPob() {
        return conocPob;
    }

    public void setConocPob(Conocimiento conocPob) {
        this.conocPob = conocPob;
    }
    
}
    
  