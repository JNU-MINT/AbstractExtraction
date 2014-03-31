package org.fnlp.app.keyword;

import java.util.Map;

import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.corpus.StopWords;

public abstract class AbstractExtractor {

	protected CWSTagger cWSTagger;
	protected StopWords stopWords;
	/**
	 * 权重收敛的默认阈值
	 * 原程序为0.001
	 */
	protected double precision = 0.0001;//权重收敛的默认阈值
	/**
	 * 阻尼参数
	 */
	protected double dN = 0.85;  //阻尼参数
	/**
	 * 窗体大小
	 * 只对于原代码中的WordExtract
	 * 对后来写的SentenceExtract无用
	 */
	protected int windowN = 10; //窗体大小
	
	/**
	 *设置窗口大小 和阻尼系数
	 */
	public void setN(int windowN,double dN){
		this.windowN = windowN;
		this.dN = dN;
	}

	/**
	 * 将权重收敛的阈值设小
	 * 算出来的关键词更精确
	 */
	public void setPrecisionHigh() {
		this.precision = 0.000000001;
	}

	/**
	 * 将权重收敛的阈值设大
	 * 算出来的关键词粗糙，但速度更快
	 */
	public void setPrecisionLow() {
		this.precision = 0.1;
	}

	/**
	 * 将权重收敛的阈值设为默认
	 */
	public void setPrecisionDefault() {
		this.precision = 0.0001;
	}

	/**
	 * 
	 * @param precision
	 *       权重收敛的阈值
	 */
	public void setPrecision(double precision){
		this.precision = precision;
	}

	/**
	 * 这个目前没用到
	 */
	abstract public String extract(String str, int num, boolean isWeighted);
	/**
	 * 按数量选取
	 */
	abstract public Map<String,Integer> extract(String readFile, int i);
	
}
