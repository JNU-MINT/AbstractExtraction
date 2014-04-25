package SentenceExtraction;

import java.io.*;
import java.net.*;
import java.util.List;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.morph.WordnetStemmer;

/**
 * 利用WordNet java interface词干进行词干提取
 * 具体概念维基百科“词干提取”
 * 
 * @author qiusd
 */
public class JWIStemmer {
	private WordnetStemmer wns;

	public JWIStemmer() throws Exception {
		String wnhome = System.getenv("WNHOME") + "\\2.1";
		String path = wnhome + File.separator + "dict";
		URL url = new URL("file", null, path);
		IDictionary dict = new Dictionary(url);
		dict.open();
		wns = new WordnetStemmer(dict);
	}

	public List<String> getAllStems(String word) {
		return wns.findStems(word, null);
	}

	public String getFirstStem(String word) {
		List<String> stemList = wns.findStems(word, null);
		if (!stemList.isEmpty())
			return stemList.get(0);
		return word;
	}

}
