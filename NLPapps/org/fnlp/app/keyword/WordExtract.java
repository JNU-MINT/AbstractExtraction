package org.fnlp.app.keyword;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.corpus.StopWords;

/**
 * 基于TextRank的关键词抽取
 * @author tlian
 *
 */

class WDataSet{
	Graph textRankGraph = new Graph();
	ArrayList<Double> weight = new ArrayList<Double>();
	ArrayList<Double> lastWeight = new ArrayList<Double>();
	//词的列表
	List<String> strList = new ArrayList<String>();
	//排除重复词后的列表
	ArrayList<String> uniqueStrList = new ArrayList<String>();
}

public class WordExtract extends AbstractExtractor{
	
	public WordExtract(){
		precision = 1.0;
		dN = 0.85;
	}
	public WordExtract(String segPath, String dicPath) throws Exception{
		cWSTagger = new CWSTagger(segPath);
		stopWords = new StopWords(dicPath);
	}
	public WordExtract(CWSTagger tag, String dicPath){
		this.cWSTagger = tag;
		stopWords = new StopWords(dicPath);
	}
	//目前是这个
	public WordExtract(CWSTagger tag, StopWords test){
		this.cWSTagger = tag;
		this.stopWords = test;
	}
	
	
	private WDataSet getWord(String[] words){
		Set<String> set = new TreeSet<String>();
		WDataSet wds = new WDataSet();
		
		//去除停用词
		if(stopWords!=null){
			wds.strList = stopWords.phraseDel(words);
		}else{
			wds.strList = new ArrayList<String>(); 
			for(int i=0;i<words.length;i++){
				if(words[i].length()>0)
				wds.strList.add(words[i]);
			}
		}
		
		//重复的词只取一个
		for(int i = 0; i < wds.strList.size(); i++){
			String temp = wds.strList.get(i);
			set.add(temp);
		}
		Iterator<String> ii = set.iterator();
		while(ii.hasNext()){
			String str = ii.next();
			wds.uniqueStrList.add(str);
		}
		return wds;
	}
	
	private WDataSet mapInit(int window, WDataSet wDataSet){
		
		TreeMap<String,Integer> treeMap = new TreeMap<String,Integer>();
		Iterator<String> ii = wDataSet.uniqueStrList.iterator();
		int vertexIndex = 0;
		
		//加顶点
		while(ii.hasNext()){	
			String s = ii.next();
			Vertex vertex = new Vertex(s);
			wDataSet.textRankGraph.addVertex(vertex);
			wDataSet.weight.add(1.0);
			wDataSet.lastWeight.add(1.0);
			treeMap.put(s, vertexIndex);
			vertexIndex++;
		}
		
		int length = wDataSet.strList.size();
		while(true){
			if(window > length)
				window /= 2;
			else if(window <= length || window <= 3)
				break;
		}

		//加边
		String word1,word2;
		int index1,index2;
		for(int i = 0; i < wDataSet.strList.size() - window; i++){
			word1 = wDataSet.strList.get(i);
			index1 = treeMap.get(word1);
			for(int j = i + 1; j < i + window; j++){
				word2 = wDataSet.strList.get(j);
				index2 = treeMap.get(word2);
				//WHY:加的是单向边？
				wDataSet.textRankGraph.addEdge(index2, index1, 1.0);
			}
		}
		return wDataSet;
	}
	
	/**
	 * 计算是否已经迭代到收敛条件
	 */
	boolean isCover(WDataSet wds){
		int i;
		double result = 0.0;
		
		for(i = 0; i < wds.textRankGraph.getNVerts(); i++){
			result += Math.abs(wds.weight.get(i) - wds.lastWeight.get(i));
		}

		if(result < precision)
			return true;
		else
			return false;
	}
	
	/**
	 * 把迭代结果保存起来，供下次迭代使用
	 */
	public void toBackW(WDataSet wds){
		int i;
		for(i = 0; i < wds.textRankGraph.getNVerts(); i++){
			wds.lastWeight.set(i, wds.weight.get(i));
		}
	}
	
	public WDataSet cal(WDataSet wds){
		int i, j, forwardCount, times = 0;
		double sumWBackLink, newW;
		ArrayList<Vertex> nextList;
		ArrayList<Double> nextWList;
		Vertex vertex;
		
		while(true){
			times++;
			for(i = 0; i < wds.textRankGraph.getNVerts(); i++){
				vertex = wds.textRankGraph.getVertex(i);
				nextList = vertex.getNext();
				nextWList = vertex.getWNext();
				if(nextList != null){
					sumWBackLink = 0;
					for(j = 0; j < nextWList.size(); j++){
						vertex = nextList.get(j);
						double ww = nextWList.get(j);
						int temp = vertex.index;
						forwardCount = vertex.getForwardCount();
						if(forwardCount != 0)
							sumWBackLink += wds.lastWeight.get(temp) * ww / forwardCount;
					}
					newW = (1 - dN) + dN * sumWBackLink;
					wds.weight.set(i, newW);
				}
			}
			if(isCover(wds) == true){
				System.out.println("Iteration Times: " + times);
				break;
			}
			toBackW(wds);
		}
		return wds;
	}
	
	/**
	 * 对迭代结果进行归一化(0-100)
	 */
	public ArrayList<Integer> normalized(WDataSet wds){
		ArrayList<Integer> wNormalized = new ArrayList<Integer>();
		double max, min, wNDouble;
		int i, wNormalInt;
		double wNormal;
		max = Collections.max(wds.weight);
		min = Collections.min(wds.weight);
		
		if(max != min)
			for(i = 0; i < wds.textRankGraph.getNVerts(); i++){
				wNDouble = wds.weight.get(i);
				wNormal = (wNDouble - min) / (max - min);
				wNormalInt = (int)(100 * wNormal);
				wds.weight.set(i, wNormal);
				wNormalized.add(wNormalInt);
			}
		else
			for(i = 0; i < wds.textRankGraph.getNVerts(); i++)
				wNormalized.add(100);
		return wNormalized;
	}
	
	/**
	 * 选取TOP-K
	 */
	public LinkedHashMap<String,Integer> selectTop(int selectCount, WDataSet wds){
		int i, j, index;
		double max;
		LinkedHashMap<String,Integer> mapList = new LinkedHashMap<String,Integer>();
		
		if(wds.textRankGraph.getNVerts() == 0)
			return mapList;
		
		ArrayList<Integer> wNormalized = normalized(wds);
		toBackW(wds);
		
		int temp = wds.uniqueStrList.size();
		if(selectCount > temp)
			selectCount = temp;
		
		for(j = 0; j < selectCount; j++){
			max = -1.0;
			index = -1;
			for(i = 0; i < wds.textRankGraph.getNVerts(); i++){
				if(wds.lastWeight.get(i) > max){
					max = wds.lastWeight.get(i);
					index = i;
				}
			}
			if(index != -1){
				mapList.put(wds.textRankGraph.getVertex(index).getId(), wNormalized.get(index));
				wds.lastWeight.set(index, -2.0);
			}
		}
		return mapList;
	}
	
	public WDataSet proceed(String[] words){
		WDataSet wds1, wds2;
		//去除停用词
		wds1 = getWord(words);
//		long time1 = System.currentTimeMillis();
//		System.out.println("InitGraph...");
		wds2 = mapInit(windowN, wds1);
//		System.out.println("Succeed In InitGraph!");
//		System.out.println("Now Calculate the PageRank Value...");
		wds1 = cal(wds2);
//		double time = (System.currentTimeMillis() - time1) / 1000.0;
//		System.out.println("Time using: " + time + "s");
//		System.out.println("PageRank Value Has Been Calculated!");
		return wds1;
	}
	
	public Map<String,Integer> extract(String str, int num){
		String[] words;
		if(cWSTagger!=null)
			words = cWSTagger.tag2Array(str);
		else
			words = str.split("\\s+");
		WDataSet wds = proceed(words);
		LinkedHashMap<String,Integer> mapList = selectTop(num, wds);
		return mapList;
	}

	@Override
	public String extract(String str, int num, boolean isWeighted) {
		return extract(str,num).toString();
	}
}








