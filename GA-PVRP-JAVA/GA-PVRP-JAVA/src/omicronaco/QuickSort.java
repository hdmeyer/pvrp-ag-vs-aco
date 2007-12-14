package omicronaco;
import java.util.*;

///////////////////////////////////////////////////////////////////////////

/**
 * Quick sort implementation that will sort an array or Vector of IComparable objects.
 * @see org.relayirc.util.IComparable
 * @see java.util.Vector 
 */
public class QuickSort {
 
	//------------------------------------------------------------------
	public static void swap(Vector v, int i, int j) {
		Object tmp = v.elementAt(i);
		v.setElementAt(v.elementAt(j),i);
		v.setElementAt(tmp,j);
	}
	//------------------------------------------------------------------
        
	/*
	* quicksort a vector of objects.
	*
	* @param v     - a vector of objects
	* @param left  - the start index - from where to begin sorting
	* @param right - the last index.
        * @param tipo  - para saber si ordenamos vector de hormigas o de nodos
        * donde true indica hormigas, y false nodos.
	*/
//	public static void quicksort(Vector v, int left, int right, boolean ascending, boolean tipo) {
//
//		int i, last;
//
//		if (left >= right) { // do nothing if array size < 2
//			return;   
//		}
//		swap(v, left, (left+right) / 2);
//		last = left;
//		for (i = left+1; i <= right; i++) {
//                    if(tipo){
//                        Hormiga ic = (Hormiga)v.elementAt(i);
//			Hormiga icleft = (Hormiga)v.elementAt(left);
//                        if (ascending && ic.comparaHormigas(icleft) < 0 ) {
//				swap(v, ++last, i);
//			}
//			else if (!ascending && ic.comparaHormigas(icleft) > 0 ) {
//				swap(v, ++last, i);
//			}revisar el compare to
//                    }else{
//                        Nodo ic = (Nodo)v.elementAt(i);
//			Nodo icleft = (Nodo)v.elementAt(left);
//                        if (ascending && ic.comparaNodos(icleft) < 0 ) {
//				swap(v, ++last, i);
//			}
//			else if (!ascending && ic.comparaNodos(icleft) > 0 ) {
//				swap(v, ++last, i);
//			}
//                    }
//		}
//		swap(v, left, last);
//		quicksort(v, left, last-1,ascending,tipo);
//		quicksort(v, last+1, right,ascending,tipo);
//	}
        public static void quicksort(Vector v, int left, int right, boolean ascending, boolean tipo) {
            int i, j;
            if(tipo){
                double costo = ((Hormiga) v.elementAt((int) (Math.random() * v.size()))).getCostoTotal();
                i = left;
                j = right;
                do{
                    while (((Hormiga) v.elementAt(i)).getCostoTotal() > costo)
                        ++i;
                    while (((Hormiga) v.elementAt(j)).getCostoTotal() < costo)
                        --j;
                    
                    if (i <= j) {
                        swap(v, i, j);
                        ++i;
                        --j;
                    }
                } while (i <= j);
                if (left < j)
			quicksort (v, left, j,ascending,tipo);
			
		if (i < right)
			quicksort (v, i, right, ascending,tipo);
            }else{
                double probabilidad = ((Nodo) v.elementAt((int) (Math.random() * v.size()))).getProbabilidad();
                i = left;
                j = right;
                do{
                    while (((Nodo) v.elementAt(i)).getProbabilidad() > probabilidad)
                        ++i;
                    while (((Nodo) v.elementAt(j)).getProbabilidad() < probabilidad)
                        --j;
                    
                    if (i <= j) {
                        swap(v, i, j);
                        ++i;
                        --j;
                    }
                } while (i <= j);
                if (left < j)
			quicksort (v, left, j,ascending,tipo);
			
		if (i < right)
			quicksort (v, i, right, ascending,tipo);
            }
        }
        
}