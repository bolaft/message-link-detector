/**
 * 
 */
package link.analysisEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import link.dataModel.LexicalChain;
import link.dataModel.Mail;
import link.resource.CollocationNetworkModelInterface;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import common.types.Token;
import factory.parser.MBoxParser;

/**
 * Annotator that parse the content of a JCas assuming it is an MBox message
 */
public class MBoxMessageConsumerAE extends linkJCasAnnotator {

	public final static String RES_KEY = "aKey";
	@ExternalResource(key = RES_KEY)
	private CollocationNetworkModelInterface collocationNetwork;

	public static final String PARAM_THRESHOLD = "threshold";
	@ConfigurationParameter(name = PARAM_THRESHOLD, mandatory = false, defaultValue = "3")
	private Integer threshold;

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		MBoxParser parser = new MBoxParser();
		Mail mail = null;
		
		try {
			mail = new Mail(parser.parse(aJCas.getDocumentText()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<Token> tokens = new ArrayList<Token>(JCasUtil.select(aJCas, Token.class));
		
		HashSet<String> lc = new HashSet<String>();
		
		for (int i = 0; i < (tokens.size() - 1) ; i++) {
			String word = tokens.get(i).getCoveredText().toLowerCase();
			
			if (collocationNetwork.check(word, tokens.get(i + 1).getCoveredText().toLowerCase(), threshold)) {
				if (lc.isEmpty()) lc.add(word);
				
				lc.add(tokens.get(i + 1).getCoveredText().toLowerCase());
			} else {
				if (!lc.isEmpty()) {
					System.out.println(lc.toString());
					mail.getDescription().add(new LexicalChain(lc));
				}
				
				lc = new HashSet<String>();
			}
		}
		
		System.out.println("### EOF ###");
		
//		ArrayList<String> words = new ArrayList<String>();
//		
//		for (Token token : tokens) {
//			String word = token.getCoveredText().toLowerCase();
//			
//			if (!words.contains(word) && word.length() >= minSize) {
//				words.add(word);
//			} else {
//				continue;
//			}
//			
//			boolean added = false;
//
//			for (LexicalChain lexicalChain : mail.getDescription()) {
//				if (collocationNetwork.check(word, lexicalChain.getLexicalChain(), threshold)) {
//					lexicalChain.addItem(word);
//					added = true;
//				}
//			}
//			
//			if (!added){
//				HashSet<String> hs = new HashSet<String>();
//				hs.add(word);
//				mail.getDescription().add(new LexicalChain(hs));
//			}
//		}
	}
	
}
