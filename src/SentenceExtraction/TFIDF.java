package SentenceExtraction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.util.exception.LoadModelException;

public class TFIDF {
	private CWSTagger cWSTagger;
	private static List<String> fileList = new ArrayList<String>();
	private static HashMap<String,Double> tf = new HashMap<String,Double>();
	private static HashMap<String,Double> tfidf = new HashMap<String,Double>();
	//private static HashMap<String, HashMap<String, Float>> allTheTf = new HashMap<String, HashMap<String, Float>>();
	//private static HashMap<String, HashMap<String, Integer>> allTheNormalTF = new HashMap<String, HashMap<String, Integer>>();

	/*
	 * 
	 */
	public TFIDF() throws LoadModelException {
		cWSTagger = new CWSTagger("models/seg.m");
	}

	// 找出text中有意义的词，返回一个HashMap
	public static void getMeaningfulWords(String text)throws Exception {
		SentenceProcess getWords = new SentenceProcess();
		String[] meaningfulWords = getWords.getMeaningfulWords(text);
		int wordNum = meaningfulWords.length;
		//HashMap<String, Double> tfidfWords = new HashMap<String, Double>();// 正规化
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
				tf.put(meaningfulWords[i], (new Double(++wordtf)) / wordNum);
				meaningfulWords[i] = " ";
			}
			// System.out.println(meaningfulWords[i]);
		}
	}

	//输入一个文件库的目录，遍历文件并计算一个text的tfidf值
	public static void getTFIDF(String text,String dir) throws Exception {  
      //公式IDF＝log((1+|D|)/|Dt|)，其中|D|表示文档总数，|Dt|表示包含关键词t的文档数量。  	
		getMeaningfulWords(text);
		readDirs(dir);
	          float D = fileList.size();//文档总数  
	          int count;
	          double idf;
	          int n=0,i;
	          ArrayList<String[]> allWord = new ArrayList<String []>();
	          for(i = 0; i < D; i++){
	        	  //System.out.println(i);
	        	  String[] meaningfulWords1 = cutWord(fileList.get(i));
	        	  allWord.add(meaningfulWords1);
	          }
	          for(String tfidfWord : tf.keySet()) {
	        	  count = 0;
	          for(i=0; i<D; i++){
				//String[] meaningfulWords1 = cutWord(fileList.get(i));
				//n++;
				//System.out.println(n);
	        	  for(String word:allWord.get(i)){
	        		  if (word.equals(tfidfWord)){
	        			  count++;
	        			  break;
	        		  }
	        	  }
	          }
	          idf = TFIDF.log(D, count);
	          System.out.println(tf.get(tfidfWord)+" "+idf);//输出每个词的tf,idf
	          tfidf.put(tfidfWord, tf.get(tfidfWord)/idf);
	          }
	      }  

	public static float log(float value, float base) {
		return (float) (Math.log(value) / Math.log(base));
	}

	public static void readDirs(String filepath)throws FileNotFoundException, IOException {
		try {
			File file = new File(filepath);
			if (!file.isDirectory()) {
				System.out.println("输入的参数应该为[文件夹名]");
				System.out.println("filepath: " + file.getAbsolutePath());
			} else if (file.isDirectory()) {
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File readfile = new File(filepath + "\\" + filelist[i]);
					if (!readfile.isDirectory()) {
						// System.out.println("filepath: " +
						// readfile.getAbsolutePath());
						fileList.add(readfile.getAbsolutePath());
					} else if (readfile.isDirectory()) {
						readDirs(filepath + "\\" + filelist[i]);
					}
				}
			}

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		//return fileList;
	}

	public static String readFiles(String file) throws FileNotFoundException,
			IOException {
		StringBuffer sb = new StringBuffer();
		InputStreamReader is = new InputStreamReader(new FileInputStream(file),
				"gbk");
		BufferedReader br = new BufferedReader(is);
		String line = br.readLine();
		while (line != null) {
			sb.append(line).append("\r\n");
			line = br.readLine();
		}
		br.close();
		return sb.toString();
	}

	public static String[] cutWord(String file) throws Exception {
		String text = readFiles(file);	
		SentenceProcess getWords = new SentenceProcess();
		String[] cutWordResult = getWords.getMeaningfulWords(text);
		return cutWordResult;
	}

/*	public static HashMap<String, Float> tf(String[] cutWordResult) {
		HashMap<String, Float> tf = new HashMap<String, Float>();// 正规化
		int wordNum = cutWordResult.length;
		int wordtf = 0;
		for (int i = 0; i < wordNum; i++) {
			wordtf = 0;
			for (int j = 0; j < wordNum; j++) {
				if (cutWordResult[i] != " " && i != j) {
					if (cutWordResult[i].equals(cutWordResult[j])) {
						cutWordResult[j] = " ";
						wordtf++;
					}
				}
			}
			if (cutWordResult[i] != " ") {
				tf.put(cutWordResult[i], (new Float(++wordtf)) / wordNum);
				cutWordResult[i] = " ";
			}
		}
		return tf;
	}
*/
	public void test(String s) {
		String[] words = cWSTagger.tag2Array(s);
		for (int i = 0; i < words.length; i++) {
			System.out.println(words[i]);
		}
	}

	public static void main(String args[]) throws Exception {
		String s = "A method for bulk transport, the method comprising:"
				+ "positioning an inflatable bladder over a gravity-fed closable aperture in a bulk shipping container; and"
				+ "after inflation of said bladder, introducing a bulk material into said container, whereby said bladder at least substantially impedes access by said bulk material to said aperture, said bladder remaining inflated during loading of said container;"
				+ "said bladder having an upper bound and a lower bound, said method including positioning said lower bound proximal said aperture and introducing said bulk material into said rail car at a height not exceeding the height of said upper bound.";
		// TFIDF test1 = new TFIDF();
		// test1.test(s);
		// TFIDF.getTFIDF(s, true);
		//HashMap<String, Double> h = TFIDF.getMeaningfulWords(s);
		//TFIDF.getMeaningfulWords(s);
		TFIDF.getTFIDF(s,"C:\\work\\AbstractExtract\\task140410\\expeMaterial\\xmlFiles");
		for (String hh : tfidf.keySet())
			System.out.println(hh + " " + tfidf.get(hh));
	}
}
