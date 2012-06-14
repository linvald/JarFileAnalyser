import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;


import jode.decompiler.Decompiler;
import jode.decompiler.ProgressListener;


public class TestDecompiler {
	
	public static void main(String[] args) {
		ClassDecompiler.setClassPath("TestDecompiler");
		//StringBuffer buf = ClassDecompiler.decompile("TestDecompiler");
		//System.out.println("BUFFER:"+ buf);
		
		/*Decompiler d = new Decompiler();

		Writer out = new BufferedWriter(new OutputStreamWriter(System.out));

	
		try {
			d.decompile("TestDecompiler",out,null);
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
	}

		
}
