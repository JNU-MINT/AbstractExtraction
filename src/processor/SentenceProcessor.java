package processor;

import java.util.ArrayList;
import java.util.List;

import SentenceExtraction.JWIStemmer;

import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.corpus.StopWords;


public class SentenceProcessor {
	
    private StopWords stopWords;
    private CWSTagger cWSTagger;
	
    public SentenceProcessor() throws Exception{
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
		for (int i = 0; i < meaningfulWords.size(); i++) {
			meaningfulWords.set(i, meaningfulWords.get(i).toLowerCase());
		}
		int length = meaningfulWords.size();
		String[] wordArray = meaningfulWords.toArray(new String[length]);
    	return wordArray;
	}
	
	public String[] getStemmedWord(String sentence) throws Exception {
		String[] meaningfulWords = getMeaningfulWords(sentence);
		ArrayList<String> stemmedWords = new ArrayList<String>();
		JWIStemmer jwiStemmer = new JWIStemmer();
		for (String word : meaningfulWords) {
			String stemmedWord = jwiStemmer.getFirstStem(word);
			if(stemmedWord == null || stemmedWord.isEmpty()) {
				stemmedWords.add(word);
			}
			else {
				stemmedWords.add(stemmedWord);
			}
			
		}
		return stemmedWords.toArray(new String[stemmedWords.size()]);
	}
	
	
	
	public static void main(String[] args) throws Exception {
		SentenceProcessor sentenceProcess = new SentenceProcessor();
		FileProcessor fileProcessor = new FileProcessor();
		String text = fileProcessor.readFiles("TestData/TestData7");
		String[] meaningfulWords = sentenceProcess.getStemmedWord(text);
		for (String word : meaningfulWords) {
			System.out.println(word);
		}
	}
}
