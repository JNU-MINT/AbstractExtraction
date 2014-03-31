package Lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.FieldInfos;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.ReaderUtil;
import org.apache.lucene.util.Version;

public class IndexSearchProcess {

	IndexReader ir = null;
	IndexSearcher is = null;
	Directory id = null;

	public IndexSearchProcess(String path) throws CorruptIndexException,
			IOException {
		id = FSDirectory.open(new File(path));
		ir = IndexReader.open(id);
		is = new IndexSearcher(ir);
	}

	/**
	 * 列出某个indexReader里面的所有field的name
	 * 
	 * @return fieldName的数组
	 */
	public String[] listFieldName() {
		FieldInfos fInfos = ReaderUtil.getMergedFieldInfos(ir); // ir.getFieldInfos();
		Iterator<FieldInfo> iterator = fInfos.iterator();
		List<String> fieldList = new ArrayList<String>();
		while (iterator.hasNext()) {
			// FieldInfo fInfo = iterator.next();
			fieldList.add(iterator.next().name);
		}
		return fieldList.toArray(new String[0]);
	}

	// 搜索
	public void search(String field, String word) throws Exception {
		QueryParser queryParser = new QueryParser(Version.LUCENE_36, field,
				new StandardAnalyzer(Version.LUCENE_36));
		Query q = queryParser.parse(word);
		TopDocs topDocs = is.search(q, Integer.MAX_VALUE);
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		int scoreDocsLength = scoreDocs.length;
		System.out.println("共有" + topDocs.totalHits);
		System.out.println("共有" + scoreDocsLength + "条记录");

		if (scoreDocs != null) {
			for (ScoreDoc sdTemp : scoreDocs) {
				Document docTemp = ir.document(sdTemp.doc);
				System.out.println(docTemp.get("fileName"));
			}
		}
		this.closeAll();

		// Document doc=null;
		// Fieldable fieldable =null;
		// if(scoreDocsLength > 0){
		// for (int i=0;i<scoreDocsLength;i++){
		// List listFields=doc.getFields();
		// Object [] objectFields=listFields.toArray();
		// System.out.println("通过函数 getFields()  得到的结果:");
		// for (int k=0;k<objectFields.length;k++){
		// System.out.println(objectFields[k].toString()+" 转化后  "+((Fieldable)objectFields[k]).stringValue());
		// }
		// System.out.println("\n----------------");
		// }
		// }
		// else{
		// System.out.println("未找到相关记录");
		// }
		// this.closeAll();
	}

	/**
	 * 多field搜索
	 */
	public void searchInMultiFields(String[] fieldsForSearch,
			String wordForSearch) throws ParseException, IOException {
		MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(
				Version.LUCENE_36, fieldsForSearch, new StandardAnalyzer(
						Version.LUCENE_36));
		Query query = multiFieldQueryParser.parse(wordForSearch);
		TopDocs topDocs = is.search(query, Integer.MAX_VALUE);
		System.out.println("共有" + topDocs.totalHits + "条记录");
		// ScoreDoc [] sd=topDocs.scoreDocs;
		// // if(sd!=null){
		// int l=sd.length;
		// System.out.println("共有"+topDocs.totalHits);
		//
		// System.out.println("共有"+l+"条记录");
		// Document doc=null;
		// Fieldable fieldable =null;
		// if(l>0){
		// for (int i=0;i<l;i++){
		// doc=ir.document(sd[i].doc);
		//
		// List<Fieldable> listFields=doc.getFields();
		// Object [] objectFields=listFields.toArray();
		//
		// System.out.println("通过函数 getFields()  得到的结果:");
		// for (int k=0;k<objectFields.length;k++){
		// System.out.println(objectFields[k].toString()+" 转化后  "+((Fieldable)objectFields[k]).stringValue());
		// }
		// System.out.println("\n----------------");
		// }
		// }
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		if (scoreDocs != null) {
			for (ScoreDoc sdTemp : scoreDocs) {
				Document docTemp = ir.document(sdTemp.doc);
				System.out.println(docTemp.get("fileName"));
			}
		}
		this.closeAll();
	}

	/**
	 * 从所有field范围检索
	 * 
	 * @param wordForSearch
	 * @throws ParseException
	 * @throws IOException
	 */
	public void searchInAllFields(String wordForSearch) throws ParseException,
			IOException {
		this.searchInMultiFields(this.listFieldName(), wordForSearch);
	}

	// 分页查询
	// public void searchInPages() throws IOException{
	// Term t=new Term("mark", "国");
	// Query q=new TermQuery(t);
	// TopDocs td=is.search(q, 5);
	// ScoreDoc [] sd=td.scoreDocs;
	// // if(sd!=null){
	// int l=sd.length;
	// System.out.println("共有"+td.totalHits);
	//
	// System.out.println("共有"+l+"条记录");
	// Document doc=null;
	// Fieldable fieldable =null;
	// if(l>0){
	// for (int i=0;i<l;i++){
	// doc=ir.document(sd[i].doc);
	//
	// System.out.println(doc.getFieldable("name"));
	// } //for
	// }else{
	// System.out.println("未找到相关记录");
	// }//if
	//
	// System.out.println("\n---------========-------");
	//
	// ScoreDoc sdoc = td.scoreDocs[4];
	// // ScoreDoc sdoc = sd[4];
	// td=is.searchAfter(sdoc, q, 3);
	// sd=td.scoreDocs;
	// for (int i=0;i<3;i++){
	// doc=ir.document(sd[i].doc);
	//
	// System.out.println(doc.getFieldable("name"));
	// }
	// id.close();
	// ir.close();
	// is.close();
	// }

	public void closeAll() {
		try {
			if (id != null)
				id.close();
			if (ir != null)
				ir.close();
			//这个Lucene4.0没有了
			//网上说可以忽略
//			if (is != null)
//				is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
