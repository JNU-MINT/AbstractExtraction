package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * 专利文献类
 * @author qiusd
 */
public class Patent {
	
	//文件名
	public String fileName;
	
	//文件路径
	public String filePath;
	
	//在Lucene搜索中的相关度
	public double relatedScore;
	
	//发明人，代理方，专利引用信息
	//目前没用到
	public ArrayList<String> inventorList = new ArrayList<String>();
	public ArrayList<String> assigneeList = new ArrayList<String>();
	public ArrayList<String> citedPatentList = new ArrayList<String>();
	
	//xml文件中的addressBook信息
	public Set<String> addressBookSet = new HashSet<String>();
	
	//链接专利文献的标题集合
	public Set<ArrayList<String>> linkTitleSet = new HashSet<ArrayList<String>>();
}
