package com.chenrui.xmlModel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 *xml文档的内容
 * */
public class Document {
	
	private Node root ;
	
	public Document() {
		root = new Node();
		root.setTypeOfNode(TypeOfNode.SIGN);
		root.setName("xml");
	}

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}
	
	/**
	 * 根据标签的姓名获得节点
	 * 这里采用遍历的方式
	 * */
	public Node getNodeByTagName(String tagName) {
		return root.getChildNode(tagName);
	}
	
	/**
	 * 先序遍历
	 * */
	public void preTravel() {
		Node.preTravel(root);
	}
	
	/**
	 * 将节点名为tagName的节点转换为一个对象
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws NoSuchFieldException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * */
	public Object getEntity(String tagName,Class clazz,String[] mapList) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, NoSuchFieldException, IllegalArgumentException, InvocationTargetException {
		Object object = clazz.newInstance();
		Method method = null;
		String nameOfMethod = "";
		Node n = root.getChildNode(tagName);
		ArrayList<Node> childNodes = n.getChilds();
		for(int i = 0 ; i < mapList.length; i++) {
			Node node = childNodes.get(i);
			String attr = mapList[i];
			String firstLetter = attr.substring(0,1);
			nameOfMethod = "set"+firstLetter.toUpperCase()+attr.substring(1);
			Field filed = clazz.getDeclaredField(attr);
			Class type = filed.getType();
			method = clazz.getMethod(nameOfMethod,new Class[]{type});
			if(type == int.class) {
				 int attribute = Integer.parseInt(node.getAttr());
				 method.invoke(object,attribute);
			}
			else {
				String attributeS = node.getAttr();
				method.invoke(object,attributeS);
			}
		}
		return object;
	}
	
	
	
}










