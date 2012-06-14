/*
 * Created on 29-10-2003 by jesper
 * This class should 
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import jode.decompiler.Decompiler;
import jode.decompiler.Options;

/**
 * @author jesper
 */
public class ClassDecompiler {
	static Decompiler d = new Decompiler();

	public static StringBuffer decompile(String clazz, String [] paths){
		if(clazz==null){
			clazz="TestDecompiler";
		}
		d.setClassPath(paths);
		//ArrayList buf = new ArrayList();
		StringBuffer buf = new StringBuffer();
		
					File f = null;
					Writer out;
					try {
						f = File.createTempFile(clazz, "class");
						out = new BufferedWriter(new FileWriter(f));
						d.setOption("style","sun" ); 
						d.setOption("pretty","1" );
						
						//d.setOption("verbose","1" ); //for debugging
						d.decompile( clazz,out,null);
					} catch (IOException e) {
						e.printStackTrace();
					}

					System.out.println("Succesfully decompiled");
					
					String NEWLINE = "\n";
					try {
						BufferedReader br = new BufferedReader(new FileReader(f)); 
						String line ="";
						int num = 0;
						while ((line = br.readLine()) != null) {
							if(num>2) 
								buf.append(line+NEWLINE); 
							num++;
						} 
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				
				return buf;
	}
	
	
	public static void setClassPath(String path){
		d.setClassPath(path);
	}
	public static void setClassPath(String []paths){
		d.setClassPath(paths);
	}
}
