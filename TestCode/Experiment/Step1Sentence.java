package Experiment;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import processor.SentenceProcessor;

import SentenceExtraction.JWIStemmer;
import SentenceExtraction.SentenceExtractor;


/**
 * 算法步骤一
 * 基于句子的TextRank
 * @author qiusd
 */
public class Step1Sentence {

	public Set<String> ExcuteStep1(String TextPath) throws Exception {
		SentenceExtractor sentenceExtractor = new SentenceExtractor();
		JWIStemmer jwiStemmer = new JWIStemmer();
		Map<String, Integer> map = sentenceExtractor.extract(TextPath,
				1.0 / 4.0, 1.0 / 3.0);
		Set<String> wordSet = new TreeSet<String>();
		SentenceProcessor sProcess = new SentenceProcessor();
		for (String sentence : map.keySet()) {
			String[] words = sProcess
					.getMeaningfulWords(sentence.toLowerCase());
			for (int i = 0; i < words.length; i++) {
				String stem = jwiStemmer.getFirstStem(words[i]);
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
		Step1Sentence step1 = new Step1Sentence();
		System.out.println(step1.ExcuteStep1("TestData/TestData6"));
	}
}
