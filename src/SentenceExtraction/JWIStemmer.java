package SentenceExtraction;

import java.io.*;
import java.net.*;
import java.util.List;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.morph.WordnetStemmer;

/**
 * 利用WordNet java interface查找词干
 * 
 * @author qiusd
 */
public class JWIStemmer {
	
	WordnetStemmer wns;
	
	public JWIStemmer() throws Exception{
		String wnhome = System.getenv("WNHOME");
		String path = wnhome + File.separator + "dict";
		URL url = new URL("file", null, path);
		IDictionary dict = new Dictionary(url);
		dict.open();
		wns = new WordnetStemmer(dict);
	}
	
	public List<String> FindAllStems(String word) {
		return wns.findStems(word, null);
	}
	
	public String FindFirstStem(String word) {
		List<String> stemList = wns.findStems(word, null);
		if(!stemList.isEmpty())
			return stemList.get(0);
		return null;
	}
	
	
}
