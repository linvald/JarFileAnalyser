import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
/*
 * Created on 01-11-2003 by jesper
 * This class should 
 */
/**
 * @author jesper
 */
public class FileTreePanel extends JPanel {
	private JTree tree;
	private JScrollPane scrollTree;
	private JarGUI gui = null;
	public FileTreePanel(File root, JarGUI gui) {
		this.gui = gui;
		setLayout(new GridLayout(1, 1));
		FileSystemModel fileSystemModel = new FileSystemModel(root);
		// create JTree for FileSystemModel
		tree = new JTree(fileSystemModel);
		// make JTree editable for renaming Files
		tree.setEditable(true);
		tree.setCellRenderer(new FileTreeRenderer());
		// add a TreeSelectionListener
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			// display details of newly selected File when
			// selection changes
			public void valueChanged(TreeSelectionEvent event) {
				File file = (File) tree.getLastSelectedPathComponent();
				if (file != null) {
					String ext =
						file.getName().substring(
							file.getName().lastIndexOf(".") + 1,
							file.getName().length());
					if (ext.equals("txt")
						|| ext.equals("bat")
						|| ext.equals("html")
						|| ext.equals("log")
						|| ext.equals("java")
						|| ext.equals("cs")
						|| ext.equals("css")
						|| ext.equals("xml")
						|| ext.equals("xsl")
						|| ext.equals("xslt")
						|| ext.equals("php")
						|| ext.equals("htm")
						|| ext.equals("cpp")
						|| ext.equals("classpath")
						|| ext.equals("project")
						|| ext.equals("build")
						|| ext.equals("dtd")
						|| ext.equals("xsd")) {
						//open it
						StringBuffer b = FileHandler.getTextFileText(file);
						FileTreePanel.this.gui.tabs.addTab(file.getName(), file.getName(), b);
					} else if (ext.equals("jar") || ext.equals("zip")) {
						try {
							FileTreePanel.this.gui.jarReader = new JarReader(file);
						} catch (IOException e) {
							e.printStackTrace();
						}
						JarTreePanel panel = new JarTreePanel(FileTreePanel.this.gui);
						FileTreePanel.this.gui.tree = panel.getTree();
						panel.initJTree(FileTreePanel.this.gui.jarReader, file.getName());
						panel.setupPopupMenu();
						FileTreePanel.this.gui.addTree(panel,"jar");
						FileTreePanel.this.gui.treeTab.setSelectedIndex(1);
					}else if(ext.equals("class")){
						//FIXIT: decompile it
					
					}
				}
			}
		}); // end addTreeSelectionListener
		scrollTree = new JScrollPane(tree);
		Dimension minimumSize = new Dimension(200, 400);
		scrollTree.setMinimumSize(minimumSize);
		//this.setSize(600, 500);
		add(scrollTree);
	}
	public JTree getTree() {
		return this.tree;
	}
}
