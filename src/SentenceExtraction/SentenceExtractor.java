package SentenceExtraction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.fnlp.app.keyword.AbstractExtractor;
import org.fnlp.app.keyword.Graph;
import org.fnlp.app.keyword.Vertex;

import edu.fudan.nlp.cn.Sentenizer;
import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.corpus.StopWords;

/**
 * 句子类
 * 与NLPapps/org/fnlp/app/keyword/WordExtract中的WDataSet类似
 * @author qiusd
 */
class SDataSet{
	Graph textRankGraph = new Graph();
	ArrayList<Double> weight = new ArrayList<Double>();
	ArrayList<Double> lastWeight = new ArrayList<Double>();
	//句子列表
	ArrayList<String> sentenceList = new ArrayList<String>();
	//不重复句子列表
	ArrayList<String> uniqueSentenceList = new ArrayList<String>();
}


//TODO 设置AbstractExtractor权重收敛的默认阈值
/**
 * @author qiusd
 * 重要句子提取类
 */
public class SentenceExtractor extends AbstractExtractor{
	
	
	/**
	 * sR：定义句子的相似值比例
	 * sRValue:：相似度比例达到sR的实际阈值
	 * sT：选取的句子数与句子总数的比值
	 */
	private double sR;
	private double sRValue;
	private double sT;
	
	private int sentenceNum;
	
	private SDataSet getSentence(String[] sentences){
		Set<String> set = new TreeSet<String>();
		SDataSet sds = new SDataSet();
		
		sentenceNum = sentences.length;
		
		sds.sentenceList = new ArrayList<String>(); 
		for(int i=0;i<sentences.length;i++){
			if(sentences[i].length()>0)
			sds.sentenceList.add(sentences[i]);
		}
		
		//重复的句子只取一个
		for(int i = 0; i < sds.sentenceList.size(); i++){
			String temp = sds.sentenceList.get(i);
			set.add(temp);
		}
		Iterator<String> ii = set.iterator();
		while(ii.hasNext()){
			String str = ii.next();
			sds.uniqueSentenceList.add(str);
		}
		
		return sds;
	}
	
	
	/**
	 * 构建TextRank图
	 */
	private SDataSet mapInit(SDataSet sDataSet) throws Exception{
		
		JWSSimilar similar = new JWSSimilar();
		
		//计算sT的阈值
		ArrayList<Double> weightList = new ArrayList<>();
		for(int i = 0; i < sDataSet.uniqueSentenceList.size() - 1; i++)
		{
			for(int j = i + 1; j <  sDataSet.uniqueSentenceList.size(); j++)
			{
				String sentence1 = sDataSet.uniqueSentenceList.get(i);
				String sentence2 = sDataSet.uniqueSentenceList.get(j);
				double similarScore = similar.getSentenceSimilarity(sentence1, sentence2);
				weightList.add(similarScore);
			}
		}
		Collections.sort(weightList);
		if(weightList.size() * sR <= 1.0) {
			sRValue = 0 ;
		}
		else {
			sRValue = weightList.get((int)(weightList.size() * sR) - 1);
		}
		
		//删除与其他句子不够相似的句子
		for(int i = sDataSet.uniqueSentenceList.size()-1; i >= 0; i--) {
			double similiritySum = 0.0;
			for (int j = 0; j < sDataSet.uniqueSentenceList.size(); j++) {
				String sentence1 = sDataSet.uniqueSentenceList.get(i);
				String sentence2 = sDataSet.uniqueSentenceList.get(j);
				double similarity = similar.getSentenceSimilarity(sentence1, sentence2);
				if(i != j &&  similarity > sRValue) {
					similiritySum += similarity;
				}
			}
			if(similiritySum == 0.0) {
				sDataSet.uniqueSentenceList.remove(i);
			}
		}
		
		//加顶点
		TreeMap<String,Integer> treeMap = new TreeMap<String,Integer>();
		Iterator<String> ii = sDataSet.uniqueSentenceList.iterator();
		int vertexIndex = 0;
		while(ii.hasNext()){
			String s = ii.next();
			Vertex vertex = new Vertex(s);
			sDataSet.textRankGraph.addVertex(vertex);
			sDataSet.weight.add(1.0);
			sDataSet.lastWeight.add(1.0);
			treeMap.put(s, vertexIndex);
			vertexIndex++;
		}
		
		//加边
		String sentence1,sentence2;
		int index1,index2;
		System.out.println("Similirity of sentences:");
		for(int i = 0; i < sDataSet.uniqueSentenceList.size() - 1; i++){
			sentence1 = sDataSet.uniqueSentenceList.get(i);
			index1 = treeMap.get(sentence1);
			for(int j = i + 1; j <  sDataSet.uniqueSentenceList.size(); j++){
				sentence2 = sDataSet.uniqueSentenceList.get(j);
				index2 = treeMap.get(sentence2);
				double similarScore = similar.getSentenceSimilarity(sentence1, sentence2);
				//OUTPUT
				System.out.println(sentence1 + "--->" + sentence2 + ":" + similarScore);
				//WHY:原来加的是单向边？
				if(similarScore >= sRValue) {
					sDataSet.textRankGraph.addEdge(index2, index1, similarScore);
					sDataSet.textRankGraph.addEdge(index1, index2, similarScore);
				}
			}
		}
		return sDataSet;
	}
	
	/**
	 * 判断收敛
	 */
	boolean isCover(SDataSet sds){
		int i;
		double result = 0.0;
		
		for(i = 0; i < sds.textRankGraph.getNVerts(); i++){
			result += Math.abs(sds.weight.get(i) - sds.lastWeight.get(i));
		}
		if(result < precision)
			return true;
		else
			return false;
	}
	
	/**
	 * 保存迭代结果，供下次迭代使用
	 */
	public void toBackW(SDataSet sds){
		int i;
		for(i = 0; i < sds.textRankGraph.getNVerts(); i++){
			sds.lastWeight.set(i, sds.weight.get(i));
		}
	}
	
	/**
	 * 进行迭代计算
	 */
	public SDataSet cal(SDataSet sds){
		int i, j, forwardCount, times = 0;
		double sumWBackLink, newW;
		ArrayList<Vertex> nextList;
		ArrayList<Double> nextWList;
		Vertex vertex;
		
		while(true){
			times++;
			for(i = 0; i < sds.textRankGraph.getNVerts(); i++){
				vertex = sds.textRankGraph.getVertex(i);
				nextList = vertex.getNext();
				nextWList = vertex.getWNext();
				if(nextList != null){
					sumWBackLink = 0;
					//遍历连到顶点Vi的所有点
					for(j = 0; j < nextWList.size(); j++){
						vertex = nextList.get(j);
						double ww = nextWList.get(j);
						int temp = vertex.index;
						//Vj上面的连线数目
						forwardCount = vertex.getForwardCount();
						if(forwardCount != 0)
							sumWBackLink += sds.lastWeight.get(temp) * ww / forwardCount;
					}
					newW = (1 - dN) + dN * sumWBackLink;
					sds.weight.set(i, newW);
				}
			}
			if(isCover(sds) == true){
				System.out.println("\n" + "Total Iteration Times: " + times + "\n");
				break;
			}
			toBackW(sds);
		}
		return sds;
	}
	
	/**
	 * 对迭代结果进行归一化(0-100)
	 */
	public ArrayList<Integer> normalized(SDataSet sds){
		ArrayList<Integer> wNormalized = new ArrayList<Integer>();
		double max, min, wNDouble;
		int i, wNormalInt;
		double wNormal;
		max = Collections.max(sds.weight);
		min = Collections.min(sds.weight);
		
		if(max != min)
			for(i = 0; i < sds.textRankGraph.getNVerts(); i++){
				wNDouble = sds.weight.get(i);
				wNormal = (wNDouble - min) / (max - min);
				wNormalInt = (int)(100 * wNormal);
				sds.weight.set(i, wNormal);
				wNormalized.add(wNormalInt);
			}
		else
			for(i = 0; i < sds.textRankGraph.getNVerts(); i++)
				wNormalized.add(100);
		return wNormalized;
	}
	
	/**
	 * 选取TOP-K
	 */
	public LinkedHashMap<String,Integer> selectTop(int selectCount, SDataSet sds){
		int i, j, index;
		double max;
		LinkedHashMap<String,Integer> mapList = new LinkedHashMap<String,Integer>();
		if(sds.textRankGraph.getNVerts() == 0)
			return mapList;
		ArrayList<Integer> wNormalized = normalized(sds);
		toBackW(sds);
		int temp = sds.uniqueSentenceList.size();
		if(selectCount > temp)
			selectCount = temp;
		for(j = 0; j < selectCount; j++){
			max = -1.0;
			index = -1;
			for(i = 0; i < sds.textRankGraph.getNVerts(); i++){
				if(sds.lastWeight.get(i) > max){
					max = sds.lastWeight.get(i);
					index = i;
				}
			}
			if(index != -1){
				mapList.put(sds.textRankGraph.getVertex(index).getId(), wNormalized.get(index));
				sds.lastWeight.set(index, -2.0);
			}
		}
		return mapList;
	}
	
	public SDataSet proceed(String[] words) throws Exception{
		SDataSet sds;
		//WHY
		sds = getSentence(words);
//		long time1 = System.currentTimeMillis();
//		System.out.println("InitGraph...");
		sds = mapInit(sds);
//		System.out.println("Succeed In InitGraph!");
//		System.out.println("Now Calculate the PageRank Value...");
		sds = cal(sds);
//		double time = (System.currentTimeMillis() - time1) / 1000.0;
//		System.out.println("Time using: " + time + "s");
//		System.out.println("PageRank Value Has Been Calculated!");
		return sds;
	}
	
	/**
	 * 按句子数抽取
	 */
	public Map<String,Integer> extract(String docPath, int num){
		String[] sentences;
//		这是Fudannlp中的断句函数，太naive
//		sentences = Sentenizer.split(str);
		sentences = SentenceDetector.split(docPath);
		SDataSet sds = null;
		try {
			sds = proceed(sentences);
		} catch (Exception e) {
			
		}
		LinkedHashMap<String,Integer> mapList = selectTop(num, sds);
		return mapList;
	}
	
	/**
	 * 按比例抽取关键句子
	 * @param docPath 文本路径
	 * @param sR 比例(0-1)，超过此比例的权重才回建边
	 * @param sT 比例(0-1)，只选取超过此比例的句子作为最终结果
	 */
	public Map<String,Integer> extract(String docPath, double sR, double sT) throws Exception{
		this.sR = sR;
		this.sT = sT;
		String[] sentences;
		sentences = SentenceDetector.split(docPath);
		SDataSet sds = proceed(sentences);
		LinkedHashMap<String,Integer> mapList = selectTop((int)(this.sT * sentenceNum), sds);
		return mapList;
	}
	
	/**
	 * 没用到
	 */
	@Override
	public String extract(String str, int num, boolean isWeighted) {
		return extract(str,num).toString();
	}
}








