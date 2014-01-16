/**
 * 
 */
package link.workflow;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;
import link.analysisEngine.CollocationNetworkBuilderAE;
import link.analysisEngine.WordSegmenterAE;
import link.collectionReader.ZimReaderCR;
import link.resource.CollocationNetworkModel;
import link.resource.StopwordListModel;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ExternalResourceDescription;

/**
 * Illustrate how to configure 
 * and run annotators with the shared model object.
 */
public class ZimAnalyserWF {

	public static void main(String[] args) throws Exception {
		System.out.printf("%s - started...\n", ZimAnalyserWF.class.getName());
		
		// Creation of the external resource description
		ExternalResourceDescription stopWordsResourceDesc = createExternalResourceDescription(
			StopwordListModel.class, 
			"file:data/stopwords-fr.txt"
		);
	
		ExternalResourceDescription collocationNetworkResourceDesc = createExternalResourceDescription(
			CollocationNetworkModel.class,
			"file:data/collocation-network.csv"
		);
		
		// Binding external resource to each Annotator individually
		AnalysisEngineDescription wordSegmenterAED = createEngineDescription(
			WordSegmenterAE.class, 
			WordSegmenterAE.RES_KEY, stopWordsResourceDesc
		);

		AnalysisEngineDescription collocationNetworkBuilderAED = createEngineDescription(
			CollocationNetworkBuilderAE.class, 
			CollocationNetworkBuilderAE.RES_KEY, collocationNetworkResourceDesc, 
			CollocationNetworkBuilderAE.PARAM_RESOURCE_DEST_FILE, "tmp/collocation-network.csv"
		);
		
		// Check the external resource was injected
		AnalysisEngineDescription aed = createEngineDescription(wordSegmenterAED, collocationNetworkBuilderAED);
		
		// Creation of the collection reader description 
		// ZimReaderCR read 7864 articles of ubuntudoc_fr_01_2009.zim but only 4124 html with 4076 non null
		CollectionReaderDescription crd = createReaderDescription(
			ZimReaderCR.class,
			ZimReaderCR.PARAM_ZIM_SRCPATH, "data/ubuntu-fr/doc.zim/ubuntudoc_fr_01_2009.zim", 
			ZimReaderCR.PARAM_LANGUAGE, "fr",
			ZimReaderCR.PARAM_ENCODING, "utf-8"
		);
		
		// Run the pipeline
		SimplePipeline.runPipeline(crd, aed);
		System.out.printf("%s - done\n", ZimAnalyserWF.class.getName());
	}
	
}
