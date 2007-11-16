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
/**
 *
 * @author Huguis
 */
public class Lector {
    int tipoVRP;
    int cantVehiculos;
    int cantClientes;
    int dias;
    int duracion;
    int capacidad;
    double inicial [][];
    double matrizCostos [][];
    
    /** Creates a new instance of Lector */
    public Lector() {
    }
    public void lector(String archivo) throws IOException  { 

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
            /*Inicializamos el vector donde cargaremos los datos del archivo por 
             *cada uno de los clientes*/
            this.inicial = new double[this.cantClientes+1][6];
            for (int i = 0; i <= this.cantClientes; i++) {
                linea = sarchivo.readLine();
                linea_dividida = linea.split("\\s");
                for (int j = 0; j < 6; j++) {
                    this.inicial[i][j] = Double.parseDouble(linea_dividida[j]);
                }
            }
            this.matrizCostos = new double[this.cantClientes+1][this.cantClientes+1];
            for (int i = 0; i < this.cantClientes+1; i++) {
                for (int j = 0; j < this.cantClientes+1; j++) {
                    this.matrizCostos[i][j] = Math.sqrt(Math.pow(this.inicial[i][1] - this.inicial[j][1],2) + Math.pow(this.inicial[i][2] - this.inicial[j][2],2));
                }
                
            }
        } catch (IOException exc) { 

	    System.err.println(exc);
	    System.out.println("Asegurese de haber proporcionado " +
                               " la extension del archivo (\".java\")");

	} finally { 

	    System.out.println("");
	    System.out.println("Es la mejor manera de aprender Streams, practicando!");
	} 
	    
    }
}
