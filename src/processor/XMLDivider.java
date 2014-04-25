/**
 * Copyright 1998-2013,2013 南方人才网
 * 文件名： DivideToSmall.java
 * JDK版本： 1.6.0_10
 * 作者： yf
 * 日期： 上午08:41:35
 * 描述：账号Service类
 * 
 */

package processor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.jdom2.xpath.XPath;

/**
 * 功能：切割大XML文件
 * 
 * @author qiusd
 */
public class XMLDivider {

	BufferedReader br;
	BufferedWriter bw;

	/**
	 * 把一个包含很多个xml文件 完整内容的文件分为单独的xml文件 保存在文件名同名的文件夹下面 TODO:代码风格不好,考虑重构
	 * 
	 * @param bigXMLPath
	 *            目标xml文件的完整路径名
	 * @return 分割完后的各个小的xml文件
	 */
	public File[] divide(String bigXMLPath, String smallXMLPath)
			throws Exception {
		File bigXmlFile = new File(bigXMLPath);
		ArrayList<File> fileList = new ArrayList<File>();
		br = new BufferedReader(new FileReader(bigXmlFile));
		File xmlDir = new File(smallXMLPath);
		xmlDir.mkdirs();
		String line;
		String headLine = new String(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		File fileOutput;
		int fileCount = 0;
		StringBuilder sb = new StringBuilder();
		while (true) {
			line = br.readLine();
			if (line == null || line.contains(headLine)) {
				if (fileCount > 0) {
					String xmlPara = null;
					try {
						xmlPara = GetXmlPara(sb.toString(), fileCount);
					} catch (Exception e) {
						System.out.println(sb.toString() + "  "
								+ String.valueOf(fileCount));
					}
					String fileName = xmlDir.getAbsolutePath() + "/" + xmlPara;
					String suffix = ".xml";
					int id = 1;
					if (!(new File(fileName + suffix).exists())) {
						fileOutput = new File(fileName + suffix);
					} else {
						while ((fileOutput = new File(xmlDir.getAbsolutePath()
								+ "\\addition" + "-" + String.valueOf(id) + "-"
								+ xmlPara + suffix)).exists()) 
						{
							id++;
						}
					}
					bw = new BufferedWriter(new FileWriter(fileOutput));
					bw.write(sb.toString());
					bw.flush();
					sb = new StringBuilder();
					fileList.add(fileOutput);
				}
				fileCount++;
			}
			if (line == null)
				break;
			sb.append(line + "\n");
		}
		return fileList.toArray(new File[0]);
	}

	private String GetXmlPara(String xmlString, int fileCount) throws Exception {
		XMLParser xmlParse = new XMLParser();
		xmlParse.setXmlByString(xmlString);
		StringBuilder sb = new StringBuilder();
		String paraString = xmlParse
				.getFirstNode("//publication-reference/document-id/country")
				+ "-";
		if (paraString.isEmpty())
			return String.valueOf(fileCount);
		sb.append(paraString);

		paraString = xmlParse
				.getFirstNode("//publication-reference/document-id/doc-number")
				+ "-";
		if (paraString.isEmpty())
			return String.valueOf(fileCount);
		sb.append(paraString);

		paraString = xmlParse
				.getFirstNode("//publication-reference/document-id/kind");
		if (paraString.isEmpty())
			return String.valueOf(fileCount);
		sb.append(paraString);
		
		return sb.toString();
	}

	/**
	 * 返回某个文件夹下面的所有xml文件
	 * 
	 * @param dirPath
	 *            目标文件夹
	 * @return xml文件数组
	 * @throws Exception
	 */
	public File[] getXMLFilesByDir(String dirPath) throws Exception {
		File fatherDir = new File(dirPath);
		if (!fatherDir.exists() || !fatherDir.isDirectory())
			throw new Exception("文件夹路径错误");
		return fatherDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				boolean accept = false;
				if (name.toLowerCase().endsWith("." + "xml")) {
					accept = true;
				}
				return accept;
			}
		});
	}

	/**
	 * 删除文件
	 * 
	 * @param file
	 *            文件
	 */
	public void deleteFile(File file) {
		if (file.exists() && file.isFile()) {
			try {
				System.gc();
				file.delete();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 删除某个文件夹及其下面的内容 其实也具有删除单个文件的功能. TODO：考虑能不能把两个功能合并
	 * 
	 * @param dir
	 *            目标文件夹
	 */
	public void deleteDir(File dir) {
		if (dir.exists() && dir.isDirectory()) {
			File[] subFiles = dir.listFiles();
			if (subFiles != null && subFiles.length > 0) {
				for (File fTemp : subFiles) {
					if (fTemp.isFile())
						deleteFile(fTemp);
					else
						deleteDir(fTemp);
				}
			}
		}
		System.out.println("删除全部文件完成");
		System.gc();
		dir.delete();
		System.out.println("删除文件夹完成");
	}

	/**
	 * 删除和某文件同名的文件夹
	 * 
	 * @param file
	 */
	public void deleteDirOfSameName(File file) {
		File dir = null;
		if (file.getAbsolutePath().lastIndexOf('.') > 0)
			dir = new File(file.getAbsolutePath().substring(0,
					file.getAbsolutePath().lastIndexOf('.')));
		if (dir.exists() && dir.isDirectory())
			this.deleteDir(dir);
	}

	/**
	 * 关闭缓存读取和写入资源
	 * 
	 * @throws IOException
	 */
	public void closeAll() throws IOException {
		if (br != null)
			br.close();
		if (bw != null)
			bw.close();
	}

}
