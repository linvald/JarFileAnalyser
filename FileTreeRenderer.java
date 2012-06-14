import java.awt.*;
import java.io.File;
import java.net.URL;


import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.*;

public class FileTreeRenderer extends DefaultTreeCellRenderer {
        ImageIcon icon;

        public FileTreeRenderer() {
            icon = new ImageIcon("img/class.gif");
        }

        public Component getTreeCellRendererComponent(
                            JTree tree,
                            Object value,
                            boolean sel,
                            boolean expanded,
                            boolean leaf,
                            int row,
                            boolean hasFocus) {

            super.getTreeCellRendererComponent(
                            tree, value, sel,
                            expanded, leaf, row,
                            hasFocus);
  
				setTheIcon(value);
            return this;
        }

		protected void setTheIcon(Object value) {
			if (value instanceof DefaultMutableTreeNode) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
				if (node.getUserObject() instanceof Node) {
					Node inf = (Node) (node.getUserObject());
					String title = inf.name;
					assignIcon(title);
					if (inf.isPackage()) {
						setIcon(new ImageIcon("img/package.gif"));
					}
					setToolTipText(inf.getAllInfo());
				}
				}else if(value instanceof File){
					File f = (File)value;
					String title = f.getName();
				 	if (f.isDirectory()) {
						setIcon(new ImageIcon("img/package.gif"));
					} else{
						assignIcon(title);	
				 	}
			}
		}
		public void assignIcon(String title) {
			title = title.toLowerCase();
			if (title.indexOf("class") >= 0) {
				setIcon(new ImageIcon("img/class.gif"));
			} else if (title.indexOf("java") >= 0) {
				setIcon(new ImageIcon("img/java.gif"));
			}  else if (title.indexOf("jar") >= 0) {
				setIcon(new ImageIcon("img/jar.gif"));
			} else if (title.indexOf("properties") >= 0) {
				setIcon(new ImageIcon("img/property.gif"));
			} else if (title.indexOf("exe") >= 0) {
				setIcon(new ImageIcon("img/exe.gif"));
			} else if (title.indexOf("cs") >= 0) {
				setIcon(new ImageIcon("img/cs.gif"));
			} else if (
				title.indexOf("sln") >= 0
					|| title.indexOf("suo") >= 0
					|| title.indexOf("csproj") >= 0
					|| title.indexOf("resx") >= 0
					|| title.indexOf("user") >= 0
					|| title.indexOf("doc") >= 0
					|| title.indexOf("xsl") >= 0
					|| title.indexOf("ppt") >= 0) {
				setIcon(new ImageIcon("img/micro.gif"));
			} else if (
				title.indexOf("xsd") >= 0
					|| title.indexOf("xml") >= 0
					|| title.indexOf("xsl") >= 0
					|| title.indexOf("build") >= 0
					|| title.indexOf("dtd") >= 0) {
				setIcon(new ImageIcon("img/x.gif"));
			} else if (title.indexOf("jpg") >= 0 
					|| title.indexOf("gif") >= 0
					|| title.indexOf("jpg") >= 0
					|| title.indexOf("jpeg") >= 0
					|| title.indexOf("tiff") >= 0
					|| title.indexOf("bmp") >= 0
					|| title.indexOf("ico") >= 0) {
				setIcon(new ImageIcon("img/image.gif"));
			}else if (title.indexOf("txt") >= 0 
				|| title.indexOf("bat") >= 0
				|| title.indexOf("html") >= 0
				|| title.indexOf("log") >= 0) {
				setIcon(new ImageIcon("img/txt.gif"));
			}else if (title.indexOf("txt") >= 0 
				|| title.indexOf("php") >= 0
				|| title.indexOf("c++") >= 0
				|| title.indexOf("cpp") >= 0
				|| title.indexOf("classpath") >= 0
				|| title.indexOf("project") >= 0){
			setIcon(new ImageIcon("img/code.gif"));
			}
			 else {
				setIcon(new ImageIcon("img/other.gif"));
			}
			if(title.indexOf("css") >= 0){
				setIcon(new ImageIcon("img/code.gif"));
			}
		}
	
    }