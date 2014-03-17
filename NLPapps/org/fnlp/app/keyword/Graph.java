package org.fnlp.app.keyword;

import java.util.ArrayList;

/**
 * @author qiusd
 *
 */
/**
 * @author qiusd
 *
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
	
	public Vertex getVertex(int index){
		return vertexList.get(index);
	}
	
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
	 * 加边
	 */
	public void addEdge(int start, int end, double weight){
		Vertex vertex1 = vertexList.get(start);
		Vertex vertex2 = vertexList.get(end);
		if(vertex1.getNext() != null)
		{
			int index = vertex1.getNext().indexOf(vertex2);
			//如果两个顶点已经有边相连，增加权值weight
			if(index != -1)
			{
				vertex1.setWNext(index, weight);
			}
			//如果还没有，则添加边，权值为weight
			else
				vertex1.addVer(vertex2, weight);
		}
		//如果vertex1还没有后续结点
		else
			vertexList.get(start).addVer(vertexList.get(end), weight);
		vertexList.get(end).addForwardCount(1);
	}
}
