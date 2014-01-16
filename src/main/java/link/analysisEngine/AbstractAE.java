package link.analysisEngine;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.resource.ResourceInitializationException;

public abstract class AbstractAE extends JCasAnnotator_ImplBase {
	
	@Override
	public void collectionProcessComplete() throws AnalysisEngineProcessException {
		System.out.printf("%s - collection process complete\n", getClass().getName());
	}

	@Override
	public void initialize(final UimaContext context) throws ResourceInitializationException {
		System.out.printf("%s - started (this can take a few minutes)...\n", getClass().getName());
		
		super.initialize(context);
	}
}
