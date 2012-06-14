import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.*;

/*
 * Created on 01-11-2003 by jesper
 * This class should 
 */
/**
 * @author jesper
 */
public class TextView extends JPanel{
	
	private JTextArea text;

	public TextView(){}
	public TextView(StringBuffer code, JPopupMenu contextMenu){
		setLayout(new GridLayout(1, 1)); 
		text = new JTextArea();
		
		JScrollPane scrollText = new JScrollPane(text);
		scrollText.setAutoscrolls(true);
		text.setFont(new Font("Verdana", Font.BOLD, 12));
		text.setText(code.toString());
		text.add(contextMenu);
		this.add(scrollText);
	}
	
	public String getText(){
		return text.getText();
	}

	
}
