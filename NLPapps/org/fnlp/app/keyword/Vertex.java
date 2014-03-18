package org.fnlp.app.keyword;

import java.util.ArrayList;

public class Vertex {
	public String id;
	public int index;
	private int forwardCount = 0;
	private ArrayList<Vertex> next = null;
	private ArrayList<Double> wNext = null;
	
	public Vertex(String id){
		this.id = id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getId(){
		return id;
	}
	
	public void addVer(Vertex ver, double weight){
		if(next == null){
			next = new ArrayList<Vertex>();
			wNext = new ArrayList<Double>();
		}
		next.add(ver);
		wNext.add(weight);
	}
	
	public ArrayList<Vertex> getNext(){
		return next;
	}
	
	public ArrayList<Double> getWNext(){
		return wNext;
	}
	
	public void setWNext(int index, double wAdd){
		double w = wNext.get(index);
		wNext.set(index, w + wAdd);
	}
	
	public void setIndex(int index){
		this.index = index;
	}
	
	public void addForwardCount(int wAdd){
		forwardCount += wAdd;
	}
	
	public int getForwardCount(){
		return forwardCount;
	}
	
	public String vertexToString(){
		String s = id+ " " + String.valueOf(index)+ " " + String.valueOf(forwardCount);//+ " " + next.toString();
		return s;
	}
}