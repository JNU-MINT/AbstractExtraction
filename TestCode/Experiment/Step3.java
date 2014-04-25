package Experiment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import model.Document;
import model.Patent;

import edu.fudan.nlp.cn.ner.stringPreHandlingModule;

import Lucene.SearchProcessor;

import processor.SentenceProcessor;
import processor.XMLParser;

public class Step3 {

	PatentPathProcess patentPathProcess;

	public Step3() throws Exception {
		patentPathProcess = new PatentPathProcess();
		System.out.println("step3 constructor done");
	}

	private ArrayList<Patent> extract(ArrayList<Patent> patents)
			throws Exception {
		for (Patent patent : patents) {
			XMLParser xmlParser = new XMLParser(new File(patent.filePath));
			// 只取第一个
			String peoName = xmlParser.getFirstNode("//addressbook/last-name")
					+ " " + xmlParser.getFirstNode("//addressbook/first-name");
			patent.addressBookSet.add(peoName);
			patent.addressBookSet.add(xmlParser
					.getFirstNode("//addressbook/orgname"));
			patent.addressBookSet.add(xmlParser
					.getFirstNode("//patcit/document-id/doc-number"));
		}
		return patents;
	}

	private ArrayList<Patent> search(ArrayList<Patent> patents)
			throws Exception {
		SearchProcessor st = new SearchProcessor("F:\\patent(F)\\index");
		SentenceProcessor sentenceProcessor = new SentenceProcessor();
		for (Patent patent : patents) {
			for (String queryString : patent.addressBookSet) {
				if (!queryString.isEmpty()) {
					Map<String, Double> searchResultMap = st.searchInAllFields(
							queryString, 2);
					for (String fileName : searchResultMap.keySet()) {
						String filePath = patentPathProcess
								.getFilePath(fileName);
						XMLParser xmlParser = new XMLParser(new File(filePath));
						String outLinkTitle = xmlParser
								.getFirstNode("//invention-title");
						if (!outLinkTitle.isEmpty()) {
							String[] titleWords = sentenceProcessor
									.getStemmedWord(outLinkTitle);
							patent.linkTitleSet.add(new ArrayList(Arrays.asList(titleWords)));
						}
					}
				}
			}
		}
		return patents;
	}

	public Document excuteStep3(Document doc) throws Exception {
		ArrayList<Patent> patents = extract(doc.relatedPatents);
		patents = search(patents);
		doc.relatedPatents = patents;
		patentPathProcess.entireXMLMap.clear();
		return doc;
	}

	public static void main(String[] args) throws Exception {
		DocHandler docHandler = new DocHandler();
		Document doc = docHandler
				.getDoc("F:\\patent(F)\\uspatent2014\\smallxml\\US-08621662-B2.xml");
		Step1Word step1 = new Step1Word();
		doc = step1.ExcuteStep1(doc);
		Step2 step2 = new Step2();
		doc = step2.excuteStep2(doc);
		Step3 step3 = new Step3();
		doc = step3.excuteStep3(doc);
	}
}
