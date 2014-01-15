package link.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.SharedResourceObject;

import common.util.MiscUtil;

public class CollocationNetworkModel implements CollocationNetworkModelInterface, SharedResourceObject {
	protected Map<String, Map<String, Integer>> collocationMap = new HashMap<String, Map<String, Integer>>();
	protected Boolean loaded = false;

	/**
	 * Increments the collocation value of a pair of words 
	 */
	public void increment(String word, String colWord, boolean lookBack) {
		if (collocationMap.containsKey(word)){
			Map<String, Integer> wordMap = collocationMap.get(word);
			
			if (wordMap.containsKey(colWord)){
				wordMap.put(colWord, wordMap.get(colWord) + 1);
			} else {
				wordMap.put(colWord, 1);
			}
		} else {
			Map<String, Integer> initMap = new HashMap<String, Integer>();
			initMap.put(colWord, 1);
			collocationMap.put(word, initMap);
		}
		
		if (lookBack) {
			increment(colWord, word, false);
		}
	}
	
	/**
	 * Displays the collocation network in the console
	 */
	public void display(){
		for (Entry<String, Map<String, Integer>> entry : collocationMap.entrySet()){
		    for (Entry<String, Integer> subEntry : entry.getValue().entrySet()){
			    System.out.println(entry.getKey() + " " + subEntry.getKey() + ": " + subEntry.getValue());
		    }
		}
	}
	
	/**
	 * If the collocation map was not loaded, saves it to file
	 */
	public synchronized void save(String filename, Integer min) {
		if (!loaded) {
			String csv = "";

			for (Entry<String, Map<String, Integer>> entry : collocationMap.entrySet()){
			    for (Entry<String, Integer> subEntry : entry.getValue().entrySet()){
			    	if (subEntry.getValue() >= min) csv += entry.getKey() + "\t" + subEntry.getKey() + "\t" + subEntry.getValue() + "\n";
			    }
			}
			
			MiscUtil.writeToFS(csv, filename);
		}
	}

	/**
	 * Attempts to load the collocation map from file
	 */
	public void load(DataResource aData) {
		InputStream inStr = null;
		String errorMsg = "CollocationNetworkModel - map could not be loaded from file ; will proceed with building from scratch.";
		String successMsg = "CollocationNetworkModel - map successfuly loaded from file";
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(aData.getInputStream()));
			String line;
			
			while ((line = br.readLine()) != null) {
                String[] columns = line.split("\t");
                
                if (columns.length == 3){
                	loaded = true;
                	
                	if (!collocationMap.containsKey(columns[0])){
                    	Map<String, Integer> pair = new HashMap<String, Integer>();
                        collocationMap.put(columns[0], pair);
                	}
                	
                	collocationMap.get(columns[0]).put(columns[1], Integer.valueOf(columns[2]));
                }
			}
			
			System.out.println(successMsg);
		} catch (IOException e) {
			System.out.println(errorMsg);
		} catch (NullPointerException e) {
			System.out.println(errorMsg);
		} finally {
			if (inStr != null) {
				try {
					inStr.close();
				} catch (IOException e) {}
			}
		}
	}

	/**
	 * Checks if the collocation map was loaded
	 */
	public boolean isLoaded() {
		return loaded;
	}
	
}
