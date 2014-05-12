package Experiment;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import processor.FileProcessor;
import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.corpus.StopWords;
import model.Document;
import model.Word;

import org.fnlp.app.keyword.WordExtract;


/**
 * 算法步骤一
 * 基于词的TextRank
 * @author qiusd
 */
public class Step1Word {

	public Document ExcuteStep1(Document doc) throws Exception {
		StopWords sw= new StopWords("models/stopwords");
		CWSTagger seg = new CWSTagger("models/seg.m");
		WordExtract wordExtract = new WordExtract(seg,sw);
		StringBuilder sBuilder = new StringBuilder();
		for (Word word : doc.abstractWords) {
			sBuilder.append(word.text + " ");
		}
		Map<String, Integer> wordScoreMap = wordExtract.extract(sBuilder.toString(), 5);
		//TODO:这个功能应该放在更合适的位置
		for (Word word : doc.abstractWords) {
			if (wordScoreMap.keySet().contains(word.text)) {
				int rank = 1;
				for (String scoreWord : wordScoreMap.keySet()) {
					if (scoreWord.equals(word.text)) {
						word.textRankValue = wordScoreMap.get(scoreWord);
						word.textRank = rank;
						break;
					}
					else {
						rank++;
					}
				}
			}
		}
		
		for (String word : wordScoreMap.keySet()) {
			doc.queryWordSet.add(word);
		}
		return doc;
	}

	public static void main(String[] args) throws Exception {
		
		DocHandler docHandler = new DocHandler();
		Document doc = docHandler.getDoc("F:\\patent(F)\\uspatent2014\\140107\\xml\\US-08623430-B2.xml");
		Step1Word step1 = new Step1Word();
		step1.ExcuteStep1(doc);
	}
}
