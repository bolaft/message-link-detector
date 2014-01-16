package link.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

/**
 * Pairs message ids and generated thread ids
 */
public class ThreadIndexModel implements ThreadIndexModelInteface, SharedResourceObject {
	
	protected Map<String, Integer> index = new HashMap<String, Integer>();
	
	@Override
	public void load(DataResource aData) throws ResourceInitializationException {
		String errorMsg = "%s - map could not be loaded\n";
		String successMsg = "%s - map loaded\n";
		
		System.out.printf("%s - loading map from %s...\n", getClass().getName(), aData.getUrl().getFile());
		
		InputStream inStr = null;
		
		try {
			// open input stream to data
			inStr = aData.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inStr));
			
			// read each line
			String line;
			Integer threadId = 1;
			
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				
				if (line.startsWith("#")) {
					threadId++;
				} else {
					index.put(line, threadId);
					// System.out.println(threadId +"\t"+line);
				}
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

	/**
	 * Get indexMap
	 */
	public Map<String, Integer> getIndex() {
		return index;
	}
	
}
