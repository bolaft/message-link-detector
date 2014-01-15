package link.dataModel;

import java.util.ArrayList;

import com.auxilii.msgparser.Message;

/**
 * @author hernandez
 */
public class Mail {
	
	ArrayList<LexicalChain> description = new ArrayList<LexicalChain>();
	Message message;
	
	public Mail(Message message) {
		this.message = message;
	}

	public ArrayList<LexicalChain> getDescription() {
		return description;
	}
	
}
