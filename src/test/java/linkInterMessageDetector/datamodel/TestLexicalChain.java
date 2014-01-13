package linkInterMessageDetector.datamodel;

import link.dataModel.LexicalChain;

import org.junit.Test;

public class TestLexicalChain {

	@Test
	public void test() {
		LexicalChain lc1 = new LexicalChain();
		lc1.addItem("père");
		lc1.addItem("noël");
		lc1.addItem("santa");
		lc1.addItem("sapin");
		lc1.addItem("neige");
		lc1.addItem("vinasse");
		
		LexicalChain lc2 = new LexicalChain();
		lc2.addItem("père");
		lc2.addItem("fouettard");
		lc2.addItem("martinet");
		lc2.addItem("enfant");
		lc2.addItem("vinasse");
		
		System.out.printf("Comparing %s with %s gives %f which stands for %s \n",lc1, lc2, lc1.compare(lc2), lc1.isSimilar(lc2) ? "similar" : "notSimilar");
		
		
	}

}
