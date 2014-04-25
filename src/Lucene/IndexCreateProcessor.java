package Lucene;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.ant.DocumentHandlerException;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;




public class IndexCreateProcessor extends DefaultHandler {
	
	private StringBuilder elementBuffer = new StringBuilder();
	private Map<String, String> attributeMap = new HashMap<String, String>();
	private Document doc;
	IndexWriter writer=null;

	/**
	 * 构造函数，初始化索引存储的目标文件夹
	 * @param indexDir 目标文件夹
	 */
	public IndexCreateProcessor (String indexDir){
		try {
			writer = this.getWriter(indexDir);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 解析xml文件的入口，并添加到lucene的Document中
	 * @param xmlFileName xml文件
	 * @return lucene的Document对象
	 * @throws DocumentHandlerException
	 * @throws FileNotFoundException
	 */
	public Document getDocument( File xmlFileName)
			throws DocumentHandlerException, FileNotFoundException {
		InputStream is = new FileInputStream (xmlFileName);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser parser = spf.newSAXParser();
			parser.parse(is, this);
		} catch (Exception e) {
			throw new DocumentHandlerException(e);
		}
		doc.add(new Field("fileName", xmlFileName.getName(), Field.Store.YES,
						Field.Index.NOT_ANALYZED));
		return doc;
	}

	public void startDocument() {
		doc = new Document();
	}

	
	public void startElement(String uri, String localName,
			String qName, Attributes atts) throws SAXException {
		elementBuffer.setLength(0);
		attributeMap.clear();
		int numAtts = atts.getLength();
		if (numAtts > 0) {
			for (int i = 0; i < numAtts; i++) {
				attributeMap.put(atts.getQName(i), atts.getValue(i));
			}
		}
	}

	public void characters(char[] text, int start, int length) {
		elementBuffer.append(text, start, length);
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		//TODO:搞清楚这里的代码
		if (qName.equals("us-patent-grant")) {
			return;
		}else {
			doc.add(new Field(qName, elementBuffer.toString(), Field.Store.NO,
					Field.Index.ANALYZED));
		}
	}

	/**
	 * 把某个文件加入索引
	 * @param xmlFile 目标文件
	 */
	public void addToIndex(File xmlFile){
		try {
			doc = this.getDocument(xmlFile);
			writer.addDocument(doc);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentHandlerException e) {
			e.printStackTrace();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 生成indexWriter
	 * @param path 索引保存的文件夹
	 * @return indexWriter对象
	 * @throws Exception
	 */
	public IndexWriter getWriter(String path) throws Exception{
		Directory dir=FSDirectory.open(new File(path));
		IndexWriterConfig iwConfig=new IndexWriterConfig(Version.LUCENE_36,
				new StandardAnalyzer(Version.LUCENE_36));
		//追加？
		iwConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
		IndexWriter iWriter=new IndexWriter(dir, iwConfig);
		return iWriter;
	}

	/**
	 * 关闭写索引的资源
	 */
	public void closeWriter(){
		try {
			if(this.writer != null)
				this.writer.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
