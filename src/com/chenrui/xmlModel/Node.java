package com.chenrui.xmlModel;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * xml文档的节点
 * */
public class Node {

	public Node() {
		
	}
	
	public Node(String name) {
		this.name = name;
	}
	
	private TypeOfNode TypeOfNode;
	
	//这个是节点的名字
	private String name;
	
	//当TypeOfNode为ATTR的时候，这个属性有值
	private String attr;
	
	//节点的子节点
	private ArrayList<Node> childs = new ArrayList<Node>();

	public TypeOfNode getTypeOfNode() {
		return TypeOfNode;
	}

	public void setTypeOfNode(TypeOfNode TypeOfNode) {
		this.TypeOfNode = TypeOfNode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public  ArrayList<Node> getChilds() {
		return childs;
	}

	public void setChilds( ArrayList<Node> childs) {
		this.childs = childs;
	}

	public void addChile(Node node){
		childs.add(node);
	}
	
	
	/**
	 * 先序遍历的方式，找到相应名字的node
	 * */
	public Node getChildNode(String tagName) {
		if(tagName == null || tagName.equals("\0")) {
			return null;
		}
		else {
			if(tagName.equals(this.name)) {
				return this;
			}
			else {
				//遍历孩子结点
				int k = 0;
				while(k < childs.size()) {
					Node node = childs.get(k).getChildNode(tagName);
					if(node != null) {
						return node;
					}
					k++;
				}
			}
		}
		return null;
	}
	
	
	public static void preTravel(Node node) {
		System.out.println(node.getName()); 
		int numOfChild = node.getChilds().size();
		for(int i = 0; i < numOfChild;i++) {
			preTravel(node.getChilds().get(i));
		}
	}
}
