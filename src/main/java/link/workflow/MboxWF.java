/**
 * 
 */
package link.workflow;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import link.analysisEngine.MBoxMessageParserAE;
import link.collectionReader.MboxReaderCR;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.pipeline.SimplePipeline;

/**
 * Illustrate how to configure 
 * and run annotators with the shared model object.
 */
public class MboxWF {

	public static void main(String[] args) throws Exception {
		AnalysisEngineDescription aed4 = createEngineDescription(MBoxMessageParserAE.class,
				MBoxMessageParserAE.PARAM_DEST_DIR, "data/ubuntu-fr/email.message");

	
		CollectionReaderDescription crd = createReaderDescription(MboxReaderCR.class,
				MboxReaderCR.PARAM_MBOX_SRCPATH, "data/ubuntu-fr/email.mbox/ubuntu-fr.mbox", 
				//MboxReaderCR.PARAM_MBOX_SRCPATH, "/tmp/athread", 
				MboxReaderCR.PARAM_LANGUAGE, "fr",
				MboxReaderCR.PARAM_ENCODING, "iso-8859-1");


		// Run the pipeline
		SimplePipeline.runPipeline(crd,aed4); //aaed
	}


}
