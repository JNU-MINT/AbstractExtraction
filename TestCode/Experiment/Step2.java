package Experiment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import model.Document;
import model.Patent;

import Lucene.SearchProcessor;

/**
 * 算法步骤二
 * 获取相关文件集合
 * @author qiusd
 */
public class Step2 {

	PatentPathProcess patentPathProcess;

	public Step2() throws Exception {
		patentPathProcess = new PatentPathProcess();
		System.out.println("step2 constructor done");
	}

	private Map<String, Double> getRelatedDocs(String[] queryStrings)
			throws Exception {
		Map<String, Double> relatedFileMap = new HashMap<String, Double>();
		// TODO:配置路径
		SearchProcessor st = new SearchProcessor("C:\\Data\\AbstractExtraction\\index");
		for (String queryString : queryStrings) {
			Map<String, Double> searchResultsMap = st.searchInAllFields(
					queryString, 3);
			for (String fileName : searchResultsMap.keySet()) {
				if (!fileName.contains("addition")) {
					if (relatedFileMap.containsKey(fileName)) {
						if (searchResultsMap.get(fileName) > relatedFileMap
								.get(fileName)) {
							relatedFileMap.put(fileName,
									searchResultsMap.get(fileName));
						}
					} else {
						relatedFileMap.put(fileName,
								searchResultsMap.get(fileName));
					}
				}
			}
		}
		st.closeAll();
		System.out.println(new Date().toLocaleString());
		System.out.println("Get Related Doc done");
		System.out.println("there are " + relatedFileMap.size()
				+ " related files");
		return relatedFileMap;
	}

	private ArrayList<Patent> convertMap2Patent(
			Map<String, Double> relatedFileMap) {
		ArrayList<Patent> patentList = new ArrayList<Patent>();
		for (String fileName : relatedFileMap.keySet()) {
			Patent patent = new Patent();
			patent.fileName = fileName;
			patent.filePath = patentPathProcess.getFilePath(fileName);
			patent.relatedScore = relatedFileMap.get(fileName);
			patentList.add(patent);
		}
		return patentList;
	}

	public Document excuteStep2(Document doc) throws Exception {
		String[] words = (String[]) doc.queryWordSet.toArray(new String[0]);
		doc.relatedPatents = convertMap2Patent(getRelatedDocs(words));
		patentPathProcess.entireXMLMap.clear();
		return doc;
	}

	public static void main(String[] args) throws Exception {
		XMLDocHandler docHandler = new XMLDocHandler();
		// 记得配置路径
		Document doc = docHandler.getDoc("C:\\Data\\AbstractExtraction\\experiment\\US-08341762-B2.xml");
		Step1Word step1 = new Step1Word();
		doc = step1.ExcuteStep1(doc);
		Step2 step2 = new Step2();
		doc = step2.excuteStep2(doc);
	}
}
