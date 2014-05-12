package SentenceExtraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Lucene.SearchProcessor;

import processor.FileProcessor;
import processor.SentenceProcessor;

/**
 * 仅考虑有意义的词
 * 
 * @author qiusd
 */
public class TFIDF {

	private HashMap<String, Double> tfMap = new HashMap<String, Double>();
	private HashMap<String, Double> idfMap = new HashMap<String, Double>();
	private HashMap<String, Double> tfidfMap = new HashMap<String, Double>();
	private SentenceProcessor sentenceProcess;

	public TFIDF() throws Exception {
		sentenceProcess = new SentenceProcessor();
	}

	/**
	 * 计算TF
	 */
	private void calTf(String text) throws Exception {
		String[] meaningfulWords = sentenceProcess.getStemmedWord(text);
		int wordNum = meaningfulWords.length;
		int wordtf;
		for (int i = 0; i < wordNum; i++) {
			wordtf = 0;
			for (int j = 0; j < wordNum; j++) {
				if (meaningfulWords[i] != " " && i != j) {
					if (meaningfulWords[i].equals(meaningfulWords[j])) {
						meaningfulWords[j] = " ";
						wordtf++;
					}
				}
			}
			if (meaningfulWords[i] != " ") {
				tfMap.put(meaningfulWords[i], (new Double(++wordtf)) / wordNum);
				meaningfulWords[i] = " ";
			}
		}
	}

	/**
	 * 利用语料库计算IDF
	 */
	private void calIdf(String text, String dir) throws Exception {
		// IDF＝log((1+|D|)/|Dt + 1|)
		// |D|表示文档总数，|Dt|表示包含关键词t的文档数量
		FileProcessor fileProcessor = new FileProcessor();
		List<String> fileList = fileProcessor.getFilesByDir(dir);
		float D = fileList.size();
		int count;
		ArrayList<String[]> corpusList = new ArrayList<String[]>();
		for (int i = 0; i < D; i++) {
			String corpusString = fileProcessor.readFiles(fileList.get(i));
			String[] meaningfulWords = sentenceProcess
					.getMeaningfulWords(corpusString);
			corpusList.add(meaningfulWords);
		}
		for (String meaningfulWord : tfMap.keySet()) {
			count = 0;
			for (String[] corpusWords : corpusList) {
				for (String corpusWord : corpusWords) {
					if (corpusWord.equals(meaningfulWord)) {
						count++;
						break;
					}
				}
			}
			double idf = Math.log(D / (count + 1));
			idfMap.put(meaningfulWord, idf);
		}
	}

	/**
	 * 利用Lucene计算IDF
	 */
	private void calIdf(String text) throws Exception {
		float D = 498633;
		SearchProcessor st = new SearchProcessor("F:\\patent(F)\\index");
		for (String meaningfulWord : tfMap.keySet()) {
			int count = st.getAllFieldsSearchNum(meaningfulWord);
			double idf = Math.log(D / (count + 1));
			idfMap.put(meaningfulWord, idf);
		}
		st.closeAll();
	}

	/**
	 * 利用语料库计算TFIDF
	 */
	public Map<String, Double> calTfidf(String text, String dir) throws Exception {
		calTf(text);
		calIdf(text, dir);
		for (String tfidfWord : tfMap.keySet()) {
			tfidfMap.put(tfidfWord,
					tfMap.get(tfidfWord) * idfMap.get(tfidfWord));
		}
		return tfidfMap;
	}

	/**
	 * 利用Lucene计算TFIDF
	 */
	public Map<String, Double> calTfidf(String text) throws Exception {
		calTf(text);
		calIdf(text);
		for (String tfidfWord : tfMap.keySet()) {
			tfidfMap.put(tfidfWord,
					tfMap.get(tfidfWord) * idfMap.get(tfidfWord));
		}
		return tfidfMap;
	}

	public static void main(String args[]) throws Exception {
		String s = "A method for bulk transport, the method comprising:"
				+ "positioning an inflatable bladder over a gravity-fed "
				+ "closable aperture in a bulk shipping container; and"
				+ "after inflation of said bladder, introducing a bulk"
				+ " material into said container, whereby said bladder at "
				+ "least substantially impedes access by said bulk material"
				+ " to said aperture, said bladder remaining inflated during "
				+ "loading of said container;said bladder having an upper "
				+ "bound and a lower bound, said method including positioning "
				+ "said lower bound proximal said aperture and introducing "
				+ "said bulk material into said rail car at a height not "
				+ "exceeding the height of said upper bound.";

		TFIDF tfidf = new TFIDF();
		// tfidf.calTfidf(s,
		// "F:\\百度云\\研究方向\\一种基于背景知识的摘要提取算法\\task\\task140410\\实验材料\\文件库");
		Map<String, Double> map = tfidf.calTfidf(s);
		for (String hh : map.keySet())
			System.out.println(hh + ": " + map.get(hh) + ", "
					+ map.get(hh) + ", " + map.get(hh));
	}
}
