package link.analysisEngine;

import java.util.ArrayList;

import link.resource.CollocationNetworkModelInterface;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import common.types.Token;

/**
 * Annotator that builds the collocation network
 */
public class CollocationNetworkBuilderAE extends linkJCasAnnotator {

	public final static String RES_KEY = "aKey";
	@ExternalResource(key = RES_KEY)
	private CollocationNetworkModelInterface collocationNetwork;
	
	public static final String PARAM_RESOURCE_DEST_FILE = "resourceDestFilename";
	@ConfigurationParameter(name = PARAM_RESOURCE_DEST_FILE, mandatory = false, defaultValue="tmp/collocation-network.csv")
	private String resourceDestFilename;
	
	public static final String PARAM_WINDOW_SIZE = "windowSize";
	@ConfigurationParameter(name = PARAM_WINDOW_SIZE, mandatory = false, defaultValue="3")
	private Integer windowSize;

	public static final String PARAM_MIN_COLLOCATION = "minCollocation";
	@ConfigurationParameter(name = PARAM_MIN_COLLOCATION, mandatory = false, defaultValue = "1")
	private Integer minCollocation;

	public static final String PARAM_MIN_SIZE = "minSize";
	@ConfigurationParameter(name = PARAM_MIN_SIZE, mandatory = false, defaultValue = "2")
	private Integer minSize;
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		ArrayList<Token> tokens = new ArrayList<Token>(JCasUtil.select(aJCas, Token.class));
		
		for (int i = 0; i < tokens.size(); i++){
			String head = tokens.get(i).getCoveredText().toLowerCase();
			
			for (int j = 1; j < windowSize && i+j < tokens.size(); j++) {
				String token = tokens.get(i+j).getCoveredText().toLowerCase();
				
				collocationNetwork.increment(head, token, true);
			}
		}
	}
	
	@Override
	public void collectionProcessComplete() throws AnalysisEngineProcessException {
		// collocationNetwork.display(); 
		collocationNetwork.save(resourceDestFilename, minCollocation, minSize);
		
		super.collectionProcessComplete();
	}
	
}
