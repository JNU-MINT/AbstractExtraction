package Experiment;

import java.util.ArrayList;
import java.util.List;

import processor.CsvProcessor;
import processor.FileProcessor;

import model.Word;

/**
 * 算法步骤五
 * @author qiusd
 */
public class Step5 {

	public ArrayList<Word> getData(List<String> csvPaths) throws Exception {
		ArrayList<Word> datasetList = new ArrayList<Word>();
		for (String csvPath : csvPaths) {
			ArrayList<String[]> csvList = CsvProcessor.readFromCsv(csvPath);
			for (String[] csvRow : csvList) {
				Word word = new Word();
				word.text = csvRow[0];
				word.linkFeature = Double.parseDouble(csvRow[1]);
				word.tfidf = Double.parseDouble(csvRow[2]);
				word.position = Double.parseDouble(csvRow[3]);
				word.wordLength = Double.parseDouble(csvRow[4]);
				word.textRank = Double.parseDouble(csvRow[5]);
				word.textRankValue = Double.parseDouble(csvRow[6]);
				word.lable = Double.parseDouble(csvRow[7]);
				datasetList.add(word);
			}
		}
		return datasetList;
	}

	public void excute(String trainDirPath) throws Exception {
		FileProcessor fileProcessor = new FileProcessor();
		List<String> trainPaths = fileProcessor.getFilesByDir(trainDirPath);
		ArrayList<Word> trainWordList = getData(trainPaths);
		SVM svm = new SVM();
		svm.train(trainWordList);
	}

	public static void main(String[] args) throws Exception {
		Step5 step5 = new Step5();
		step5.excute("F:\\patent(F)\\uspatent2014\\140107\\experiment\\train");
	}
}
