/**
 * 
 */
package link.analysisEngine;

import link.dataModel.Mail;
import link.resource.ThreadIndexModelInteface;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.jcas.JCas;

import com.auxilii.msgparser.Message;

import factory.parser.MBoxParser;

/**
 * Annotator that parse the content of a JCas assuming it is an MBox message
 */
public class MBoxMessageParserAE extends AbstractAE {
	
	public final static String RES_KEY = "aKey";
	@ExternalResource(key = RES_KEY)
	private ThreadIndexModelInteface threadIndex;

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		MBoxParser parser = new MBoxParser();
		Message message = null;
		
		try {
			message = parser.parse(aJCas.getDocumentText());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		String mid = message.getMessageId();
		
		if (mid instanceof String == false || mid.length() <= 2) return;
			
		message.setMessageId(mid.substring(1, mid.length() - 1));
		
		Integer thread = threadIndex.getIndex().get(message.getMessageId());
		
		if (thread == null) return;
		
		Mail.jCasMails.put(aJCas, new Mail(message, thread));
	}
}
