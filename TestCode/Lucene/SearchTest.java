package Lucene;

import Lucene.IndexSearchProcess;

public class SearchTest {

	public static void main(String[] args) throws Exception {
		IndexSearchProcess st = new IndexSearchProcess(
				"F:\\patent(F)\\index");
//		String[] fields = {"last-name"};
//		st.searchInMultiFields(fields, "Wesinger, Jr. Ralph E.");
		//st.search("date", "20111227");
		System.out.println(st.searchInAllFields("guangzhou", Integer.MAX_VALUE));
	}
}