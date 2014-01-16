package link.dataModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import com.auxilii.msgparser.Message;

public class Mail {
	public static HashMap<Integer, HashSet<Mail>> mails = new HashMap<Integer, HashSet<Mail>>();
	
	protected ArrayList<LexicalChain> description = new ArrayList<LexicalChain>();
	protected Integer thread;
	protected Message message;
	
	/**
	 * Constructor
	 */
	public Mail(Message message, Integer thread) {
		this.message = message;
		this.thread = thread;
		
		if (!mails.containsKey(thread)) mails.put(thread, new HashSet<Mail>());
		
		mails.get(thread).add(this);
	}
	
	/**
	 * Compares the mail to an older one, and updates replyTo and maxReplyToLikelihood values
	 */
	public Double compare(Mail other) {		
		Double sum = 0.0;
		
		for (LexicalChain thisLC : other.getDescription()) {
			Double max = 0.0;
			
			for (LexicalChain otherLC : description) {
				Double sim = thisLC.compare(otherLC);
				
				if (sim > max) max = sim;
			}
			
			sum += max;
		}
		
		return sum / (other.getDescription().size() * description.size());
	}
	
	/**
	 * Checks if the mail is first in its thread
	 */
	public boolean isFirst() {
		Date date = this.message.getDate();
		
		for (Mail mail : mails.get(thread)) {
			if (mail.getMessage().getDate().before(date)) return false;
		}
		
		return true;
	}

	/**
	 * Get message
	 */
	public Message getMessage() {
		return message;
	}

	/**
	 * Set message
	 */
	public void setMessage(Message message) {
		this.message = message;
	}

	/**
	 * Get description
	 */
	public ArrayList<LexicalChain> getDescription() {
		return description;
	}

	public Integer getThread() {
		return thread;
	}
}
