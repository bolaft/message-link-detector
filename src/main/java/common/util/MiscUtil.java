package common.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.uima.jcas.tcas.Annotation;

public class MiscUtil  {
	
	/**
	 * Echo in the standard output (the console) the type 
	 * and the covered text of the given annotation
	 * @param anAnnotation
	 */
	public static void echo(Annotation anAnnotation) {
		System.out.printf(
			"type>%s<\t\tcoveredText>%s<\n", 
			anAnnotation.getClass().getSimpleName(), 
			anAnnotation.getCoveredText()
		);
	}
	
	public static void echo(String string) {
		System.out.println(string);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * TreeMap sorted by value
	 * http://stackoverflow.com/questions/2864840/treemap-sort-by-value
	 * @param map
	 * @return sortedEntries
	 */
	static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
		SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
			new Comparator<Map.Entry<K,V>>() {
				public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
					int res = e1.getValue().compareTo(e2.getValue());
					return res != 0 ? res : 1; // Special fix to preserve items with equal values
				}
			}
		);
		
		sortedEntries.addAll(map.entrySet());
		
		return sortedEntries;
	}
	
	/**
	 * Prints a string to the file system
	 * @param text
	 * @param filename
	 */
	public static void writeToFS (String text, String filename) {
		PrintWriter out = null;
		
		try {
			out = new PrintWriter(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		out.println(text);
		out.close();
	}
	
}