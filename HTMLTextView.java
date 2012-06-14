import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
/*
 * Created on 01-11-2003 by jesper
 * This class should 
 */
/**
 * @author jesper
 */
public class HTMLTextView extends JPanel {
	private JTextPane text;
	public Node node;
	public HTMLTextView() {
	}
	public HTMLTextView(StringBuffer code, JPopupMenu contextMenu) {
		init(code, contextMenu);
	}
	public HTMLTextView(StringBuffer code, JPopupMenu contextMenu, Node n) {
		node = n;
		init(code, contextMenu);
	}
	private void init(StringBuffer code,JPopupMenu contextMenu) {
		setLayout(new GridLayout(1, 1));
		text = new JTextPane();
		javax.swing.text.html.HTMLEditorKit eKit = new javax.swing.text.html.HTMLEditorKit();
		text.setEditorKit(eKit);
		text.setContentType("text/html");
		JScrollPane scrollText = new JScrollPane(text);
		scrollText.setAutoscrolls(true);
		//text.setFont(new Font("Verdana", Font.BOLD, 12));
		text.setText(code.toString());
		text.add(contextMenu);
		this.add(scrollText);
	}
	public String getText() {
		return text.getText();
	}
	public String getUnModyfiedContent() {
		return node.content;
	}
}
