package org.fnlp.app.keyword;

import java.util.ArrayList;

/**
 * 注释添加
 * @author qiusd
 */
public class Graph {
	private ArrayList<Vertex> vertexList = new ArrayList<Vertex>();
	
	
	private int nVerts = 0;
	
	public ArrayList<Vertex> getVertexList(){
		return vertexList;
	}
	
	/**
	 * 获得图中顶点个数
	 */
	public int getNVerts(){
		return nVerts;
	}
	
	/**
	 * 根据index
	 * 获得图中的某个顶点
	 */
	public Vertex getVertex(int index){
		return vertexList.get(index);
	}
	
	
	/**
	 * 根据id
	 * 获得图中的某个顶点
	 */
	public int getIndex(String id){
		int index;
		for(index = 0; index < nVerts; index++)
			if(vertexList.get(index).getId() == id)
				break;
		if(index == nVerts)
			index = -1;
		return index;
	}
	
	/**
	 * 加顶点
	 */
	public void addVertex(Vertex vertex){
		vertex.setIndex(nVerts);
		vertexList.add(vertex);
		nVerts++;
	}
	
	
	/**
	 * 删顶点
	 * 只删除了graph中的结点信息
	 * 外部的信息无法删除
	 * @author qiusd
	 */
	public void deleteVertex(int index) {
		vertexList.remove(index);
		nVerts--;
	}
	
	
	/**
	 * 加边
	 */
	public void addEdge(int start, int end, double weight){
		Vertex vertexStart = vertexList.get(start);
		Vertex vertexEnd = vertexList.get(end);
		if(vertexStart.getNext() != null)
		{
			int index = vertexStart.getNext().indexOf(vertexEnd);
			//如果两个顶点已经有边相连，增加权值weight
			if(index != -1)
			{
				vertexStart.setWNext(index, weight);
			}
			//如果还没有，则添加边，权值为weight
			else
			{
				vertexStart.addVer(vertexEnd, weight);
			}
		}
		//如果vertex1还没有后续结点
		else
			vertexList.get(start).addVer(vertexList.get(end), weight);
		vertexList.get(end).addForwardCount(1);
	}
}
