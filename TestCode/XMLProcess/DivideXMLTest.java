package XMLProcess;

import java.io.File;

import XMLProcess.XMLFileDivide;

public class DivideXMLTest {
	/**
	 * 把一个文件夹下所有大的xml分割成小的xml
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("Divide Begin.");
		XMLFileDivide fp = new XMLFileDivide();
		File[] fs;
		fs = fp.getXMLFilesByDir("C:\\Users\\qiusd\\Desktop\\patent");
		if (fs != null && fs.length > 0) {
			for (File fTemp : fs) {
				fp.divide(fTemp.getAbsolutePath());
			}
		}
		System.out.println("Divide Finished.");
	}
}