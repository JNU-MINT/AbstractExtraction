package Extraction;

import java.util.ArrayList;
import java.util.List;
import edu.stanford.nlp.process.DocumentPreprocessor;

public class StanfordSentenceDetector {

	public static String[] split(String docPath) {
		
		DocumentPreprocessor dp = new DocumentPreprocessor(docPath);
		
		ArrayList<String> list = new ArrayList<String>();
		
		for (List sentence : dp) {
			StringBuilder sb = new StringBuilder();
			for(Object word : sentence)
			{
				sb.append(word.toString() + " ");
			}
			list.add(sb.toString());
		}
		String[] str = new String[list.size()];
		list.toArray(str);
		
		System.out.println("The sentences:");
		for(int i = 0; i < list.size(); i++)
		{
			System.out.println((i+1) + ". " + str[i]);
		}
		
		return str;
	}
}
