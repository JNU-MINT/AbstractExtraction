package SentenceExtraction;
import java.util.ArrayList;
import java.lang.String;

import processor.SentenceProcessor;

import edu.sussex.nlp.jws.JWS;
import edu.sussex.nlp.jws.Lin;

/**
 * 利用Java WordNet计算
 * 词或句的相似度
 * @author qiusd
 *
 */
public class JWSSimilar {

    /**
     * 此处为配置的路径
     * 需要先在计算机中安装WordNet
     * 再在环境变量中设置“WNHOME”为WordNet安装路径
     * 到"WordNet"文件夹为止
     */
    private JWS jws = new JWS(System.getenv("WNHOME"), "2.1");
    
    SentenceProcessor sProcess;
    
    public JWSSimilar() throws Exception{
    	sProcess = new SentenceProcessor();
    }
    
    public double getSentenceSimilarity(String sentence1, String sentence2) throws Exception
    {
		double semSim = 0.0;
		String[] words1 = sProcess.getMeaningfulWords(sentence1.toLowerCase());
		String[] words2 = sProcess.getMeaningfulWords(sentence2.toLowerCase());
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
				double simScore = getWordSimilarity(words1[i], words2[j]);
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
    
    /**
     * 计算词语相似度
     */
    public double getWordSimilarity(String word1,String word2){
        Lin lin = jws.getLin();
        double nScore = lin.max(word1, word2, "n");
        double vScore = lin.max(word1, word2, "v");
        double maxScore = Math.max(nScore, vScore);
        return maxScore;
    }
}