package XMLProcess;

import java.io.File;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import processor.XMLParser;


public class XMLParseTest {
	public static void main(String[] args) throws Exception {
		XMLParser xmlParse = new XMLParser(new File("F:\\patent(F)\\uspatent2014\\140107\\xml\\US-08623430-B2.xml"));
//		String string = xmlParse.GetFirstElememt("doc-number");
//		System.out.println(string);
		System.out.println(xmlParse.getFirstNode("//invention-title"));
		
	}
}