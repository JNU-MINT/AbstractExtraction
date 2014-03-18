package SentenceExtraction;
import java.util.List;
import java.util.ArrayList;
import java.lang.String;

import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.corpus.StopWords;
import edu.sussex.nlp.jws.JWS;
import edu.sussex.nlp.jws.Lin;


/**
 * 利用Java WordNet计算 
 * @author qiusd
 *
 */
public class JWSSimilar {

    /**
     * 此处为配置的路径
     * 需要先在计算机中安装WordNet
     */
    private String dir = "E:\\Program Files (x86)\\WordNet";
    
    private JWS jws = new JWS(dir, "2.1");
    
    private CWSTagger cWSTagger;
    private StopWords stopWords;
    
    public JWSSimilar(CWSTagger tag, StopWords test){
		this.cWSTagger = tag;
		this.stopWords = test;
	}
    
    public double getSentenceSimilirity(String sentence1, String sentence2) 
    {
		double semSim = 0.0;
		
		String[] words1 = getMeaningfulWords(sentence1.toLowerCase());
		String[] words2 = getMeaningfulWords(sentence2.toLowerCase());
		
		
		ArrayList<Double> simScore1 = new ArrayList<Double>();
		ArrayList<Double> simScore2 = new ArrayList<Double>();
		
		for (int i = 0; i < words1.length; i++) {
			simScore1.add(0.0);
		}
		for (int i = 0; i < words2.length; i++) {
			simScore2.add(0.0);
		}
		
		for(int i = 0; i < words1.length; i++) {
			for(int j = 0; j < words2.length; j++) {
				double simScore = getMaxScoreOfLin(words1[i], words2[j]);
				simScore1.set(i, Math.max(simScore1.get(i), simScore));
				simScore2.set(j, Math.max(simScore2.get(j), simScore));
			}
		}
		
		double scoreSum1 = 0.0;
		double scoreSum2 = 0.0;
		
		for(double score : simScore1) {
			scoreSum1 += (score / simScore1.size());
		}
		
		for(double score : simScore2) {
			scoreSum2 += (score / simScore2.size());
		}
		
		semSim = (scoreSum1 + scoreSum2) / 2.0;
		
    	return semSim;
	}
    
    private String[] getMeaningfulWords(String sentence) {
		String[] words = cWSTagger.tag2Array(sentence);
		List<String> meaningfulWords = stopWords.phraseDel(words);
		int length = meaningfulWords.size();
		String[] wordArray = meaningfulWords.toArray(new String[length]);
    	return wordArray;
	}
    
    //OLD
    public double getSimilarity(String str1, String str2){
        String[] strs1 = splitString(str1);
        String[] strs2 = splitString(str2);
        double sum = 0.0;
        for(String s1 : strs1){
            for(String s2: strs2){
                double sc= getMaxScoreOfLin(s1,s2);
                sum+= sc;
                System.out.println("当前计算: "+s1+" VS "+s2+" 的相似度为:"+sc);
            }
        }
        double Similarity = sum /(strs1.length * strs2.length);
        sum=0;
        return Similarity;
    }
    
    //OLD
    private String[] splitString(String str){
        String[] ret = str.split(" ");
        return ret;
    }
    
    private double getMaxScoreOfLin(String word1,String word2){
        Lin lin = jws.getLin();
        double nounScore = lin.max(word1, word2, "n");
        double verbScore = lin.max(word1, word2, "v");
        double maxScore = Math.max(nounScore, verbScore);
        return maxScore;
    }
    

}