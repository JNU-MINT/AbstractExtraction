package Lucene;

import Lucene.IndexSearchProcess;

public class SearchTest {

	public static void main(String[] args) throws Exception {
		IndexSearchProcess st = new IndexSearchProcess(
				"C:\\Users\\qiusd\\Desktop\\index");
//		String[] fields = {"last-name"};
//		st.searchInMultiFields(fields, "Wesinger, Jr. Ralph E.");
//		st.search("kind", "P2");
		st.searchInAllFields("Pleasant Garden");
	}
}
