import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.*;

import javax.swing.tree.TreePath;

import org.apache.tools.ant.taskdefs.Untar;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.tar.TarUtils;



/*
 * Created on 17-10-2003 by jesper
 * This class should 
 */
/**
 * @author jesper
 */
public class JarReader {
	
	JarFile jar = null;
	private Node topNode = new Node();
	private File jarFilePath = null;
	private File tmpDir = null;
	
	
	public JarReader(File f) throws IOException {
		if (f.isFile()) {
			try {
				jar = new JarFile(f);
				this.jarFilePath = f;
				unJar();
			} catch (IOException e) {
				System.err.println("Error opening jar file: " + e);
			}
		} else {
			throw new IOException("Error opening jar file...");
		}
	}
	
	private ArrayList getJarFileEntries(){
		ArrayList info = new ArrayList();
		Enumeration enum = jar.entries();
		 while (enum.hasMoreElements()) {		   
		   JarEntry entry = (JarEntry)enum.nextElement();	   
		   info.add(entry); 
		 }
		return info;
	}
	
	/**
	 * Just for this main method
	 */
	public void buildJarModel(){
		System.out.println("JarReader.buildJarModel()");
		topNode = new Node(this.jar.getName(), null, true);

		topNode.setPackage(true);
		extractPackages();
	}

	private void unJar(){		
		try {
			tmpDir = new File("./" + "tmp" );
			//tmpDir = new File("." );
			JarUtil.unjar(this.jarFilePath,tmpDir );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getSize(){
		if(jar!=null)
		return jar.size();
		else return 0;
	}
	public long getFileSize(){
		if(jar != null){
		
			File f = new File(jar.getName());
		return f.length();
		}else{
			return 0;
		}

	}
	
	public PackageStructure extractPackages() {
		if (jar != null ) {
		File f = new File(jar.getName());
		
			topNode = new Node(f.getName(), null, true);
			PackageStructure ps = new PackageStructure(topNode);
			ArrayList entries = getJarFileEntries();
			Node lastNode = topNode;
			for (Iterator iter = entries.iterator(); iter.hasNext();) {
				JarEntry element = (JarEntry) iter.next();
				String[] split = element.getName().split("/");
				for (int i = 0; i < split.length; i++) {
					Node n = null;
					String name = (String) split[i].trim();
					int index = name.indexOf(".");
					if (index != -1) {
						//this is a file
						if (i == 0) {
							n = new Node(name, topNode, false);
							n.jarEntry = element;
							ps.addTopLevelPackage(n);
						} else {
							n = new Node(name, lastNode, false);
							n.jarEntry = element;
							ps.addFileToParent(lastNode, n);
						}
						lastNode = topNode;
					} else {
						//this is a package
						if (i == 0) {
							n = new Node(name, topNode, true);
							n.jarEntry = element;
							ps.addTopLevelPackage(n);
						} else {
							n = new Node(name, lastNode, true);
							n.jarEntry = element;
							String path ="";
							for (int j = 0; j < i; j++) {
								path += split[j];
							}
							n.fullPath = path;
							ps.addPackageToParent(lastNode, n);
						}
						lastNode = n;
					}
				}
			}
			return ps;
		}
		return null;
	}


	public JarFile getJarFile(){
		return this.jar;
	}

	
	public String getManifestData(){
		Enumeration enum = jar.entries();

		String all = "";
		while (enum.hasMoreElements()) {
			JarEntry entry = (JarEntry) enum.nextElement();
			if(entry.getName().toUpperCase().equals("META-INF/MANIFEST.MF")){
				InputStream input;
				try {
					input = jar.getInputStream(entry);
					InputStreamReader isr =  new InputStreamReader(input);
		  			BufferedReader reader = new BufferedReader(isr);
		  			
		  			String line; 
		  			while ((line = reader.readLine()) != null) {
		  				all+=line+"\n";
		  			}
		  			reader.close();
					//jar.close();
				} catch (IOException e) {
					//System.out.println(e.getMessage());
					//e.printStackTrace();
				}
			}
		}
		return all;
	}
	
	public StringBuffer processClass(JarEntry entry, Node n) throws IOException {
		//InputStream input = jar.getInputStream(entry);

		//BufferedReader reader =
		//	new BufferedReader(new InputStreamReader(input));

		//get path to class file with no file ending
		String p = slashToDotNoClass(entry.getName());

		String[] paths =
			new String[] {
				".",
				"tmp",
				"./tmp/" + p.replace('.', '/') + ".class",
				tmpDir.getCanonicalPath(),
				tmpDir.getCanonicalPath().replace('\\', '/')
					+ "/"
					+ p.replace('.', '/'),
				"tmp/"
					+ tmpDir.getCanonicalPath()
					+ "\\"
					+ p.replace('.', '\\')
					+ ".class" };

		StringBuffer b = ClassDecompiler.decompile(p, paths);

		//reader.close();
		return b;

	}

	public StringBuffer processManifest(JarEntry entry, Node n) throws IOException {
		String s = getManifestData();
		return new StringBuffer(s);
	}
	
	public StringBuffer processTextBasedFile(JarEntry entry, Node n) {
		StringBuffer code = new StringBuffer();
		code.append("<html><body>");
		InputStream input = null;
		BufferedReader reader = null;
		try {
			input = jar.getInputStream(entry);
			InputStreamReader isr = new InputStreamReader(input);
			reader = new BufferedReader(isr);
			String line;
			while ((line = reader.readLine()) != null) {
				code.append(line);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		code.append("</body></html>");
		return code;
	}


	public void copyFile(File from_file, File to_file){
		FileInputStream from = null;  // Stream to read from source
		FileOutputStream to = null;   // Stream to write to destination
		   try {
			 from = new FileInputStream(from_file);  // Create input stream
			 to = new FileOutputStream(to_file);     // Create output stream
			 byte[] buffer = new byte[4096];         // A buffer to hold file contents
			 int bytes_read;                         // How many bytes in buffer

			 while((bytes_read = from.read(buffer)) != -1) // Read bytes until EOF
			   to.write(buffer, 0, bytes_read);            //   write bytes 
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		   // Always close the streams, even if exceptions were thrown
		   finally {
			 if (from != null) try { from.close(); } catch (IOException e) { ; }
			 if (to != null) try { to.close(); } catch (IOException e) { ; }
		   }

	}
	
	public Enumeration getEntries(){
		return jar.entries();
	}
	
	public String getMainClassManifestAttribute(){
		return "Not implemented yet...";
	}
	
	public String slashToDotNoClass(String slashed){
		String dot = slashed.replaceAll("/",".");
		return dot.substring(0,dot.lastIndexOf("."));
	}
	public String getDir(String fullPath){
		//remove fileName
		return fullPath.substring(0,fullPath.lastIndexOf("/"));
		
	}
	
	public static String getEntryAsString(JarEntry entry){
		String name = entry.getName();
		long size = entry.getSize();
		long compressedSize = entry.getCompressedSize();
		String tmp = "	Name:" + name+ "	Size:"+size + "	CompressedSize:" + compressedSize;
		
		return tmp;
	}
	
	
	public static void main(String[] args) {
		try {
	
			JarReader reader = new JarReader(new File("Util.jar"));
			ArrayList list = reader.getJarFileEntries();
			reader.buildJarModel();
		} catch (IOException e) {
			System.out.println("Failed reading jar:" + e);
		}	
	}

}
