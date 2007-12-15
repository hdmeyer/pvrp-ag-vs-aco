/*
 * Hormiga.java
 *
 * Created on 11 de diciembre de 2007, 9:50
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package omicronaco;

import java.util.Vector;

/**
 *
 * @author Propietario
 */
public class Hormiga {
    
    private int posicion;
    //CADA HORMIGA DEBE MANTENER SU SOLUCION.
    private Camino[][] caminos;
    
    private double CostoTotal;
    private int dias;
    private int cantVehiculos;
    
    //CADA HORMIGA DEBERIA SABER A QUIEN VISITO
    
    /** Creates a new instance of Hormiga */
    public Hormiga(Conocimiento entrada) {
                
        this.setPosicion(0);// EMPEZAMOS EN EL DEPOSITO SIEMPRE
        this.setCaminos(new Camino[entrada.dias][entrada.cantVehiculos]);
        for (int i = 0; i < caminos.length; i++) {
            for (int j = 0; j < caminos[i].length; j++) {
                this.getCaminos()[i][j] = new Camino(entrada);
            }
        }
        this.setCostoTotal(0);
        this.setDias(entrada.dias);
        this.setCantVehiculos(entrada.cantVehiculos);
    }
    
    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public Camino[][] getCaminos() {
        return caminos;
    }

    public void setCaminos(Camino[][] caminos) {
        this.caminos= caminos;
    }

    public double getCostoTotal() {
        return CostoTotal;
    }

    public void setCostoTotal(double CostoTotal) {
        this.CostoTotal = CostoTotal;
    }
    
    public int comparaHormigas(Hormiga h){
        
        if(this.getCostoTotal() > h.getCostoTotal()){
            return 1;
        }else if(this.getCostoTotal() < h.getCostoTotal()){
            return -1;
        }else{
            return 0;
        }
    }
    
    public void calcularCostoTotal(){
        for (int i = 0; i < caminos.length; i++) {
            
            for (int j = 0; j < caminos[i].length; j++) {
                if(!this.getCaminos()[i][j].getRuta().isEmpty()){
                    this.getCaminos()[i][j].calcularCosto();
                    
                    this.CostoTotal += this.getCaminos()[i][j].getCosto();
                }
            }
        }   
    }
    
    public String toString(){
        String retorno = "";
        for (int i = 0; i < this.getCantVehiculos(); i++) {
            
            for (int j = 0; j < this.getDias(); j++) {
                
                retorno += this.getCaminos()[j][i].toString()+"-";
            }
            retorno += "\n";
        }
        return retorno;
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public int getCantVehiculos() {
        return cantVehiculos;
    }

    public void setCantVehiculos(int cantVehiculos) {
        this.cantVehiculos = cantVehiculos;
    }
    
}
