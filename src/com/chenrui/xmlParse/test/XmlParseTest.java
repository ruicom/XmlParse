package com.chenrui.xmlParse.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.print.DocFlavor.URL;

import org.junit.Test;

import com.chenrui.xmlModel.Document;
import com.chenrui.xmlModel.Node;
import com.chenrui.xmlModel.Server;
import com.chenrui.xmlParse.XmlParse;

public class XmlParseTest {

	@Test
	public void testGetch() throws IOException {
		XmlParse xmlParse = new XmlParse("test.xml");
		assertEquals('<',xmlParse.getch());
	}

	@Test
	public void testGetch1() throws IOException  {
		XmlParse xmlParse =  new XmlParse("test.xml");
		char c = xmlParse.getch();
		while(c != (char)-1) {
			//去除空格,和制表符等
			if(('\0' == c)||('\t' == c)) {	
			}
			else {
				System.out.print(c);
			}
			c = xmlParse.getch();
		}
	}
	
	/**
	 * 词法分析测试
	 * */
	@Test 
	public void testGetSym() throws FileNotFoundException  {
		XmlParse xmlParse =  new XmlParse("test.xml");
		try {
			xmlParse.getSym();
			System.out.println(xmlParse.getSyms());
			System.out.println(xmlParse.getId());
			xmlParse.getSym();
			System.out.println(xmlParse.getSyms());
			System.out.println(xmlParse.getId());
			xmlParse.getSym();
			System.out.println(xmlParse.getSyms());
			System.out.println(xmlParse.getId());
			xmlParse.getSym();
			System.out.println(xmlParse.getSyms());
			System.out.println(xmlParse.getId());
			xmlParse.getSym();
			System.out.println(xmlParse.getSyms());
			System.out.println(xmlParse.getId());
			xmlParse.getSym();
			System.out.println(xmlParse.getSyms());
			System.out.println(xmlParse.getId());
			xmlParse.getSym();
			System.out.println(xmlParse.getSyms());
			System.out.println(xmlParse.getId());
			xmlParse.getSym();
			System.out.println(xmlParse.getSyms());
			System.out.println(xmlParse.getId());
			xmlParse.getSym();
			System.out.println(xmlParse.getSyms());
			System.out.println(xmlParse.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

	
	
	/**
	 * 语法分析测试
	 * @throws FileNotFoundException 
	 * */
	@Test
	public void testGrammarAnalyse() throws FileNotFoundException {
		XmlParse xmlParse = new XmlParse("test.xml");
		xmlParse.grammarAnalyse();
		System.out.println("ok");
	}
	
	@Test
	public void testGetNode() throws FileNotFoundException {
		XmlParse xmlParse = new XmlParse("test.xml");
		xmlParse.grammarAnalyse();
		@SuppressWarnings("unused")
		Node server = xmlParse.getDocument().getNodeByTagName("<server>");
		assertEquals("<server>",server.getName());	
		Node port = server.getChildNode("<port>");
		assertEquals("<port>",port.getName());
		assertEquals("a80",port.getAttr());
	}
	
	@Test
	public void testGetClassPath() {
		java.net.URL classPathUrl = Thread.currentThread().getContextClassLoader().getResource("");
		String classPath =  classPathUrl.getPath();
		int lastIndex = classPath.lastIndexOf("/bin");
		classPath = classPath.substring(0,lastIndex+1);
		
	}
	
	@Test
	public void testPreTravel() throws FileNotFoundException {
		XmlParse xmlParse = new XmlParse("test.xml");
		xmlParse.grammarAnalyse();
		xmlParse.getDocument().preTravel();
	}
	
	
	@Test
	public void testGetEntity() throws FileNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, NoSuchFieldException, IllegalArgumentException, InvocationTargetException {
		XmlParse xmlParse = new XmlParse("test.xml");
		xmlParse.grammarAnalyse();
		Document document = xmlParse.getDocument();
		Server server = (Server) document.getEntity("<server>",Server.class,new String[]{"port","root","name"});
		System.out.println(server.getPort());
		System.out.println(server.getRoot());
		System.out.println(server.getName());
	}
	
}
