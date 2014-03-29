package SentenceExtraction;

import java.util.ArrayList;
import java.util.List;
import edu.stanford.nlp.process.DocumentPreprocessor;

/**
 * Stanford断句器
 * @author qiusd
 */
public class SentenceDetector {
	/**
	 * 输入文本路径
	 * 输出断句后的String数组
	 */
	public static String[] split(String docPath) {
		DocumentPreprocessor dp = new DocumentPreprocessor(docPath);
		ArrayList<String> list = new ArrayList<String>();
		for (List sentence : dp) {
			StringBuilder sb = new StringBuilder();
			for (Object word : sentence) {
				sb.append(word.toString() + " ");
			}
			list.add(sb.toString());
		}
		String[] strArray = new String[list.size()];
		list.toArray(strArray);
		System.out.println("The sentences:");
		for (int i = 0; i < list.size(); i++) {
			System.out.println((i + 1) + ". " + strArray[i]);
		}
		return strArray;
	}
}
