package model;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;


/**
 * 待处理文本类
 * @author qiusd
 */
public class Document {
	
	//标题单词
	public ArrayList<String> titleWords = new ArrayList<String>();
	
	//摘要文本
	public String abstractString;
	//摘要单词
	//含特征值信息和类别
	public ArrayList<Word> abstractWords = new ArrayList<Word>();
	
	//查询词
	public Set<String> queryWordSet = new TreeSet<String>();
	
	//相关文件
	public ArrayList<Patent> relatedPatents = new ArrayList<Patent>();
}
