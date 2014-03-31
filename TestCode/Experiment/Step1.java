package Experiment;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import SentenceExtraction.JWIStemmer;
import SentenceExtraction.SentenceExtract;
import SentenceExtraction.SentenceProcess;
import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.corpus.StopWords;

public class Step1 {

	public Set<String> ExcuteStep1() throws Exception {
		SentenceExtract extractor = new SentenceExtract();
		JWIStemmer jwiStemmer = new JWIStemmer();
		Map<String, Integer> map = extractor.extract("TestData/TestData4",
				1.0 / 4.0, 1.0 / 3.0);
		Set<String> wordSet = new TreeSet<String>();
		SentenceProcess sProcess = new SentenceProcess();
		for (String sentence : map.keySet()) {
			String[] words = sProcess
					.getMeaningfulWords(sentence.toLowerCase());
			for (int i = 0; i < words.length; i++) {
				String stem = jwiStemmer.FindFirstStem(words[i]);
				if (stem != null) {
					wordSet.add(stem);
				} else {
					wordSet.add(words[i]);
				}
			}
		}
		return wordSet;
	}

	public static void main(String[] args) throws Exception {
		Step1 step1 = new Step1();
		System.out.println(step1.ExcuteStep1());
	}
}
