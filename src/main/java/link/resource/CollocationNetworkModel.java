package link.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

import common.util.MiscUtil;

public class CollocationNetworkModel implements CollocationNetworkModelInterface, SharedResourceObject {
	private Map<String, Map<String, Double>> collocationNetwork = new HashMap<String, Map<String, Double>>();

	public void addPair(String word, String colWord, boolean lookBack) {
		if (collocationNetwork.containsKey(word)){
			Map<String, Double> wordMap = collocationNetwork.get(word);
			
			if (wordMap.containsKey(colWord)){
				wordMap.put(colWord, wordMap.get(colWord) + 1);
			} else {
				wordMap.put(colWord, 1.0);
			}
		} else {
			Map<String, Double> initMap = new HashMap<String, Double>();
			initMap.put(colWord, 1.0);
			collocationNetwork.put(word, initMap);
		}
		
		if (lookBack) {
			addPair(colWord, word, false);
		}
	}
	
	public void display(){
		for (Entry<String, Map<String, Double>> entry : collocationNetwork.entrySet()){
		    for (Entry<String, Double> subEntry : entry.getValue().entrySet()){
			    System.out.println(entry.getKey() + " " + subEntry.getKey() + ": " + subEntry.getValue());
		    }
		}
	}
	
	public synchronized void save(String filename) {
		String csv = "";

		for (Entry<String, Map<String, Double>> entry : collocationNetwork.entrySet()){
		    for (Entry<String, Double> subEntry : entry.getValue().entrySet()){
			    csv += entry.getKey() + "\t" + subEntry.getKey() + "\t" + subEntry.getValue() + "\n";
		    }
		}
		
		MiscUtil.writeToFS(csv, filename);
	}

	public void load(DataResource aData) throws ResourceInitializationException {
		InputStream inStr = null;
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(aData.getInputStream()));
			String line;
			
			while ((line = br.readLine()) != null) {
                String[] columns = line.split("\t");
                
                if (columns.length == 3){
                	if (!collocationNetwork.containsKey(columns[0])){
                    	Map<String, Double> pair = new HashMap<String, Double>();
                        collocationNetwork.put(columns[0], pair);
                	}
                	
                	collocationNetwork.get(columns[0]).put(columns[1], Double.valueOf(columns[2]));
                	
                }
			}
		} catch (IOException e) {
			throw new ResourceInitializationException(e);
		} finally {
			if (inStr != null) {
				try {
					inStr.close();
				} catch (IOException e) {}
			}
		}
	}
	
}
