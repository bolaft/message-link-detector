/**
 * 
 */
package link.workflow;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;

import java.io.File;

import link.analysisEngine.MBoxMessageConsumerAE;
import link.analysisEngine.MBoxMessageParserAE;
import link.collectionReader.MboxReaderCR;
import link.resource.CollocationNetworkModel;
import link.resource.StopWordModel;
import link.resource.ThreadIndexModel;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ExternalResourceDescription;

/**
 * Illustrate how to configure and run annotators with the shared model object.
 */
public class MboxWF {

	public static void main(String[] args) throws Exception {
		System.out.printf("%s - started...\n", MboxWF.class.getName());
		
		ExternalResourceDescription stopWordsResourceDesc = createExternalResourceDescription(
			StopWordModel.class, 
			new File("data/stopwords-fr.txt")
		);
		
		ExternalResourceDescription collocationNetworkResourceDesc = createExternalResourceDescription(
			CollocationNetworkModel.class,
			new File("tmp/collocation-network.csv")
		);
		
		ExternalResourceDescription threadIndexResourceDesc = createExternalResourceDescription(
			ThreadIndexModel.class,
			new File("data/thread-messageId.digest")
		);
		
		AnalysisEngineDescription mBoxMessageParserAED = createEngineDescription(
			MBoxMessageParserAE.class,
			MBoxMessageParserAE.RES_KEY, stopWordsResourceDesc
		);
		
		AnalysisEngineDescription mBoxMessageConsumerAED = createEngineDescription(
			MBoxMessageConsumerAE.class,
			MBoxMessageConsumerAE.COL_RES_KEY, collocationNetworkResourceDesc,
			MBoxMessageConsumerAE.THR_RES_KEY, threadIndexResourceDesc,
			MBoxMessageConsumerAE.PARAM_OUTPUT_FILE, "tmp/results.digest"
		);

		CollectionReaderDescription crd = createReaderDescription(
			MboxReaderCR.class,
			MboxReaderCR.PARAM_MBOX_SRCPATH, "data/ubuntu-fr/email.mbox/ubuntu-fr.mbox",
			MboxReaderCR.PARAM_LANGUAGE, "fr",
			MboxReaderCR.PARAM_ENCODING, "iso-8859-1"
		);
		
		// Check the external resource was injected
		AnalysisEngineDescription aed = createEngineDescription(mBoxMessageParserAED, mBoxMessageConsumerAED);

		// Run the pipeline
		SimplePipeline.runPipeline(crd, aed);
		
		System.out.printf("%s - done\n", MboxWF.class.getName());
	}

}
