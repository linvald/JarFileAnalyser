/*
 * Created on 17-10-2003 by jesper
 * This class should 
 */
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;

/**
 * @author jesper
 */
public class JarGUI extends JFrame {
	
	public JarReader jarReader = null; //jar must be selected by user
	private Container frame = null;
	private final String title = "Jar & Zip browser";
	public JTree tree = null;
	TabView tabs;
	JTabbedPane treeTab;
	private JLabel noSelectedLabel = new JLabel("Browse for jar og zip file or select it in file view!");

	
	//panels
	private JPanel  bottom;
	public JPanel treePanel, treePanelDirs;
	private ButtonPanel top;
	private JSplitPane splitPane;
	
	
	public JarGUI(){
		frame = this.getContentPane();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		initGUI();
		this.setTitle(title);
	}
	

	
	private void initGUI(){	
		//Quit this app when the big window closes.
		   addWindowListener(new WindowAdapter() {
			   public void windowClosing(WindowEvent e) {
			   	File tmp  = new File("./" + "tmp" );
			   	if(tmp.isDirectory()){
					FileHandler.deleteFileTree(tmp);
			   	}
				   System.exit(0);
			   }
		   });
		
		treePanel = new JPanel();
		treePanelDirs = new JPanel();
		tabs = new TabView();
		treeTab = new JTabbedPane();
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeTab, tabs);
		top = new ButtonPanel(this);
		top.setBackground(Color.lightGray);
		this.setBackground(Color.lightGray);
		bottom = new JPanel();
		frame.setLayout(new BoxLayout(frame,BoxLayout.Y_AXIS));
		
		treePanel.setLayout(new GridLayout(1,1));
		treePanelDirs.setLayout(new GridLayout(1,1));
		treePanel.add(this.noSelectedLabel);
		bottom.setLayout(new BoxLayout(bottom,BoxLayout.X_AXIS));
	//	top.setBackground(Color.white);
		frame.add(top);
		frame.add(bottom);
		
		
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(300);
		bottom.add(splitPane);
		this.setSize(600,500);
		
		treeTab.setTabPlacement(JTabbedPane.BOTTOM);
		treeTab.addTab("Dir view", treePanelDirs);
		treeTab.addTab("Jar view", treePanel);
	}

	public JTree getTree(){
		return this.tree;
	}
	
	public void addTree(JPanel treeWithPanel, String fileOrJar) {
		if (fileOrJar.equals("file")) {
			if (treePanelDirs.getComponents().length > 0)
				treePanelDirs.removeAll();
			treePanelDirs.add(treeWithPanel);
			if (treeWithPanel instanceof FileTreePanel) {
				FileTreePanel p = (FileTreePanel) treeWithPanel;
				this.tree = p.getTree();
			}
			if (treeWithPanel instanceof JarTreePanel) {
				JarTreePanel p = (JarTreePanel) treeWithPanel;
				this.tree = p.getTree();
			}
			if(treeTab.getTabCount()>0)
			this.treeTab.setSelectedIndex(0);
		} else if (fileOrJar.equals("jar")) {
			if (treePanel.getComponents().length > 0)
				treePanel.removeAll();
			treePanel.add(treeWithPanel);
			if (treeWithPanel instanceof FileTreePanel) {
				FileTreePanel p = (FileTreePanel) treeWithPanel;
				this.tree = p.getTree();
			}
			if (treeWithPanel instanceof JarTreePanel) {
				JarTreePanel p = (JarTreePanel) treeWithPanel;
				this.tree = p.getTree();
			}
			if(treeTab.getTabCount()>0)
			this.treeTab.setSelectedIndex(1);
		}
		splitPane.setDividerLocation(splitPane.getDividerLocation());
		//tree.repaint();
	}

	public static void main(String[] args) {
		new JarGUI().show();
	}
}
