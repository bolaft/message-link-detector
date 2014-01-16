package link.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.SharedResourceObject;

import common.util.MiscUtil;

public class CollocationNetworkModel implements CollocationNetworkModelInterface, SharedResourceObject {
	
	protected Map<String, Map<String, Integer>> collocationMap = new HashMap<String, Map<String, Integer>>();

	/**
	 * Increments the collocation value of a pair of words 
	 */
	public void increment(String head, String word, boolean lookBack) {
		// System.out.printf("+1 \"%s\", \"%s\"\n", head, word);
		
		if (collocationMap.containsKey(head)){
			Map<String, Integer> wordMap = collocationMap.get(head);
			
			if (wordMap.containsKey(word)){
				wordMap.put(word, wordMap.get(word) + 1);
			} else {
				wordMap.put(word, 1);
			}
		} else {
			Map<String, Integer> initMap = new HashMap<String, Integer>();
			initMap.put(word, 1);
			collocationMap.put(head, initMap);
		}
		
		if (lookBack) increment(word, head, false);
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
	 * Saves the collocation network to file
	 */
	public synchronized void save(String filename, Integer minCol, Integer minSize) {
		System.out.printf("%s - saving map to %s...\n", getClass().getName(), filename);
		
		StringBuilder sb = new StringBuilder();
		
		for (String head : collocationMap.keySet()) {
			for (String word : collocationMap.get(head).keySet()) {
				int val = collocationMap.get(head).get(word);
				if (val >= minCol 
						&& head.length() >= minSize
						&& word.length() >= minSize) {
					sb.append(head).append('\t').append(word).append('\t').append(collocationMap.get(head).get(word)).append('\n');
				}
			}
		}
		
		MiscUtil.writeToFS(sb.toString(), filename);
		
		System.out.printf("%s - map successfuly saved to file\n", getClass().getName());
	}

	/**
	 * Attempts to load the collocation map from file
	 */
	public void load(DataResource aData) {
		String errorMsg = "%s - map could not be loaded\n";
		String successMsg = "%s - map loaded\n";
		
		System.out.printf("%s - loading map from %s...\n", getClass().getName(), aData.getUrl().getFile());
		
		InputStream inStr = null;
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(aData.getInputStream()));
			String line;
			
			while ((line = br.readLine()) != null) {
                String[] columns = line.split("\t");
                
                if (columns.length == 3){
                	if (!collocationMap.containsKey(columns[0])){
                    	Map<String, Integer> pair = new HashMap<String, Integer>();
                        collocationMap.put(columns[0], pair);
                	}
                	
                	collocationMap.get(columns[0]).put(columns[1], Integer.valueOf(columns[2]));
                }
			}
			
			System.out.printf(successMsg, getClass().getName());
		} catch (IOException e) {
			System.out.printf(errorMsg, getClass().getName());
		} catch (NullPointerException e) {
			System.out.printf(errorMsg, getClass().getName());
		} finally {
			if (inStr != null) {
				try {
					inStr.close();
				} catch (IOException e) {}
			}
		}
	}

	@Override
	public boolean check(String word, Set<String> set, Integer threshold) {
		int sum = 0;
		
		for (String head : set) {
			if (collocationMap.containsKey(head) && collocationMap.get(head).containsKey(word)) {
				sum += collocationMap.get(head).get(word);
			}
		}
		
		if (sum / set.size() >= threshold) return true;
		
		return false;
	}

	@Override
	public boolean check(String word1, String word2, Integer min) {
		if (collocationMap.containsKey(word1) 
				&& collocationMap.get(word1).containsKey(word2) 
				&& collocationMap.get(word1).get(word2) >= min) return true;
		
		return false;
	}
	
}
