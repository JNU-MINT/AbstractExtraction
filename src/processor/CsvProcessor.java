package processor;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

public class CsvProcessor {

	/**
	 * 将数据写入csv文件
	 * @param csvFilePath 文件路径
	 * @param contentList 数据内容
	 */
	public static void writeToCsv(String csvFilePath, ArrayList<String[]> contentList)
			throws Exception {
		try {
			CsvWriter csvWriter = new CsvWriter(csvFilePath, ',',
					Charset.forName("GBK"));
			for (String[] content : contentList) {
				csvWriter.writeRecord(content);
			}
			csvWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从csv文件中读取数据
	 * @param csvFilePath 文件路径
	 * @return 数据内容
	 */
	public static ArrayList<String[]> readFromCsv(String csvFilePath) throws Exception {
		try {
			ArrayList<String[]> csvList = new ArrayList<String[]>();
			CsvReader reader = new CsvReader(csvFilePath, ',',
					Charset.forName("GBK"));
			while(reader.readRecord()){
                csvList.add(reader.getValues());
            }
            reader.close();
            return csvList;
		} catch (Exception e) {
			return new ArrayList<String[]>();
		}
	}
}
