package Experiment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import processor.SentenceProcessor;
import processor.XMLParser;
import model.Document;
import model.Word;

public class DocHandler {
	
	public Document getDoc(String filePath) throws Exception {
		Document doc = new Document();
		
		XMLParser xmlParser = new XMLParser(new File(filePath));
		
		String titlestrString = xmlParser.getFirstNode("//invention-title");
		doc.abstractString = xmlParser.getFirstNode("//abstract/p");
		
		SentenceProcessor sentenceProcessor = new SentenceProcessor();
		
		String[] titleArray = sentenceProcessor.getStemmedWord(titlestrString);
		doc.titleWords = new ArrayList<String>(Arrays.asList(titleArray));
		
		String[] abstractWordArray = sentenceProcessor.getStemmedWord(doc.abstractString);
		for (String abstractWord : abstractWordArray) {
			Word word = new Word();
			word.text = abstractWord;
			doc.abstractWords.add(word);
		}
		return doc;
	}
	
	public static void main(String[] args) throws Exception {
		DocHandler docHandler = new DocHandler();
		Document doc = docHandler.getDoc("F:\\patent(F)\\uspatent2014\\smallxml\\US-08621662-B2.xml");
	}
}
