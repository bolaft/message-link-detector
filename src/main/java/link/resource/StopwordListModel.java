package link.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.SharedResourceObject;

/**
 * Implementation of the model of stop word list
 */
public final class StopwordListModel implements StopwordListModelInterface, SharedResourceObject {
	private Set<String> stopWordSet;

	private synchronized Set<String> getStopWords () {
		return stopWordSet;
	}
	
	private synchronized void add (String key) {
		getStopWords().add(key);
	}
	
	public Boolean contains(String key) {
		return getStopWords().contains(key);
	}
	
	public synchronized void load(DataResource aData) {
		String errorMsg = "%s - set could not be loaded\n";
		String successMsg = "%s - set loaded\n";
		
		System.out.printf("%s - loading set from %s...\n", getClass().getName(), aData.getUrl().getFile());
		
		if (stopWordSet == null) {		
			stopWordSet = new HashSet<String>();
			InputStream inStr = null;
			
			try {
				// open input stream to data
				inStr = aData.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inStr));
				
				// read each line
				String line;
				
				while ((line = reader.readLine()) != null) {
					if (! line.startsWith("#"))
					add(line.trim());
				}
				
				System.out.printf(successMsg, getClass().getName());
			} catch (Exception e) {
				System.out.printf(errorMsg, getClass().getName());
			} finally {
				if (inStr != null) {
					try {
						inStr.close();
					} catch (IOException e) { }
				}
			}
		}
	}
	
}