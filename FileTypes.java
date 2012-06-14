import java.util.ArrayList;
import java.util.Arrays;
/*
 * Created on 31-10-2003 by jesper
 * This class should 
 */
/**
 * @author jesper
 */
public class FileTypes {
	public final static String CLASS = "class";
	public final static String MANIFEST = "manifest";
	public final static String MANIFEST2 = "mf";
	public final static String GIF = "gif";
	public final static String HTML = "html";
	public final static String TXT = "txt";
	public final static String TXT2 = "text";
	public final static String JAVA = "java";
	public final static String CSS = "css";
	
	public static String getFileType(String name) {
		if (name.indexOf(".") != -1 && name.length() > 3) {
			String end = name.substring(name.lastIndexOf(".") + 1, name.length()).toLowerCase();
			if (end.equals(FileTypes.CLASS)) {
				return FileTypes.CLASS;
			} else if (end.equals(FileTypes.MANIFEST)) {
				return FileTypes.MANIFEST;
			} else if (end.equals(FileTypes.MANIFEST2)) {
				return FileTypes.MANIFEST;
			} else if (end.equals(FileTypes.GIF)) {
				return FileTypes.GIF;
			} else if (end.equals(FileTypes.HTML)) {
				return FileTypes.HTML;
			} else if (end.equals(FileTypes.TXT)) {
				return FileTypes.TXT;
			} else if (end.equals(FileTypes.TXT2)) {
				return FileTypes.TXT;
			} else if (end.equals(FileTypes.JAVA)) {
				return FileTypes.JAVA;
			} else if (end.equals(FileTypes.CSS)) {
				return FileTypes.CSS;
			} else {
				return "";
			}
		}
		return "";
	}
}
