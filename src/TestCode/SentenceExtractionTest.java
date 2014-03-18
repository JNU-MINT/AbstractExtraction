package TestCode;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

import org.fnlp.app.keyword.AbstractExtractor;

import SentenceExtraction.SentenceExtract;

import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.corpus.StopWords;

/**
 * 测试重要句子的抽取
 * @author qiusd
 *
 */
public class SentenceExtractionTest {
	public static void main(String[] args) throws Exception {
		StopWords sw= new StopWords("models/stopwords");
		CWSTagger seg = new CWSTagger("models/seg.m");
		
		AbstractExtractor extractor = new SentenceExtract(seg,sw);
		/*System.out.println(extractor.extract("George took sticks of the same length and cut them randomly until all parts became at most 50 units long. Now he wants to return sticks to the original state, but he forgot how many sticks he had originally and how long they were originally. Please help him and design a program which computes the smallest possible original length of those sticks. All lengths expressed in units are integers greater than zero.", 100, true));
		System.out.println(extractor.extract("car.bus.desk.pen.book.shoe.", 100));
		System.out.println(extractor.extract("shoe.car.bus.desk.pen.book.", 100));
		*/
		
//		FileInputStream fi=new FileInputStream("TestData/SentenceDetectionTest");
//		InputStreamReader ir = new InputStreamReader(fi,"GBK");
//		BufferedReader br = new BufferedReader(ir);
//		StringBuffer sb = new StringBuffer("");
//		
//		String str;
//		while((str = br.readLine()) != null)
//		{
//			sb.append(str);
//		}
		
		Map<String, Integer> map = extractor.extract("TestData/SentenceDetectionTest4", 100);
		Iterator iter = map.entrySet().iterator(); 
		System.out.println("Result:");
		while (iter.hasNext()) { 
		    Map.Entry entry = (Map.Entry) iter.next(); 
		    Object key = entry.getKey(); 
		    Object val = entry.getValue(); 
		    System.out.println(key);
		    System.out.println("value = " + val);
		}
	}
}
