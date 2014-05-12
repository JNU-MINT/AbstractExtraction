package processor;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.xpath.XPath;

@SuppressWarnings("deprecation")
public class XMLParser {

	StringBuilder stringBuilder;
	SAXBuilder saxBuilder;
	Document doc;

	public XMLParser() {
	}

	/**
	 * 构造函数 输入XML文件
	 * 
	 * @param xmlFile
	 */
	public XMLParser(File xmlFile) throws Exception {
		setXmlByFile(xmlFile);
	}

	/**
	 * 构造函数 输入XML的String
	 * 
	 * @param xmlString
	 */
	public XMLParser(String xmlString) throws Exception {
		setXmlByString(xmlString);
	}

	public void setXmlByFile(File xmlFile) throws Exception {
		String xmlString = fileToString(xmlFile);
		saxBuilder = new SAXBuilder();
		doc = saxBuilder.build(new StringReader(xmlString));
	}

	public void setXmlByString(String xmlString) throws Exception {
		saxBuilder = new SAXBuilder();
		doc = saxBuilder.build(new StringReader(xmlString));
	}

	/**
	 * 将File中的信息转到一个String上存储 使程序在项目根目录中查找DTD
	 * TODO: 需要和原本的处理函数合并
	 */
	private String fileToString(File file) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(file));
		stringBuilder = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			stringBuilder.append(line + "\n");
		}
		br.close();
		return stringBuilder.toString();
	}

	/**
	 * 自己实现的首元素提取 输入为当级标签
	 * 
	 */
	public String getFirstElememt(String targetName) throws Exception {
		Element root = doc.getRootElement();
		Element element = traverseForFirstElement(root, targetName);
		if (element == null)
			return null;
		return element.getValue();
	}

	/**
	 * 递归遍历函数
	 */
	public Element traverseForFirstElement(Element rootElement,
			String targetName) throws Exception {
		if (rootElement.getName() == targetName) {
			return rootElement;
		}
		List<Element> elements = rootElement.getChildren();
		for (int i = 0; i < elements.size(); i++) {
			Element element = traverseForFirstElement(elements.get(i),
					targetName);
			if (element != null) {
				return element;
			}
		}
		return null;
	}

	/**
	 * XPath实现首元素提取
	 * 输入可为完整或部分标签路径
	 */
	public String getFirstNode(String queryString) throws Exception {
		Element root = doc.getRootElement();
		Element element = (Element) XPath.selectSingleNode(root, queryString);
		if (element != null) {
			return element.getText();
		}
		return "";
	}
	
	public ArrayList<String> SelectAllNodes(String queryString) throws Exception {
		Element root = doc.getRootElement();
		ArrayList<Element> elementList = (ArrayList<Element>) XPath.selectNodes(root, queryString);
		ArrayList<String> textList = new ArrayList<String>();
		for (Element element : elementList) {
			textList.add(element.getText());
		}
		return textList;
	}
	

}