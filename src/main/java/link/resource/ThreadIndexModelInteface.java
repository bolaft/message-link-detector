package link.resource;

import java.util.Map;

/**
 * Pairs message ids and generated thread ids
 */
public interface ThreadIndexModelInteface {

	/**
	 * Get index
	 */
	public Map<String, Integer> getIndex();
}
