/**
 * 
 */
package link.analysisEngine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import link.resource.StopWordModelInterface;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.jcas.JCas;

import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.RecipientEntry;

import common.types.Token;

/**
 * Annotator that parse the content of a JCas assuming it is an MBox message
 */
public class MBoxMessageParserAE extends linkJCasAnnotator {
	
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

	private static String messageSummary(Message message) {
		String summary;
		
		summary = "Subject " + message.getSubject() + "\n";
		summary += "Date " + message.getDate() + "\n";
		summary += "MessageId " + message.getMessageId() + "\n";
		summary += "From name " + message.getFromName() + " email "+message.getFromEmail() + "\n";
		summary += "To name " + message.getToName() + " email "+message.getToEmail() + "\n";
		summary += "Recipients ";
		
		for (RecipientEntry r : message.getRecipients()) {
			summary += r.getToName()+ " " + r.getToEmail() + ", ";
		}
		
		summary += "\n";
		summary += "DisplayTo " + message.getDisplayTo() + "\n";
		summary += "DisplayCc " + message.getDisplayCc() + "\n";
		summary += "DisplayBcc " + message.getDisplayBcc() + "\n";
		summary += "Body " + message.getBodyText() + "\n";

		for (String p : message.getProperties()) {
			summary += "Property ";
			summary += p + ":" + message.getProperty(p) + "\n";
		}
		
		summary += "\n";
		
		return summary;
	}

	private static String lightMessageSummary(Message msg) {
		String summary;
		
		summary = "Subject " + msg.getSubject() + "\n";
		summary += "MessageId " + msg.getMessageId() +"\n";
		summary += "From name " + msg.getFromName() + " email " + msg.getFromEmail() + "\n";
		summary += "To name " + msg.getToName()+ " email " + msg.getToEmail() + "\n";

		for (String p : msg.getProperties()) {
			summary += "Property ";
			summary += p + ":"+msg.getProperty(p) + "\n";
		}
		
		summary += "\n";
		
		return summary;
	}
}
