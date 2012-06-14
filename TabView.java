import java.awt.Component;
import java.awt.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;


public class TabView extends JPanel implements ActionListener {
	
	private JTabbedPane tabbedPane = new JTabbedPane();
	private ImageIcon icon = new ImageIcon("img/tab.gif");
	private JPopupMenu contextMenu;
	private JFileChooser fileChooser;
	
	public TabView(){
		this.add(tabbedPane);
		setLayout(new GridLayout(1, 1)); 
		contextMenu = new JPopupMenu();
		init();
		setupMenu();
	}
	
	public void init(){
		tabbedPane.addMouseListener(new MouseAdapter () {
			public void mousePressed(MouseEvent e) {
				if(e.getModifiers()==InputEvent.BUTTON3_MASK){
					contextMenu.show(e.getComponent(),
					   e.getX(), e.getY());    
				}
			}
			public void mouseReleased(MouseEvent e) {}
		});
		fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save file");
		fileChooser.setApproveButtonText("Save file");
		fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
	}
	
	public void addTab(String tabTitle, String info, StringBuffer code ){
		//maybe switch on title to determine an image
		tabbedPane.addTab(tabTitle, icon, new TextView(code, contextMenu), info);
		tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
	}
	
	public void addHTMLTab(String tabTitle, String info, StringBuffer code, Node n ){
		//maybe switch on title to determine an image
		HTMLTextView html = new HTMLTextView(code, contextMenu, n);
		tabbedPane.addTab(tabTitle, icon,html , info);
		tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
	}
	public void addHTMLTab(String tabTitle, String info, StringBuffer code ){
		//maybe switch on title to determine an image
		HTMLTextView html = new HTMLTextView(code, contextMenu);
		tabbedPane.addTab(tabTitle, icon,html , info);
		tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
	}
	
	
	private void setupMenu() {
		JMenuItem close = new JMenuItem("close");
		JMenuItem closeall = new JMenuItem("close all");
		JMenuItem save = new JMenuItem("save");
		
		close.addActionListener(this);
		closeall.addActionListener(this);
		save.addActionListener(this);
		
		contextMenu.add(close);
		contextMenu.add(closeall);
		contextMenu.add(save);
	}
	
	private void saveTab(String toSave, File f) {
			boolean ok = false;
			BufferedWriter out = null;
			try {
				ok = f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (ok) {
				try {
					out = new BufferedWriter(new FileWriter(f));
					out.write(toSave);
				} catch (IOException e1) {
					e1.printStackTrace();
				}finally{
					try {
						out.close();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
				}
			} else {
				System.out.println("Couldnt create file...");
			}
		//}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o instanceof JMenuItem){
			JMenuItem sender = (JMenuItem)o;
			String name = sender.getText();
			if(name.equals("close")){
				tabbedPane.removeTabAt(tabbedPane.getSelectedIndex());
			}else if(name.equals("save")){
				int index = tabbedPane.getSelectedIndex();
				Component c = tabbedPane.getComponent(index);
				Point p = c.getLocation();
				Component t = tabbedPane.getComponentAt(new Point(p.x+20, p.y+30));
				if (t instanceof TextView) {
					TextView text = (TextView)t;
					//String code = text.getText();
					//save the code
					//saveTab(code);
				}
				if (t instanceof HTMLTextView) {
					HTMLTextView text = (HTMLTextView)t;
					fileChooser.setSelectedFile(new File(text.node.getName()));
					int returnVal = fileChooser.showSaveDialog(this);
								
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File f = fileChooser.getSelectedFile();
								
						//save the code
						String fileType = f.getName().substring(f.getName().lastIndexOf("."),f.getName().length());
						if(fileType.indexOf("htm")!=-1){
							//save as html
							String code = text.getText();
							saveTab(code,f);	
						}else if(fileType.indexOf("java")!=-1){
							//save as java
							String code = text.getUnModyfiedContent();
							saveTab(code, f);
						}else if(text.node.fileType.indexOf(FileTypes.MANIFEST)!=-1||text.node.fileType.indexOf(FileTypes.MANIFEST2)!=-1){	
							String code = text.getUnModyfiedContent();
							saveTab(code, f);
							System.out.println("Saved manifest");
						}else{
							System.out.println("FILETYPE UNKNOWN:" + text.node.fileType);
						}
					}	
				}
			}else if(name.equals("close all")){
				tabbedPane.removeAll();
			}
		}
	}
}
