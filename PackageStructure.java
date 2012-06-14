/*
 * Created on 19-10-2003 by jesper
 * This class should 
 */
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author jesper
 */
public class PackageStructure {
	
	private Node superNode;
	//private Set all;
	private ArrayList all;

	
	public PackageStructure(Node superNode){
		this.superNode = superNode;
		//all = new LinkedHashSet();
		all = new ArrayList();
		all.add(superNode);
	}
	
	public void addPackageToParent(Node parentPack, Node childPack){
		childPack.setPackage(true);
		if(!all.contains(childPack))
			all.add(childPack);
		Node n = findNode(parentPack);
		if(n==null){
			superNode.addChild(childPack);
		}else{
			n.addChild(childPack);
		}
	}
	public void addFileToParent(Node parentPack, Node file){
		file.setFile(true);
		if(!all.contains(file))
			all.add(file);

		Node n = findNode(parentPack);
		if(n==null){
			superNode.addChild(file);
		}else{
			n.addChild(file);
		}

	}
	
	public void addTopLevelPackage(Node n){
		if(!all.contains(n))
			all.add(n);
	
		Node no = findNode(superNode);
				if(no!=null){
					superNode.addChild(n);
				}
	}
	
	public Node findNode(Node n){
		for (Iterator iter = all.iterator(); iter.hasNext();) {
			Node element = (Node) iter.next();
			if(element.equals(n)){
				return element;
			}
		}	
		return null;
	}



	public Node getTopNode(){
		for (Iterator iter = all.iterator(); iter.hasNext();) {
				Node element = (Node) iter.next();
		//System.out.println(element);
			}
		return this.superNode;
	}
	
	public String toString(){
		return superNode.toString();
	}

}
