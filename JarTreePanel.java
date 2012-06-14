import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.*;

/*
 * Created on 01-11-2003 by jesper
 * This class should 
 */
/**
 * @author jesper
 */
public class JarTreePanel extends JPanel implements ActionListener {
	
	private DefaultMutableTreeNode topNode;
	private JTree tree;
	private DefaultTreeModel treeModel = null;	
	private JScrollPane scrollTree;
	private JPopupMenu _popup;
	private JarGUI gui;
	
	public JarTreePanel(JarGUI gui){
		this.gui = gui;
		setLayout(new GridLayout(1, 1));
		tree = new JTree();
		this._popup = new JPopupMenu();
		
		tree.putClientProperty("JTree.lineStyle", "Angled");
		tree.setShowsRootHandles(true);
		tree.setCellRenderer(new FileTreeRenderer());
		ToolTipManager.sharedInstance().registerComponent(tree);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}
	
	private void addNodes(DefaultMutableTreeNode node) {
		Node par = (Node) node.getUserObject();
		if (par.children.size() > 0) {
			for (Iterator iter = par.children.iterator(); iter.hasNext();) {
				Node n = (Node) iter.next();
				DefaultMutableTreeNode dir = new DefaultMutableTreeNode(n, true);
				if (!n.isAdded) {
					if (n.isPackage() && n.children.size() == 0) {
					} else {
						node.add(dir);
						n.isAdded = true;
					}
				}
				if (n.children.size() > 0) {
					addNodes(dir);
				}
			}
		}
	}
	

	void initJTree(JarReader reader, String jarFileName) {
		//tree

		topNode = new DefaultMutableTreeNode("No jar selected",true);
		Node n = new Node("No jar selected", null, true);
		treeModel = new DefaultTreeModel(topNode);
		//tree = new JTree(topNode);
		
		
		PackageStructure ps = reader.extractPackages();
				if (ps != null) {
					topNode = new DefaultMutableTreeNode(ps.getTopNode(), true);
					addNodes(topNode);
					treeModel.setRoot(topNode);
					tree.setModel(treeModel);
					tree.doLayout();
					tree.invalidate();
					//text.setText(
					//	(reader.getManifestData() != "") ? reader.getManifestData() : "No manifest found");
				} else {
					JOptionPane.showMessageDialog(
						gui.getContentPane(),
						"Invalid file type!",
						"Select a java archive file",
						JOptionPane.WARNING_MESSAGE);
					//	text.setText("Please select a valid jar file...");
				}
		
		
		// A node in the tree selected
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node =
					(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (node == null) {
					System.out.println("Node is null in valueChanged...");
					return; 
				}
				Object nodeInfo = node.getUserObject();
				if (nodeInfo instanceof Node) {
					Node n = (Node)nodeInfo;
					//System.out.println(n);
					//text.append("\n" +  n.getAllInfo());

					try {
						if(n.fileType.equals(FileTypes.CLASS)){
							//StringBuffer b = jarReader.processClass(n.jarEntry, n );
							//tabs.addTab(n.jarEntry.getName(),n.getAllInfo(),b);
							StringBuffer b = gui.jarReader.processClass(n.jarEntry, n );
							n.content = b.toString();
							gui.tabs.addHTMLTab(n.jarEntry.getName(),n.getAllInfo(),JavaToHTML.textJavaToHTML(b.toString()),n);
							
						}else if(n.fileType.equals(FileTypes.MANIFEST)||n.fileType.equals(FileTypes.MANIFEST2)){
							StringBuffer b = gui.jarReader.processManifest(n.jarEntry,n);
							n.content = b.toString();
							gui.tabs.addHTMLTab(n.jarEntry.getName(),n.getAllInfo(),b,n);
						}else if(n.fileType.equals(FileTypes.HTML)){
							StringBuffer b = gui.jarReader.processTextBasedFile(n.jarEntry,n);
							n.content = b.toString();
							gui.tabs.addHTMLTab(n.jarEntry.getName(),n.getAllInfo(),b,n);	
						}else if(n.fileType.equals(FileTypes.TXT)){
							StringBuffer b = gui.jarReader.processTextBasedFile(n.jarEntry,n);
							n.content = b.toString();
							gui.tabs.addHTMLTab(n.jarEntry.getName(),n.getAllInfo(),b,n);	
						}else if(n.fileType.equals(FileTypes.CSS)){
							StringBuffer b = gui.jarReader.processTextBasedFile(n.jarEntry,n);
							n.content = b.toString();
							gui.tabs.addHTMLTab(n.jarEntry.getName(),n.getAllInfo(),b,n);	
					}
						else if(n.fileType.equals(FileTypes.GIF)){
						//TODO: add processImageData...
							//StringBuffer b = jarReader.processTextBasedFile(n.jarEntry,n);
							//tabs.addHTMLTab(n.jarEntry.getName(),n.getAllInfo(),b);	
					}

					} catch (IOException e2) {
						e2.printStackTrace();
					}				
				}
			}
		});
		
		scrollTree = new JScrollPane(tree);	
		scrollTree.setBackground(Color.orange);
		

		Dimension minimumSize = new Dimension(200, 400);
		scrollTree.setMinimumSize(minimumSize);
		add(scrollTree);

		
	}

	

	public void setupPopupMenu() {
		JMenuItem itemNavigateLink = new JMenuItem("View as html");
		itemNavigateLink.addActionListener(this);
		itemNavigateLink.addActionListener(this);
		_popup.removeAll();
		_popup.add(itemNavigateLink);
		
		tree.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					if(e.getButton()==MouseEvent.BUTTON3){
						TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
						TreeNode node = (TreeNode) selPath.getLastPathComponent();
						if (node == null){
							System.out.println("Node is null");
							return;
						}
						_popup.show(tree, e.getX(), e.getY());
					}
				}
			});
		}
		
	/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getSource().getClass() == JMenuItem.class) {
				JMenuItem source = (JMenuItem) arg0.getSource();
				System.out.println("MenuItems name is " + source.getText());
				if (source.getText().equals("Delete link")) {
					//
				} else if (source.getText().equals("View as html")) {
					System.out.println("View as html clicked");
					DefaultMutableTreeNode node =
						(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
					if (node == null) {
						System.out.println("Node is null in valueChanged...");
						return;
					}
					Object nodeInfo = node.getUserObject();
					if (nodeInfo instanceof Node) {
						Node n = (Node) nodeInfo;
						StringBuffer b = gui.jarReader.processTextBasedFile(n.jarEntry, n);
						n.content = b.toString();
						gui.tabs.addHTMLTab(n.jarEntry.getName(), n.getAllInfo(), b, n);
					}
				}
			}
		}
		
	public JTree getTree(){
		return this.tree;
	}
	
}
