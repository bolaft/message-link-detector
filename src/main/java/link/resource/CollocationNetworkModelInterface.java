package link.resource;

public interface CollocationNetworkModelInterface {	
	public void addPair(String word, String colWord, boolean lookBack);
	public void display();
	public void save(String filename);
}