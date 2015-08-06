package com.chenrui.xmlParse;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.chenrui.xmlModel.Document;
import com.chenrui.xmlModel.Node;
import com.chenrui.xmlModel.TypeOfNode;

/**
 * xml的语法解析器
 * */
public class XmlParse {
	
	private Sym sym;
	
	private String id ="";
	
	private String filePath;
	
	private String line="";
	
	private int indexOfLine = 0;//当前从line读取到的单词的下标
	
	private BufferedReader buffer;

	private Document document;
	
	private char c;
	
	/**
	 * xml文件的名字
	 * */
	public XmlParse(String fileName) throws FileNotFoundException {
		
		java.net.URL classPathUrl = Thread.currentThread().getContextClassLoader().getResource("");
		String classPath =  classPathUrl.getPath();
		filePath = classPath+File.separator+fileName;
		buffer = new BufferedReader(new FileReader(filePath));
		
	}

	public Document parse() {
		grammarAnalyse();
		return document;
	}
	
	/**
	 * xml的词法解析
	 * xml的单词：
	 * （1）标签
	 * （2）属性值
	 *  sym:表示的是单词的类型，sign表示是标签，attr表示是属性
	 *  id:记录的是单词的实际的值
	 *  @throws Exception 
	 *  词法分析的目的仅仅只是为了获得单词，不用检查单词出现的位置是否合法。
	 * */
	public void getSym() {
		try {	
			id = "";
			//去除空格
			getNonBlank();
			switch(c) {
				//xml标签的情况
				case '<':
				{
					addChar();
					getch();
					if(c == '/') {
						addChar();
						getch();
						sym = Sym.RIGHTSIGN;
					}
					else {
						sym = Sym.LEFTSIGN;
					}
					
					while((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
						addChar();
						getch();
					}
					if(c != '>') {
						System.out.println("标签格式错误");
						sym = Sym.NUL;
					}
					addChar();
					break;
				}	
				//属性值的情况
				default: {
					addChar();
					getch();
					while(c != '<') {
						addChar();
						getch();
					}
					sym = Sym.ATTR;
					indexOfLine--;
					break;
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("the reason is --------"+e.getMessage());
			sym = Sym.ATTR;
		}
	}
	
	/**
	 * 将通过getch获得的字符存到id里面
	 */
	public void addChar() throws Exception {
		if(id.length() <= 50) {
			id = id + c;
		}
		else {
			System.out.println("Error-id is to long");
			throw new Exception("Error-id is to long");
		}
	}
	
	/**
	 * 跳过空格符
	 * @throws IOException 
	 * */
	public void getNonBlank() throws IOException {
		c = getch();
		while(isBlank()) {
			c = getch();
		}
	}
	
	/**
	 * 判断是否是空格
	 * */
	public boolean isBlank() {
		if(('\0' == c)||('\t' == c)||('\n'==c)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	/**
	 * 获得一个字符。
	 * @throws IOException 
	 * 
	 * */
	public char getch() throws IOException {
		while(line.length() == indexOfLine) {//表明已经读到一行的末尾
			line = buffer.readLine();
			indexOfLine = 0;
			if(line == null) {
				return (char) -1;
			}
		}
		return c = line.charAt(indexOfLine++);
	}
	
	
	/**
	 * 语法分析
	 * 生成一个语法树，有一部分语法检查，但是不完善，缺少出错处理
	 * */
	public void grammarAnalyse() {
		try {
			getSym();
			xml();
		} catch (Exception e) {
			System.out.println("解析失败，xml文档格式有错");
		}
		
	} 
	

	public void xml() {
		if((sym == Sym.LEFTSIGN) && (id.equals("<xml>"))) {
			document = new Document();
			Node root = document.getRoot();
			getSym();
			Node(root);
			//一到多个的节点
			while(sym == Sym.LEFTSIGN) {
				getSym();
				Node(root);
			}
			if(!((sym == Sym.RIGHTSIGN) && (id.equals("</xml>")))) {
				error("文档没有以</xml>结束");
			} 
			
		}
		else {
			error("文档没有以<xml>开始");
		}
	}
	
	/**
	 * 除了根节点之外，所有的节点都有父亲节点。
	 * @param fatherNode 父亲节点
	 * */
	public void Node(Node fatherNode) {
		if(sym == Sym.LEFTSIGN) {
			System.out.println(id);
			Node node = new Node(id);
			fatherNode.addChile(node);
			getSym();
			NodeA(node);	
		}	
	}
	
	/**
	 *（1） A-><属性值><右标签>
	 *（2）A->{节点}<右标签>
	 * @param n 对于（1）来说，n就是所对应的node
	 *          对于（2）来说，n是父节点。 
	 * */
	public void NodeA(Node n) {
		if(sym == Sym.ATTR) {
			System.out.println(id);
			n.setAttr(id);
			n.setTypeOfNode(TypeOfNode.ATTR);
			getSym();
			System.out.println(id);
			if(sym != Sym.RIGHTSIGN) {
				error("节点没有结束标签");
			}
			getSym();
		}
		else {
			n.setTypeOfNode(TypeOfNode.SIGN);
			//一到多个节点
			Node(n);
			while(sym == Sym.LEFTSIGN) {
				Node(n);
			}
			//右标签
			if(sym != Sym.RIGHTSIGN) {
				error("没有右标签");
			}
			System.out.println(id);
			getSym();
		}
		
	}
	
	public void error(String errorInfo) {
		System.out.println(errorInfo);
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public int getIndexOfLine() {
		return indexOfLine;
	}

	public void setIndexOfLine(int indexOfLine) {
		this.indexOfLine = indexOfLine;
	}

	public BufferedReader getBuffer() {
		return buffer;
	}

	public void setBuffer(BufferedReader buffer) {
		this.buffer = buffer;
	}

	public Sym getSyms() {
		return sym;
	}
	public void setSym(Sym sym) {
		this.sym = sym;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
}
 enum Sym {  
	LEFTSIGN,RIGHTSIGN,ATTR,NUL;
 }  
