package Lucene;

import java.io.File;
import java.io.IOException;

import Lucene.IndexCreateProcess;
import XMLProcess.XMLFileDivide;


public class IndexSmallXMLTest {
	/**
	 * 测试获取若干小的xml文件建索引 直接对已经分割好的小的xml做索引
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		long l1 = System.currentTimeMillis();
		XMLFileDivide fp = new XMLFileDivide();
		IndexCreateProcess indexProcess = new IndexCreateProcess(
				"C:\\Users\\qiusd\\Desktop\\index");
		try {
			File[] smallXMLs = fp
					.getXMLFilesByDir("C:\\Users\\qiusd\\Desktop\\patent\\新建文件夹");
			for (int i = 0; i < smallXMLs.length; i++) {
				indexProcess.addToIndex(smallXMLs[i]);
			}
			try {
				fp.closeAll();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 需要关闭写索引的资源，才能完成索引操作
			indexProcess.closeWriter();
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
		long l2 = System.currentTimeMillis();
		System.out.println((l2 - l1) / 1000.0 + "s");
	}
}
