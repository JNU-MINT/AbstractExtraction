package Experiment;

import java.util.ArrayList;
import java.util.Map;

import SentenceExtraction.JWSSimilar;
import SentenceExtraction.TFIDF;
import model.Document;
import model.Patent;
import model.Word;

public class FeatureComputer {
	
	JWSSimilar jwsSimilar;
	
	public FeatureComputer() throws Exception {
		jwsSimilar = new JWSSimilar();
	}
	
	private Document computeLinkFeature(Document doc) throws Exception {
		for (Word abstractWord : doc.abstractWords) {
			String abstractWordText = abstractWord.text;
			double formerValue = 0.0;
			for (Patent patent : doc.relatedPatents) {
				double simSum = 0.0;
				for (ArrayList<String> titleWords : patent.linkTitleSet) {
					for (String titleWord : titleWords) {
						if (titleWord != null && !titleWord.isEmpty()) {
							simSum += jwsSimilar.getWordSimilarity(abstractWordText, titleWord);
						}
					}
				}
				formerValue += patent.relatedScore * simSum;
			}
			double latterValue = 0.0;
			for (Patent patent : doc.relatedPatents) {
				latterValue += patent.relatedScore * patent.linkTitleSet.size();
			}
			abstractWord.linkFeature = formerValue / latterValue;
//			if (abstractWord.linkFeature == 0) {
//				throw new Exception();
//			}
		}
		return doc;
	}
	
	
	private Document computeTfidf(Document doc) throws Exception {
		StringBuilder sBuilder = new StringBuilder();
		for (Word word : doc.abstractWords) {
			sBuilder.append(word.text + " ");
		}
		TFIDF tfidf = new TFIDF();
		Map<String, Double> tfidfMap = tfidf.calTfidf(sBuilder.toString());
		for (Word word : doc.abstractWords) {
			try {
				word.tfidf = tfidfMap.get(word.text);
			} catch (Exception e) {
				word.tfidf = 0.0;
			}
		}
		return doc;
	}
	
	private Document computePosition(Document doc) {
		int wordNum = doc.abstractWords.size();
		int cnt = 0;
		for (Word word : doc.abstractWords) {
			word.position = ((double)cnt)/((double)wordNum);
			cnt++;
		}
		return doc;
	}
	
	private Document computeWordLength(Document doc) {
		int wordNum = doc.abstractWords.size();
		for (Word word1 : doc.abstractWords) {
			int cnt = 0;
			for (Word word2 : doc.abstractWords) {
				if(word1.text.length() < word2.text.length()) {
					cnt++;
				}
			}
			word1.wordLength = ((double)cnt)/((double)wordNum);
		}
		return doc;
	}
	
	private Document decideLabel(Document doc) {
		
		for (Word abstractWord : doc.abstractWords) {
			for (String titleWord : doc.titleWords) {
				if(abstractWord.text.equals(titleWord)) {
					abstractWord.lable = 1;
				}
			}
		}
		return doc;
	}
	
	public Document compute(Document doc) throws Exception {
		doc = computeLinkFeature(doc);
		doc = computeTfidf(doc);
		doc = computePosition(doc);
		doc = computeWordLength(doc);
		doc = decideLabel(doc);
		return doc;
	}
	
}
