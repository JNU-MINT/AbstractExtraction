package SentenceExtraction;

import java.util.List;

import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.corpus.StopWords;


public class SentenceProcess {
	
    private StopWords stopWords;
    private CWSTagger cWSTagger;
	
    public SentenceProcess() throws Exception{
    	stopWords= new StopWords("models/stopwords");
    	cWSTagger = new CWSTagger("models/seg.m");
	}
    
	/**
	 * 对句子分词并取有意义的词
	 * 返回词语数组
	 * @throws Exception
	 */
	public String[] getMeaningfulWords(String sentence) throws Exception{
		String[] words = cWSTagger.tag2Array(sentence);
		List<String> meaningfulWords = stopWords.phraseDel(words);
		int length = meaningfulWords.size();
		String[] wordArray = meaningfulWords.toArray(new String[length]);
    	return wordArray;
	}
	
	

}
