package XMLProcess;

import java.io.File;
import java.util.Date;

import processor.XMLDivider;

public class DivideXMLTest {
	/**
	 * 把一个文件夹下所有大的xml分割成小的xml
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		System.out.println("Divide Begin.");
		XMLDivider xmlFileDivide = new XMLDivider();
		File[] bigXMLFiles;
		bigXMLFiles = xmlFileDivide.getXMLFilesByDir("F:\\patent(F)\\uspatent2014");
		if (bigXMLFiles != null && bigXMLFiles.length > 0) {
			for (File bigXMLFile : bigXMLFiles) {
				String bigXMLPath = bigXMLFile.getAbsolutePath();
				String smallXMLPath = bigXMLFile.getParentFile().getAbsolutePath() + "\\smallxml";
				xmlFileDivide.divide(bigXMLPath, smallXMLPath);
				xmlFileDivide.closeAll();
				xmlFileDivide.deleteFile(bigXMLFile);
				System.out.println(new Date().toLocaleString());
				System.out.println(bigXMLPath + "  done.");
			}
		}
		System.out.println("Divide Finished.");
	}
}