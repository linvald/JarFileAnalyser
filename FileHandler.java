import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
/*
 * Created on 01-11-2003 by jesper
 * This class should 
 */
/**
 * @author jesper
 */
public class FileHandler {
	public static StringBuffer getTextFileText(File f) {
		StringBuffer code = new StringBuffer();
		InputStream input = null;
		BufferedReader reader = null;
		try {
			input = new FileInputStream(f);
			InputStreamReader isr = new InputStreamReader(input);
			reader = new BufferedReader(isr);
			String line;
			while ((line = reader.readLine()) != null) {
				code.append(line + "\n");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return code;
	}
	
	public static void deleteFileTree(File parent) {
		File[] files = parent.getAbsoluteFile().listFiles();
		if (files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				File f = (File) files[i];
				if (f.isDirectory()) {
					FileHandler.deleteFileTree(f);
					f.delete();
				} else {
					f.delete();
				}
			}
		}else{
			parent.delete();
		}
	}
}
