package link.resource;

public interface CollocationNetworkModelInterface {	

	/**
	 * Increments the collocation value of a pair of words 
	 */
	public void increment(String word, String colWord, boolean lookBack);
	
	/**
	 * Displays the collocation network in the console
	 */
	public void display();
	
	/**
	 * If the collocation map was not loaded, saves it to file
	 */
	public void save(String filename, Integer min);

	/**
	 * Checks if the collocation map was loaded
	 */
	public boolean isLoaded();
}