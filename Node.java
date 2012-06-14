/*
 * Created on 18-10-2003 by jesper
 * This class should 
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;

/**
 * @author jesper
 */
public class Node implements Comparable {
	
	public String name;
	private Node parent;
	TreeSet children = null;
	private boolean isPackage, isFile;
	public int level;
	public boolean isAdded = false;
	public JarEntry jarEntry = null;
	public String fullPath ="";
	public String fileType = "";
	public String content = "";

	
	public Node(){
		children = new TreeSet();
	}
	public Node(String name, Node parent, boolean isPackage){
		children = new TreeSet();
		this.name = name;
		this.parent = parent;
		this.isPackage = isPackage;
		this.isFile = !isPackage;
		this.fileType = FileTypes.getFileType(name);
	}

	public int getChildCount(){
		return children.size();
	}

	
	public void addChild(Node n){	
		children.add(n);
	}

	public boolean isFile() {
		return isFile;
	}

	public boolean isPackage() {
		return isPackage;
	}

	public String getName() {
		return name;
	}

	public Node getParent() {
		return parent;
	}

	
	public String getAllInfo(){
		String s =
					 (isPackage() ? "Package " : "File ")+ "name="
						+ getName()
						+ " Parent=";
						if(getParent()== null){
							s+= "TOP";
						}else{
							s+= getParent().getName();
						}
						s+= " ChildCount="
						+ children.size();
		return s;
	}
	public String toString(){
		return this.name;
	}
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * Compares this object with the specified object for order. 
	 * Returns a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than 
	 * the specified object.
	 */
	public int compareTo(Object arg0) {
		Node toCompare = (Node)arg0;
		if(toCompare.name.equals(this.name)){
			return 0;
		}else{
			return -1;
		}
	}
	
	public boolean equals(Object arg0){
		Node toCompare = (Node)arg0;
		if(toCompare.name.equals(this.name) && toCompare.fullPath.equals(this.fullPath) ){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * @param b
	 */
	public void setFile(boolean b) {
		isFile = b;
		isPackage = !b;
	}

	/**
	 * @param b
	 */
	public void setPackage(boolean b) {
		isPackage = b;
		isFile = !b;
	}

	/**
	 * @param node
	 */
	public void setParent(Node node) {
		parent = node;
	}
	public Node getChild(Node n){
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			Node element = (Node) iter.next();
			if(element.getName().equals(n.getName())){
				return element;
			}
		}
		return null;
	}
	public boolean hasChild(Node n){
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			Node element = (Node) iter.next();
			if(element.getName().equals(n.getName())){
				return true;
			}
		}
		return false;
	}
}
