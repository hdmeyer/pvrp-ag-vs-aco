/*
 * Lector.java
 *
 * Created on 31 de octubre de 2007, 19:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gapvrpjava;
import java.io.*;
import java.util.StringTokenizer;
import java.math.*;
import java.util.Vector;
/**
 *
 * @author Huguis
 */
public class Conocimiento {
    public int tipoVRP;
    public int cantVehiculos;
    public int cantClientes;
    public int dias;
    public int duracion;
    public int capacidad;
    public double clientes [][];
    public double matrizCostos [][]; // contiene información del costo de ir
                                     // de un nodo a otro. Consideramos un
                                     // grafo conexo complto.
    int listaVisitas [][];
    /*ATRIBUTOS PARA LAS SOLUCIONES LEIDAS*/
    public double mejorSol;
    public Vector<Double>solucion [];
    
    /** Creates a new instance of Lector */
    public Conocimiento() {
    }
    
    public void CargarConocimiento(String archivo) throws IOException  { 

	String linea;
        String[] linea_dividida = null;

	try { 
	    BufferedReader sarchivo = new BufferedReader(new FileReader(archivo));
            linea = sarchivo.readLine();
            StringTokenizer st = new StringTokenizer(linea);
            this.tipoVRP = Integer.parseInt(st.nextToken());
            this.cantVehiculos = Integer.parseInt(st.nextToken());
            this.cantClientes = Integer.parseInt(st.nextToken());
            this.dias = Integer.parseInt(st.nextToken());
            
            for (int i = 0; i < this.dias; i++) {
                linea = sarchivo.readLine();
                linea_dividida = linea.split("\\s");
            }
            this.capacidad = Integer.parseInt(linea_dividida[1]);
            /*inicializamos el vector donde cargaremos los datos del archivo por 
             *cada uno de los clientes*/
            this.clientes = new double[this.cantClientes+1][6];
            for (int i = 0; i <= this.cantClientes; i++) {
                linea = sarchivo.readLine();
                //linea_dividida = linea.split("\\s");
                /*for (int j = 0; j < linea_dividida.length; j++) {
                    this.clientes[i][j] = Double.parseDouble(linea_dividida[j]);
                }*/
                StringTokenizer partes = new StringTokenizer(linea," ");
                for (int j = 0; j < 6; j++) {
                    String token = partes.nextToken();
                    this.clientes[i][j] = Double.parseDouble(token);
                }

            }
            this.listaVisitas = new int[2][this.cantClientes+1];
            this.InicializarListaVisitas();
            this.matrizCostos = new double[this.cantClientes+1][this.cantClientes+1];
            for (int i = 0; i < this.cantClientes+1; i++) {
                
                /*Ak aprovechamos y cargamos una lista de visitas y necesidades de cada cliente*/
                this.listaVisitas[1][i] = (int)this.clientes[i][5];
                for (int j = 0; j < this.cantClientes+1; j++) {
                    this.matrizCostos[i][j] = Math.sqrt(Math.pow(this.clientes[i][1] - this.clientes[j][1],2) + Math.pow(this.clientes[i][2] - this.clientes[j][2],2));
                }
                
            }
        } catch (IOException exc) { 

	    System.err.println(exc);
	    System.out.println("Asegurese de haber proporcionado " +
                               " la extension del archivo (\".java\")");

	} finally { 

	    System.out.println("");
	    System.out.println("practicando!");
	} 	    
    }
    /** INICIALIZ LA LISTA DE VISITAS POSIBLES QUE ES UN VECTOR DE 2 DIMENSIONES
     * DONDE LA PRIMERA DIMENSION INDICA SI SE VISITO O NO A UN CLIENTE EN UN DIA
     * ESPECIFICO, Y LA SEGUNDA DIMENSION LA CANTIDAD DE VISITAS NECESARIAS DE 
     * CADA CLIENTE...
     */
    public void InicializarListaVisitas(){
        for (int i = 0; i < clientes.length; i++) {
            this.listaVisitas[0][i] = 0;
        }
    }
    /**Este metodo formatea totalmente la lista de visitas
     * reinicializando la frecuencia de visitas necesarias de cada cliente.
     */
    public void FormatearListaVisitas(){
        for (int i = 0; i < clientes.length; i++) {
            this.listaVisitas[0][i] = 0;
            this.listaVisitas[1][i] = (int)this.clientes[i][5];
        }

    }
    @Override
    public String toString(){
        String salida ="";
        salida += "CANTIDAD DE VEHICULOS ----------->"+this.cantVehiculos+"\n";
        salida += "CANTIDAD DE CLIENTES ----------->"+this.cantClientes+"\n";
        salida += "CANTIDAD DE DIAS ----------->"+this.dias+"\n";
        
        return salida;
    }
    public String toStringMatrizCostos(){
        String salida = "";
        for (int j = 0; j < this.cantClientes + 1; j++) {
                salida += "\t" + j;
        }
        salida+="\n";
        
        for (int i = 0; i < this.cantClientes + 1; i++) {
            salida += "\t"+i;
            for (int j = 0; j < this.cantClientes; j++) {
                salida += "\t"+ Math.round(this.matrizCostos[i][j]);
            }
            salida += "\n";
        }
        return salida;
    }
    
    /* ToDo List
       
       - Retornar directamente distancia de dos clientes
       - Revissar la idea de la matriz clientes
       - Agregar listado de clientes con sus respectivas propiedades
     
     */
    public void LeerSolucion(String archivo) throws IOException{
        String linea;
        int contador =0;
        this.solucion = new Vector[this.dias*this.cantVehiculos];
        for (int i = 0; i < this.solucion.length; i++) {
            this.solucion[i] = new Vector<Double>();
        }

        try {
            BufferedReader sarchivo = new BufferedReader(new FileReader(archivo));
            linea = sarchivo.readLine();
            
            this.mejorSol = Double.parseDouble(linea);
            linea=sarchivo.readLine();
            while (linea!=null) { 
                StringTokenizer partes = new StringTokenizer(linea," ");
                while (partes.hasMoreTokens()) {
                    double part = Double.parseDouble(partes.nextToken());
                    this.solucion[contador].add(part);
                }
                linea=sarchivo.readLine();
                contador++;
            }


        } catch (Exception exception) {
            System.out.println("Se pudrio todo viteh...");
        }
    }
    public String toStringMejorSolucion(){
        String salida = "";
        salida+= this.mejorSol + "\n";
        
        for (int i = 0; i < this.solucion.length; i++) {
            salida += "\t";
            if(!this.solucion[i].isEmpty()){
                for(int j=0; j < this.solucion[i].size();j++){
                    salida += "\t"+ this.solucion[i].get(j);
                }
                salida += "\n";
            }
        }
        return salida;
    }
}
