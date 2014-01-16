/**
 * 
 */
package link.analysisEngine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import link.dataModel.LexicalChain;
import link.dataModel.Mail;
import link.resource.CollocationNetworkModelInterface;
import link.resource.ThreadIndexModelInteface;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import com.auxilii.msgparser.Message;

import common.types.Token;
import common.util.MiscUtil;
import factory.parser.MBoxParser;

/**
 * Annotator that parse the content of a JCas assuming it is an MBox message
 */
public class MBoxMessageConsumerAE extends linkJCasAnnotator {

	public final static String RES_KEY = "aKey";
	@ExternalResource(key = RES_KEY)
	private CollocationNetworkModelInterface collocationNetwork;

	public static final String PARAM_OUTPUT_FILE = "output_file";
	@ConfigurationParameter(name = PARAM_OUTPUT_FILE, mandatory = false, defaultValue = "tmp/reply-list.csv")
	private String output_file;

	public static final String PARAM_THRESHOLD = "threshold";
	@ConfigurationParameter(name = PARAM_THRESHOLD, mandatory = false, defaultValue = "3")
	private Integer threshold;
	
	public static StringBuilder sb = new StringBuilder();
	public static Integer counter = 1;

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		Mail mail = Mail.jCasMails.get(aJCas);

		ArrayList<Token> tokens = new ArrayList<Token>(JCasUtil.select(aJCas, Token.class));
		
		HashSet<String> lc = new HashSet<String>();
		
		for (int i = 0; i < (tokens.size() - 1) ; i++) {
			if (tokens.get(i).getBegin() < aJCas.getDocumentText().indexOf(mail.getMessage().getBodyText())) continue;
					
			String word = tokens.get(i).getCoveredText().toLowerCase();
			String next = tokens.get(i + 1).getCoveredText().toLowerCase();
			
			if (collocationNetwork.check(word, next, threshold)) {
				if (lc.isEmpty()) lc.add(word);
				
				lc.add(next);
			} else {
				if (lc.size() > 1) {
					// System.out.println(lc.toString());
					mail.getDescription().add(new LexicalChain(lc));
				}
				
				lc = new HashSet<String>();
			}
		}
		
		Double max = 0.0;
		Mail replyTo = null;
		
		for (Mail m : Mail.mails.get(mail.getThread())) {
			if (m.getMessage().getDate().before(mail.getMessage().getDate())) {
				Double sim = mail.compare(m);
				
				if (sim > max) {
					max = sim;
					replyTo = m;
				}
			}
		}
		
		if (replyTo != null) {
			sb
			.append(replyTo.getMessage().getMessageId())
			.append(':')
			.append(mail.getMessage().getMessageId())
			.append('\n');
		}
		
		// System.out.printf("%s\t%s\n", new Date(), counter);
		// counter++;
	}
	
	@Override
	public void collectionProcessComplete() throws AnalysisEngineProcessException {
		System.out.printf("%s - writing results to %s...\n", getClass().getName(), output_file);
		
		MiscUtil.writeToFS(sb.toString(), output_file);
		
		super.collectionProcessComplete();
	}
}
