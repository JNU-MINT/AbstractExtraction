package Lucene;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import processor.XMLDivider;

import Lucene.IndexCreateProcessor;

public class IndexSmallXMLTest {
	/**
	 * 对某文件夹中已经分割好的小的xml做索引
	 */
	@SuppressWarnings("deprecation")
	//2009,2010,2011,2012,2013
	public static void main(String[] args) {
		XMLDivider fp = new XMLDivider();
		IndexCreateProcessor indexProcess = new IndexCreateProcessor(
				"F:\\patent(F)\\index");
		try {
			File[] smallXMLs = fp
					.getXMLFilesByDir("G:\\patent(G)\\uspatent2012\\smallxml");
			
			
			System.out.println(new Date().toLocaleString());
			System.out.println("get all xml file done");
			for (int i = 0; i < smallXMLs.length; i++) {
				indexProcess.addToIndex(smallXMLs[i]);
				if (i % 1000 == 0)
				{
					System.out.println(new Date().toLocaleString());
					System.out.println("have indexed" + i);
				}
			}
			
			File[] smallXMLs1 = fp
					.getXMLFilesByDir("G:\\patent(G)\\uspatent2013\\smallxml");
			
			System.out.println(new Date().toLocaleString());
			System.out.println("get all xml file done");
			for (int i = 0; i < smallXMLs1.length; i++) {
				indexProcess.addToIndex(smallXMLs1[i]);
				if (i % 1000 == 0)
				{
					System.out.println(new Date().toLocaleString());
					System.out.println("have indexed" + i);
				}
			}
			
			
			try {
				fp.closeAll();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
		System.out.println("all done");
	}
}
