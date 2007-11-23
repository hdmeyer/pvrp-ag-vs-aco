/*
 * ListaEnlada.java
 *
 * Created on 16 de noviembre de 2007, 8:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gapvrpjava;

/**
 *
 * @author Huguis
 */
/*
 * ListaEnlazada.java
 *
 * Creado el 9 de Junio de 2007, 13:37
 */

public class ListaEnlazada {
    Cromosoma header;
    /**
     * Crea una nueva instancia de ListaEnlazada de asignaturas
     * inicializando el header
     */
    public ListaEnlazada(Conocimiento entrada) {
        header = new Cromosoma(entrada);
    }
    /**
     * Dice si la lista esta vacia o no
     * @return -> True si esta vacia
     * -> False si no esta vacia
     */
    public boolean Vacio( )
    {
       return header.siguiente == null;
    }
    public Asignatura insertar(String a, int hi, int hf, String dia, int ht, int porc, int hl, Asignatura p )
        {
            p.siguiente = new Asignatura( a, hi, hf, dia, ht, porc, hl, null);
            return p.siguiente;
            
        }

    /**
     * Busca una materia en la lista de acuerdo a su nombre
     * @param materia -> Nombre de la materia que estamos buscando
     * @return -> El puntero donde se encuentra la materia
     */
    public Asignatura buscar( String materia )
        {
/* 1*/      Asignatura itr = header.siguiente;

/* 2*/      while( itr != null && !itr.asignatura.equals( materia ) )
/* 3*/          itr = itr.siguiente;

/* 4*/      return  itr;
        }
    /**
     * Borra una materia de la lista
     * @param materia -> nombre de la materia a borrar
     */
     public void remove( String materia )
        {
            Asignatura p = buscar( materia );

            if( p.siguiente != null )
                p.siguiente = p.siguiente.siguiente;  
        }
    /**
     * Imprime la lista enlazada
     */
     public void imprimirLista() {
         if( this.Vacio( ) )
             System.out.print( "Empty list" );
         else {
             Asignatura itr = this.header.siguiente;
             System.out.println("Asignatura" + "\t\t" + "Hora inicio" + "\t" + "Hora fin"
                 + "\t" + "Dia" + "\t\t" + "Horas Totales" + "\t" + "Porc. Lab Requerido" + "\t" +
                 "Horas Laboratorio Usadas" + "\t" + "Porc Lab utilizado");
             while( itr != null){
                 System.out.println(itr.toString() + "\n");
                 itr = itr.siguiente;
             }
         }
         
     }
}
    
}
