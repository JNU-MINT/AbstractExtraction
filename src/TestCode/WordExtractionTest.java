package TestCode;
import org.fnlp.app.keyword.AbstractExtractor;
import org.fnlp.app.keyword.WordExtract;

import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.corpus.StopWords;

/**
 * 关键词抽取使用示例
 * @author jyzhao,ltian
 *
 */
public class WordExtractionTest {
	
	public static void main(String[] args) throws Exception {
		
		
		StopWords sw= new StopWords("models/stopwords");
		CWSTagger seg = new CWSTagger("models/seg.m");
		
		String[][] strDoubleArray;
		strDoubleArray = seg.tag2DoubleArray("George took sticks of the same length and cut them randomly until all parts became at most 50 units long. Now he wants to return sticks to the original state, but he forgot how many sticks he had originally and how long they were originally. Please help him and design a program which computes the smallest possible original length of those sticks. All lengths expressed in units are integers greater than zero.");
		System.out.println(strDoubleArray.toString());
		
		AbstractExtractor key = new WordExtract(seg,sw);
		
		System.out.println(key.extract("George took sticks of the same length and cut them randomly until all parts became at most 50 units long. Now he wants to return sticks to the original state, but he forgot how many sticks he had originally and how long they were originally. Please help him and design a program which computes the smallest possible original length of those sticks. All lengths expressed in units are integers greater than zero.", 100, true));
		
		
		System.out.println(key.extract("甬温线特别重大铁路交通事故车辆经过近24小时的清理工作，26日深夜已经全部移出事故现场，之前埋下的D301次动车车头被挖出运走", 20, true));
		
		System.out.println(key.extract("甬温线特别重大铁路交通事故车辆经过近24小时的清理工作，26日", 20, true));
		
		//处理已经分好词的句子
		sw=null;
		key = new WordExtract(seg,sw);
		System.out.println(key.extract("甬温线 特别 重大 铁路交通事故车辆经过近24小时的清理工作，26日深夜已经全部移出事故现场，之前埋下的D301次动车车头被挖出运走", 20));
		System.out.println(key.extract("赵嘉亿 是 好人 还是 坏人", 10));
		System.out.println(key.extract("word1 word2 word3 word4 word5", 10));
		
		
		key = new WordExtract();
		System.out.println(key.extract("", 5));
		
		
		
	}
}
