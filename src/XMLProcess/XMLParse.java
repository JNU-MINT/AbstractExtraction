package XMLProcess;

import java.io.*;
import java.util.List;


import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.xpath.XPath;

@SuppressWarnings("deprecation")
public class XMLParse {

	SAXBuilder sBuilder;
	Document doc;
	
	
	public XMLParse() {
	}
	
	/**
	 * 构造函数 输入XML文件
	 * @param xmlFile
	 */
	public XMLParse(File xmlFile) throws Exception {
		SetXmlByFile(xmlFile);
	}

	/**
	 * 构造函数 输入XML的String
	 * @param xmlString
	 */
	public XMLParse(String xmlString) throws Exception {
		SetXmlByString(xmlString);
	}
	
	public void SetXmlByFile(File xmlFile) throws Exception {
		String xmlString = FileToString(xmlFile);
		sBuilder = new SAXBuilder();
		doc = sBuilder.build(new StringReader(xmlString));
	}
	
	public void SetXmlByString(String xmlString) throws Exception {
		sBuilder = new SAXBuilder();
		doc = sBuilder.build(new StringReader(xmlString));
	}
	
	/**
	 * 将File中的信息转到一个String上存储
	 * 使程序在项目根目录中查找DTD
	 */
	private String FileToString(File file) throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader(file));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line + "\n");
		}
		br.close();
		return sb.toString();
	}

	public String GetFirstElememt(String targetName) throws Exception {
		Element root = doc.getRootElement();
		Element element = TraverseForFirstElement(root, targetName);
		if (element == null)
			return "not found";
		return element.getValue();
	}

	public Element TraverseForFirstElement(Element rootElement,
			String targetName) throws Exception {
		if (rootElement.getName() == targetName) {
			return rootElement;
		}
		List<Element> elements = rootElement.getChildren();
		for (int i = 0; i < elements.size(); i++) {
			Element element = TraverseForFirstElement(elements.get(i),
					targetName);
			if (element != null) {
				return element;
			}
		}
		return null;
	}
	
	public String Search(String queryString) throws Exception {
		Element root = doc.getRootElement();
		
		Element element = (Element) XPath.selectSingleNode(root, queryString);
		
		return element.getText();
		
	}

}