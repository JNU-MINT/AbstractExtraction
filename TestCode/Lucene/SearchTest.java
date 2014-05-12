package Lucene;

import Lucene.SearchProcessor;

public class SearchTest {

	public static void main(String[] args) throws Exception {
		SearchProcessor st = new SearchProcessor(
				"F:\\patent(F)\\index");
//		String[] fields = {"last-name"};
//		st.searchInMultiFields(fields, "Wesinger, Jr. Ralph E.");
		//st.search("date", "20111227");
		st.searchInAllFields("us-patent-grant", Integer.MAX_VALUE);
	}
}