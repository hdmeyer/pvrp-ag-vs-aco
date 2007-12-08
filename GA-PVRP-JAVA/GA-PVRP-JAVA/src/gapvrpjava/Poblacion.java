/**
 * Población.java
 *
 * Created on 16 de noviembre de 2007, 8:37
 *
 * Contiene las funcionalidades principales del Algoritmos genético para resolver
 * PVRP
 */

package gapvrpjava;

import java.util.*;

/**
 *
 * @author Cristhian Parra
 * @author Hugo Meyer
 *
 */
public class Poblacion {
    
    /** 
     * Vector que contiene a todos los individuos de la población
     */
    private Cromosoma[] individuos; 
    
    /**
     * Hijos de los individuos selectos y variable para almacenar cantidad 
     * de hijos generados
     */
    private Cromosoma[] hijos;        
    private int CantHijosGenerados;
    
    /**
     * Tamaño de la Población, equivalente a individuos.length
     */
    private int tamanho; 

    /**
     * Fitness de los individuos 
     * También disponible como información de cada individuo
     */
    private double[] fitness;
    
    /** 
     * Número de la generación actual. 
     */
    private int generacion;

    /**
     * Contiene todas las operaciones utilizadas para el algoritmo genético
     */
    private FuncionesGA operacionesGeneticas;

    /**
     * Mejor cromosoma de toda la historia
     */
    private Cromosoma mejorIndividuo = null;
    
    /**
     * Posición en el vector de población del mejor individuo. 
     */
    private int mejorIndividuoPos;
    
    private Conocimiento conocimiento;
    
    private int probMutacion;
    
    private int iterador = 0;
    
    /** 
     * 
     * Constructor vacío de la población que genera una nueva instancia del 
     * Array de Cromosomas y setea un tamaño inicial de la población a 30
     * 
     */    
    public Poblacion() {
        this.tamanho = 30; 
        this.individuos = new Cromosoma[this.tamanho];
        this.hijos = new Cromosoma[this.tamanho];
        this.probMutacion = 20; 
    }
    
    /**
     * Constructor de la población a partir del Conocimiento que tenemos del
     * Problema.
     * 
     * @param entrada 
     * Clase que contiene la matriz del grafo y los parámetros globales del 
     * problema
     *                
     */    
    public Poblacion(Conocimiento entrada, int tamanhoPop) {
        
        this.conocimiento = entrada;
        this.tamanho = tamanhoPop;     
        this.individuos = new Cromosoma[tamanhoPop];
        this.hijos = new Cromosoma[tamanhoPop];
        this.fitness = new double[tamanhoPop];
        this.probMutacion = 20; 
        // Leer de la entrada el conocimiento e inicializar la población a 
        // partir de ello
        this.inicializarPop();
    }
    
    
    
    public Poblacion(Conocimiento entrada, int tamanhoPop, int probMutacion) {
        
        this.conocimiento = entrada;
        this.tamanho = tamanhoPop;
        this.individuos = new Cromosoma[tamanhoPop];
        this.hijos = new Cromosoma[tamanhoPop];
        this.fitness = new double[tamanhoPop];
        
        if (probMutacion > 100 || probMutacion < 0) {
            this.probMutacion = 20; 
        } else {
            this.probMutacion = probMutacion; 
        }
        
        // Leer de la entrada el conocimiento e inicializar la población a 
        // partir de ello
        this.inicializarPop();
    }

    /** 
     * Inicializa la población generando un Cromosoma y construyéndolo con 
     * valores aleatorios.
     * 
     * No hace nada más que inicializar población. No se hace ninguna evaluación
     * de la población en su conjunto.
     * 
     * @param entrada
     * @return
     */
    public void inicializarPop() {
       
        for (int i=0; i < individuos.length; i++) {
                individuos[i] = new Cromosoma(this.getConocimiento());
                individuos[i].construirCromosoma(this.getConocimiento());
        }
    }
    
    /**
     * Método para Optimizar el Algoritmo. Elimina los duplicados. 
     * PENDIENTE
     * =======================================================================
     * 
     * Compara cada individuo de la población con
     * los demás y modifica los cromosomas duplicados
     * mutandolos.
     */
    
    /**
     * 
     */
    /*
    public void descartarIguales() {
        //System.out.println("DESCARTARIGUALES...");

        for (int i=0; i<this.getTamanho()-1; i++) {
                for (int j=i+1; j<this.getTamanho(); j++) {
                        if (individuos[i].equals(individuos[j])) {
                                operacionesGeneticas.mutar(individuos[j]);
                        }
                }
        }
    }*/

    /**
     * Selecciona nuevos individuos de esta población y realiza el 
     * @return Cromosoma[] nuevos individuos seleccionados
     */
    public Cromosoma[] seleccion() {
            return FuncionesGA.seleccionar(this);
    }

    
   /**
    * Cruza los individuos seleccionados y genera la lista de hijos que 
    * son utilizados después para reemplazar parte de la población
    * @param selectos
    */
    public void cruce(Cromosoma[] selectos) {
        
        Cromosoma nuevos[];
        for (int i=0; i <= selectos.length-2; i = i+2){
            
            nuevos = FuncionesGA.Cruzar(selectos[i], selectos[i+1],this.getConocimiento());

            this.getHijos()[i] = nuevos[0];
            this.getHijos()[i+1] = nuevos[1];
           
        }
        
        // si la longitud es impar, cruzamos el último con el primero
        int indiceUltimo = selectos.length - 1;
        
        if ((indiceUltimo+1)%2 != 0) {
            nuevos = FuncionesGA.Cruzar(selectos[indiceUltimo], 
                                        selectos[0],this.getConocimiento());
            this.getHijos()[indiceUltimo] = nuevos[0];
        }
    }

    /**
     * Muta cromosomas de la población con una 
     * problabilidad de mutar de 20%
     */
    public void mutar() {
        Random rand = new Random();
        rand.nextInt();

        for (int i=0; i < this.getTamanho(); i++){
            if (rand.nextInt(99) < this.probMutacion)
                FuncionesGA.Mutar(getHijos()[i],this.conocimiento);
        }
    }

    public void inicializarIterador() {
        iterador = 0;
    }
    
    public Cromosoma getNextIndividuo() {
        if (iterador < this.tamanho) {
            return individuos[iterador++];
        } else {
            iterador = 0;
            return null;
        }
        
    }
    /**
     * Realiza el reemplazo de individuos de
     * la población.
     */
    public void reemplazar() {
        
        // Revisar esta estrategia reemplazo
        // AHORA REEMPLAZAMOS TODO
        for (int i = 0; i<this.getTamanho(); i++)
            
            individuos[i] = getHijos()[i];
            //individuos[0]=this.getMejorIndividuo(); // Reemplaza el mejor
    }

    /**
     * Realiza el calculo de fitness para todos los individuos
     * 
     * INCOMPLETO: Descomentar la evaluación
     */
    public void evaluar() {

            for (int i=0; i<this.getTamanho();i++) {
                getFitness()[i] = individuos[i].evaluar(this.getConocimiento());
            }
            elegirMejor();
    }

    /**
     * Obtiene el fitness de un individuo
     * @param ind indice de un individuo
     * @return fitness
     */
    public double getFitness(int ind) {
        return getFitness()[ind];
    }

    public Cromosoma getIndividuo(int pos) {
        return individuos[pos];
    }

    /**
     * Elige el mejor cromosoma de 
     * toda la historia.
     */
    private void elegirMejor() {
            /*
             * Si todavia no se seleccionó
             * a ninguno, guardamos al primero.
             */
            if (mejorIndividuo == null) {
                    mejorIndividuo = individuos[0];
                    this.setMejorIndividuoPos(0);
            }

            double mejorFitness = 0;// mejorIndividuo.getFitness();
            for (int i=0; i < this.getTamanho(); i++) {
                    if (getFitness()[i]> mejorFitness) {
                            mejorIndividuo = individuos[i];
                            mejorFitness = mejorIndividuo.getFitness();
                            this.setMejorIndividuoPos(i);
                    }
            }
    }

    /**
     * 
     * Realiza el control de la población, y si la cantidad
     * de cromosomas inválidos es mayor al factor, retorna
     * true y en caso contrario, retorna false.
     * @param factor valor entre 0 y 1 que indica el porcentaje permitido.
     * @return true si la cantidad de invalidos supera el factor.
     */
   public boolean reinicializar(double factor){
            int contador = 0; // cuenta los cromosomas invalidos

            for (int i=1; i < this.getTamanho(); i++) {
                    // Contamos si el fitness es inválido
                    if (getFitness()[i] < 0)
                            contador++;
            }

            /*
             * Si el porcentaje de inválidos calculado es mayor 
             * al permitido, retornamos true
             */
            if (contador > this.getTamanho()*factor)
                    return true;

            // Si no, retornamos false
            return false;
    }

   /**
    * Setters y Getters
    * @return
    */
   
    public Cromosoma getMejorIndividuo() {
            return this.mejorIndividuo;
    }

    public void setMejorIndividuo(Cromosoma x) {
            this.mejorIndividuo = x; 
    }

    public double getMejorFitness(){
        return this.mejorIndividuo.getFitness();
    }

    public Cromosoma[] getIndividuos() {
        return individuos;
    }

    public void setIndividuos(Cromosoma[] individuos) {
        this.individuos = individuos;
    }

    /**
     * Solo el getter de tamanaho está implementado, porque esta propiedad solo
     * se establece una vez, al inicializar la población
     * 
     * @return El tamanho (cantidad de individuos) de la población
     */
    public int getTamanho() {
        return tamanho;
    }    

    
    /**
     * Función qeu convierte la población en un string concatenado de todos los
     * individuos.
     * 
     */
    @Override
    public String toString() {
        
        String PopString = individuos[0].toString();
        
        for (int i=1; i<this.getTamanho(); i++){
            String currentIndividual = individuos[i].toString();
            PopString +="$"+currentIndividual;
        }        
        
        return PopString+"$"+this.getMejorIndividuoPos()+","+this.getMejorFitness(); 
    }
    
    public String toStringMultilinea(String toStringLinea) {
        
        StringTokenizer tk = new StringTokenizer(toStringLinea, "$");
        
        String poblacionMultilinea = "Individuo 1: "+tk.nextToken()+"\n";
        poblacionMultilinea +="-----> Fitness = "+individuos[0].getFitness()+"\n";
        int i = 1;
        while (tk.hasMoreTokens()) {
            String current_token = tk.nextToken();
            
            // si se cumple esta condición, todavía no estamos en el último token
            if (tk.hasMoreTokens()) {
                double currentFitness = individuos[i].getFitness();
            
                String current ="Individuo "+(++i)+": "+ current_token+"\n";
                poblacionMultilinea += current;
                poblacionMultilinea +="-----> Fitness = "+currentFitness+"\n";
            } else {
                poblacionMultilinea +="*** Mejor Individuo, Fitness *** "+current_token+"\n";
            }
        }
           
        return poblacionMultilinea;
    }
    
    public String toStringImprimible(){
        String poblacionMultilinea ="<----------------------------------------------------------------------------------------------->\n";
        poblacionMultilinea += "<--------------------------- | Población - Inicio | ------------------------->\n"; 
        
        poblacionMultilinea +=toStringMultilinea(this.toString());
        
        poblacionMultilinea +="<---------------------------- | Población - Fin | ----------------------------->\n"; 
        poblacionMultilinea +="<--------------------------------------------------------------------------------------------------->\n\n";
        
        return poblacionMultilinea;
    }

    public String toStringPorCromosoma(String toStringLinea) {
        
        StringTokenizer tk = new StringTokenizer(toStringLinea, "$");
        
        String individuoMultiLinea = this.individuos[0].ImprimirCromo(tk.nextToken());
        
        String poblacionMultilinea ="<--------------------------------------------------------------------------------------------------->\n";
        poblacionMultilinea += "<--------------------------- | Población - Inicio | --------------------------->\n"; 
        
        poblacionMultilinea += "Individuo 1: "+individuoMultiLinea+"\n";
        poblacionMultilinea +="-----> Fitness = "+individuos[0].getFitness()+"\n";
        int i = 0;
        while (tk.hasMoreTokens()) {
            String current_token = tk.nextToken();
            
            // no estamos todavía en el último...
            if (tk.hasMoreTokens()) {
                double currentFitness = individuos[i].getFitness();

                individuoMultiLinea = this.individuos[++i].ImprimirCromo(current_token);
                String current ="Individuo "+(i+1)+": "+ individuoMultiLinea+"\n";
                poblacionMultilinea += current;
                poblacionMultilinea +="-----> Fitness = "+currentFitness+"\n"; 
            } else {
                poblacionMultilinea +="*** Mejor Individuo, Fitness *** "+current_token+"\n";                
            }
        }
        
        poblacionMultilinea +="<---------------------------- | Población - Fin | ----------------------------->\n"; 
        poblacionMultilinea +="<---------------------------------------------------------------------------------->\n\n";
        
        return poblacionMultilinea;
    }

    /**
     * Imprime en salida standard toda la población
     */
    public void imprimir(){
        for (int i=0; i<this.getTamanho(); i++){
            System.out.println("Cromosoma: "+i+" ");
            System.out.println("Fitness  : "+getFitness()[i]);
            String LineaCromosoma = individuos[i].toString();            
            String CromosomaMultilinea = individuos[i].ImprimirCromo(LineaCromosoma);
            System.out.print(CromosomaMultilinea);
            System.out.println();
        }
    }

    public Cromosoma[] getHijos() {
        return hijos;
    }

    public void setHijos(Cromosoma[] hijos) {
        this.hijos = hijos;
    }

    public double[] getFitness() {
        return fitness;
    }

    public void setFitness(double[] fitness) {
        this.fitness = fitness;
    }

    public int getMejorIndividuoPos() {
        return mejorIndividuoPos;
    }

    public void setMejorIndividuoPos(int mejorIndividuoPos) {
        this.mejorIndividuoPos = mejorIndividuoPos;
    }

    public Conocimiento getConocimiento() {
        return conocimiento;
    }

    public int getCantHijosGenerados() {
        return CantHijosGenerados;
    }

    public void setCantHijosGenerados(int CantHijosGenerados) {
        this.CantHijosGenerados = CantHijosGenerados;
    }

    public int getGeneracion() {
        return generacion;
    }

    public void setGeneracion(int generacion) {
        this.generacion = generacion;
    }
}
