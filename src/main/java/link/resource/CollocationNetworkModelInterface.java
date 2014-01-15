package link.resource;

import java.util.Set;

public interface CollocationNetworkModelInterface {

	/**
	 * Increments the collocation value of a pair of words 
	 */
	public void increment(String head, String word, boolean lookBack);
	
	/**
	 * Displays the collocation network in the console
	 */
	public void display();
	
	/**
	 * If the collocation map was not loaded, saves it to file
	 */
	public void save(String filename, Integer minCol, Integer minSize);

	/**
	 * Checks if the average similarity of a word to a set is above a threshold
	 */
	public boolean check(String word, Set<String> set, Integer threshold);

	/**
	 * Checks if two words are collocated
	 */
	boolean check(String word1, String word2, Integer min);
	
}