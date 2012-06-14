import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.text.Document;




/*
 * Created on 31-10-2003 by jesper
 * This class should 
 */
/**
 * @author jesper
 */
public class HTMLTextProcessor {
	

	private ArrayList codeIn = null;
	private StringBuffer codeOut = null;
	private File codeFile = null;
	
	private final String[] keywords = new String[]{"abstract", "double", "int", "strictfp", 
		"boolean", "else", "interface", "super",
			"break", "extends", "long", "switch", 
			"byte", "final", "native", "synchronized", 
			"case", "finally", "new", "this", 
			"catch", "float", "package", "throw", 
			"char", "for", "private", "throws", 
			"class", "goto", "protected", "transient",  
			"const",  "if", "public", "try", 
			"continue", "implements", "return", "void", 
			"default", "import", "short", "volatile", 
			"do", "instanceof", "static", "while" 
	};
	

	
	public  HTMLTextProcessor (ArrayList buf){
		codeIn = buf;
		codeOut = new StringBuffer();	
		
	}
	public  HTMLTextProcessor (File buf){
		codeFile = buf;
		codeOut = new StringBuffer();	
		
	}
	
	
	public StringBuffer getSyntaxHighlighting() {
		ArrayList allWords = new ArrayList(Arrays.asList(keywords));
	
		for (Iterator iter = codeIn.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			codeOut.append(
			"<b><font color=\"blue\">" + " " + element + "</font></b>");
			/*String[] wordsInLine = element.split(" ");
			for (int i = 0; i < wordsInLine.length; i++) {
				String singleWord = (String) wordsInLine[i];
				System.out.println(singleWord + " ");
				if (allWords.contains(singleWord)) {
					codeOut.append(
						"<b><font color=\"blue\">" + " " + singleWord + "</font></b>");
				} else {
					codeOut.append(" " + singleWord );
				}
			}*/
			codeOut.append("<br>");
		}
		return codeOut;
	}
}
