package Experiment;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import processor.FileProcessor;
import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.corpus.StopWords;
import model.Document;

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
		Map<String, Integer> wordScoreMap = wordExtract.extract(doc.abstractString, 5);
		for (String word : wordScoreMap.keySet()) {
			doc.queryWordSet.add(word);
		}
		return doc;
	}

	public static void main(String[] args) throws Exception {
		
		DocHandler docHandler = new DocHandler();
		Document doc = docHandler.getDoc("F:\\patent(F)\\uspatent2014\\smallxml\\US-08621662-B2.xml");
		Step1Word step1 = new Step1Word();
		step1.ExcuteStep1(doc);
	}
}
