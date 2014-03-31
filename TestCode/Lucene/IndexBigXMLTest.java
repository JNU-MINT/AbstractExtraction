package Lucene;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import Lucene.IndexCreateProcess;
import XMLProcess.XMLFileDivide;



public class IndexBigXMLTest {
	/**
	 * 把一个大的xml文件分割成小的xml，然后做索引
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) {
		Date d1 = new Date();
		long l1 = System.currentTimeMillis();
		XMLFileDivide fp = new XMLFileDivide();
		IndexCreateProcess indexProcess = new IndexCreateProcess(
				"C:\\Users\\qiusd\\Desktop\\index");
		File[] fs;
		try {
			fs = fp.getXMLFilesByDir("C:\\Users\\qiusd\\Desktop\\patent");
			if (fs != null && fs.length > 0)
				for (File fTemp : fs) {
					File[] smallXMLs = fp.divide(fTemp.getAbsolutePath());
					System.out.println("divide finished");
					for (int i = 0; i < smallXMLs.length; i++) {
						indexProcess.addToIndex(smallXMLs[i]);
						System.out.println("add " + smallXMLs[i].toString());
					}
					try {
						fp.closeAll();
					} catch (IOException e) {
						e.printStackTrace();
					}
					indexProcess.closeWriter();
					fp.deleteDir(smallXMLs[0].getParentFile());
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fp.closeAll();
			} catch (IOException e) {
				e.printStackTrace();
			}
			indexProcess.closeWriter();
		}
		Date d2 = new Date();
		long l2 = System.currentTimeMillis();
		System.out.print("cost time: ");
		System.out.println(l2 - l1);
		System.out.println(d2.getTime() - d1.getTime());
	}
}