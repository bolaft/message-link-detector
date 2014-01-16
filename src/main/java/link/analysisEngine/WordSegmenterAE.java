/**
 * 
 */
package link.analysisEngine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import link.resource.StopWordModelInterface;
import link.resource.ThreadIndexModelInteface;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.jcas.JCas;

import common.types.Token;

/**
 * Annotator that segments the text into words and filter the one present 
 * in a stop word set
 */
public class WordSegmenterAE extends linkJCasAnnotator {	
	public final static String RES_KEY = "aKey";
	@ExternalResource(key = RES_KEY)
	private StopWordModelInterface stopWords;
	
	final static String WORD_SEPARATOR_PATTERN = "[^\\s\\p{Punct}\\d]+"; //"[^\\s\\.:,'\\(\\)!]+";
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		Pattern wordSeparatorPattern = Pattern.compile(WORD_SEPARATOR_PATTERN);
		Matcher matcher = wordSeparatorPattern.matcher(aJCas.getDocumentText());
			
		while (matcher.find()) {
			String group = matcher.group().toLowerCase();
				
			if (!stopWords.contains(group)) {
				new Token(aJCas, matcher.start(), matcher.end()).addToIndexes(); 
			}
		}
	}
	
}
