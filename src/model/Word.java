package model;


/**
 * 关键词候选词类
 * @author qiusd
 */
public class Word {
	
	//单词
	public String text;
	
	//链接特征值
	public double linkFeature;
	
	//tfidf值
	public double tfidf;
	
	//位置特征值
	public double position;
	
	//词长特征值
	public double wordLength;
	
	//TextRank排名
	
	public double textRank;
	
	//textRank数值
	
	public double textRankValue;
	
	
	//类标号
	//1为是，0为否
	//考虑用别的数据类型代替
	public double lable;
	
	//预测值
	public double prediction;
}
