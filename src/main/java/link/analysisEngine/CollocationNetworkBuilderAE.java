package link.analysisEngine;

import java.util.ArrayList;

import link.resource.CollocationNetworkModelInterface;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import common.types.Token;

/**
 * Annotator that builds the collocation network
 */
public class CollocationNetworkBuilderAE extends JCasAnnotator_ImplBase {

	public final static String RES_KEY = "aKey";
	@ExternalResource(key = RES_KEY)
	private CollocationNetworkModelInterface collocationNetwork;
	
	public static final String PARAM_RESOURCE_DEST_FILE = "resourceDestFilename";
	@ConfigurationParameter(name = PARAM_RESOURCE_DEST_FILE, mandatory = false, defaultValue="tmp/collocation-network.csv")
	private String resourceDestFilename;
	
	public static final String PARAM_WINDOW_SIZE = "windowSize";
	@ConfigurationParameter(name = PARAM_WINDOW_SIZE, mandatory = false, defaultValue="3")
	private Integer windowSize;

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		if (!collocationNetwork.isLoaded()) {
			ArrayList<Token> tokens = new ArrayList<Token>(JCasUtil.select(aJCas, Token.class));
			
			for (int i = 0; i < tokens.size(); i++){
				String head = tokens.get(i).getCoveredText().toLowerCase();
				
				System.out.println("head: " + head);
				
				for (int j = 1; j < windowSize && i+j < tokens.size(); j++) {
					String token = tokens.get(i+j).getCoveredText().toLowerCase();
					
					System.out.println("	token: " + token);
					
					collocationNetwork.increment(head, token, true);
				}
			}
		}
	}
	
	@Override
	public void collectionProcessComplete() throws AnalysisEngineProcessException {
		System.out.println("CollocationNetworkBuilderAE - display()");
		collocationNetwork.display(); 
		System.out.println("CollocationNetworkBuilderAE - save()");
		collocationNetwork.save(resourceDestFilename, 1);
	}
	
}
