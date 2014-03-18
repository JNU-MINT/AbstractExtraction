package TestCode;

import SentenceExtraction.JWSSimilar;
import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.corpus.StopWords;

/**
 * 测试JWSSimilar类
 * getSentenceSimilirity函数
 * @author qiusd
 *
 */
public class JWSSentenceTest {
    public static void main(String args[]) throws Exception{
        String sentence1="I will go to the mall and take goods back";
        String sentence2="I am going to shopping center to buy some things ";
        
        StopWords sw= new StopWords("models/stopwords");
		CWSTagger seg = new CWSTagger("models/seg.m");
		
        JWSSimilar sm= new JWSSimilar(seg, sw);
        System.out.println("The similar of 2 sentences is");
        System.out.println(sm.getSentenceSimilirity(sentence1, sentence2));
    }
}
