/*
 * IComparable.java
 *
 * Created on 13 de diciembre de 2007, 10:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package omicronaco;

/** 
 * Objects that implement this interface are sortable by QuickSort.
 * @see org.relayirc.util.QuickSort
 */
public interface IComparable {
	/**
	 *	Compare to other object. Works like String.compareTo()
	 */
	public int compareTo(IComparable c);
        
       
}
