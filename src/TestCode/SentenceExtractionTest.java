package TestCode;

import java.util.Iterator;
import java.util.Map;

import org.fnlp.app.keyword.AbstractExtractor;

import SentenceExtraction.SentenceExtract;

import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.corpus.StopWords;

/**
 * 测试重要句子的抽取
 * @author qiusd
 */
public class SentenceExtractionTest {
	public static void main(String[] args) throws Exception {
		
		CWSTagger seg = new CWSTagger("models/seg.m");
		StopWords sw= new StopWords("models/stopwords");
		SentenceExtract extractor = new SentenceExtract(seg,sw);
		
		Map<String, Integer> map = extractor.extract("TestData/TestData3", 1.0/4.0, 1.0/3.0);
		
		Iterator iter = map.entrySet().iterator(); 
		System.out.println("Result:");
		int i = 1;
		while (iter.hasNext()) { 
		    Map.Entry entry = (Map.Entry) iter.next(); 
		    Object key = entry.getKey(); 
		    Object val = entry.getValue(); 
		    System.out.println(i++ + ":" + key.toString());
		    System.out.println("value = " + val);
		}
	}
}
