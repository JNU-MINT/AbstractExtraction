package Experiment;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import processor.CsvProcessor;
import processor.XMLDivider;



/**
 * 实现专利文件名到真实路径的映射 并将映射表存放于csv文件
 * @author qiusd
 */
public class PatentPathProcess {

	public Map<String, String> entireXMLMap = new HashMap<String, String>();
	
	public PatentPathProcess() throws Exception {
		generateXMLMap();
	}
	
	
	private void generateXmlPathCsv() throws Exception {
		Map<String, String> entireXMLMap = new HashMap<String, String>();
		String[] dirPaths = new String[] {
				"D:\\patent(D)\\uspatent2009\\smallxml",
				"D:\\patent(D)\\uspatent2010\\smallxml",
				"G:\\patent(G)\\uspatent2011\\smallxml",
				"G:\\patent(G)\\uspatent2012\\smallxml",
				"G:\\patent(G)\\uspatent2013\\smallxml" };
		XMLDivider xmlFileDivide = new XMLDivider();
		for (String dirPath : dirPaths) {
			File[] files = xmlFileDivide.getXMLFilesByDir(dirPath);
			for (File file : files) {
				entireXMLMap.put(file.getName(), file.getAbsolutePath());
			}
			System.out.println(new Date().toLocaleString());
			System.out.println("Get " + dirPath + " done");
		}
		System.out.println(new Date().toLocaleString());
		System.out.println("Get all xml path done");
		CsvProcessor csvProcess = new CsvProcessor();
		ArrayList<String[]> contentList = new ArrayList<String[]>();
		for(Map.Entry<String, String> entry: entireXMLMap.entrySet()) {
			String[] content = {entry.getKey(), entry.getValue()};
			contentList.add(content);
		}
		csvProcess.writeToCsv("./xmlPath.csv", contentList);
	}
	
	public void generateXMLMap() throws Exception{
		CsvProcessor csvProcess = new CsvProcessor();
		ArrayList<String[]> csvList = csvProcess.readFromCsv("./xmlPath.csv");
		for (String[] strings : csvList) {
			entireXMLMap.put(strings[0], strings[1]);
		}
		System.out.println(new Date().toLocaleString());
		System.out.println("Get file map done");
	}
	
	public String getFilePath(String fileName) {
		return entireXMLMap.get(fileName);
	}
	
	/**
	 * 生成csv文件
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		PatentPathProcess ppp = new PatentPathProcess();
		ppp.generateXmlPathCsv();
	}
}
