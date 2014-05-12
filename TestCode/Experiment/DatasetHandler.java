package Experiment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import processor.CsvProcessor;
import processor.FileProcessor;
import model.Document;
import model.Word;

/**
 * 完成算法的前四个步骤
 * 将每个xml文件的特征抽取出来
 * 转化为csv文件
 * @author qiusd
 */
public class DatasetHandler {
	
	private void writeToCsv(Document doc, String filePath) throws Exception {
		ArrayList<String[]> dataList = new ArrayList<>();
		for (Word word : doc.abstractWords) {
			ArrayList<String> wordInfoList = new ArrayList<>();
			wordInfoList.add(word.text);
			wordInfoList.add(Double.toString(word.linkFeature));
			wordInfoList.add(Double.toString(word.tfidf));
			wordInfoList.add(Double.toString(word.position));
			wordInfoList.add(Double.toString(word.wordLength));
			wordInfoList.add(Double.toString(word.textRank));
			wordInfoList.add(Double.toString(word.textRankValue));
			wordInfoList.add(Double.toString(word.lable));
			dataList.add((String[]) wordInfoList.toArray(new String[wordInfoList.size()]));
		}
		CsvProcessor.writeToCsv(filePath, dataList);
	}
	
	private void excute(String dirPath) throws Exception {
		
		FileProcessor fileProcessor = new FileProcessor();
		List<String> filePaths = fileProcessor.getFilesByDir(dirPath);
		
		for (String filePath : filePaths) {
			try {
				System.out.println("\n\n" + filePath);
				DocHandler docHandler = new DocHandler();
				Document doc = docHandler
						.getDoc(filePath);
				Step1Word step1 = new Step1Word();
				doc = step1.ExcuteStep1(doc);
				step1 = null;
				Step2 step2 = new Step2();
				doc = step2.excuteStep2(doc);
				step2 = null;
				Step3 step3 = new Step3();
				doc = step3.excuteStep3(doc);
				step3 = null;
				Step4 step4 = new Step4();
				doc = step4.excuteStep4(doc);
				step4 = null;
				File xmlFile = new File(filePath);
				String csvName = xmlFile.getName().replace("xml", "csv");
				String csvPath = xmlFile.getParentFile().getParentFile() + "//csv//" + csvName;
				writeToCsv(doc, csvPath);
			} catch (Exception e) {
				System.err.println(filePath + " failed");
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		DatasetHandler datasetHandler = new DatasetHandler();
		datasetHandler.excute("F:\\patent(F)\\uspatent2014\\140107\\xml");
	}
}
