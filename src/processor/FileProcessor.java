package processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileProcessor {

	/**
	 * 查找文件夹中的所有文件
	 * @param dirPath 文件夹路径
	 * @return 文件夹中的文件列表
	 */
	public List<String> getFilesByDir(String dirPath)
			throws FileNotFoundException, IOException {
		try {
			File file = new File(dirPath);
			if (!file.isDirectory()) {
				System.out.println("输入的参数应该为[文件夹名]");
				System.out.println("filepath: " + file.getAbsolutePath());
			} else if (file.isDirectory()) {
				String[] fileAndDirList = file.list();
				List<String> fileList = new ArrayList<String>();
				for (int i = 0; i < fileAndDirList.length; i++) {
					File readfile = new File(dirPath + "\\" + fileAndDirList[i]);
					if (!readfile.isDirectory()) {
						fileList.add(readfile.getAbsolutePath());
					} else if (readfile.isDirectory()) {
						getFilesByDir(dirPath + "\\" + fileAndDirList[i]);
					}
				}
				return fileList;
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	/**
	 * 读取文本文件内容
	 * @param filePath 文件路径
	 * @return 文件内容
	 */
	public String readFiles(String filePath) throws FileNotFoundException,
			IOException {
		StringBuffer sb = new StringBuffer();
		InputStreamReader is = new InputStreamReader(new FileInputStream(
				filePath), "gbk");
		BufferedReader br = new BufferedReader(is);
		String line = br.readLine();
		while (line != null) {
			sb.append(line).append("\r\n");
			line = br.readLine();
		}
		br.close();
		return sb.toString();
	}

}
