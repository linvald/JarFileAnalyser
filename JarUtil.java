import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.Date;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class JarUtil {
	public static void unjar(File jarFile, File dir) throws FileNotFoundException, IOException {
		ZipInputStream zis = null;
		try {
			// code from WarExpand
			zis = new ZipInputStream(new FileInputStream(jarFile));
			ZipEntry ze = null;
			while ((ze = zis.getNextEntry()) != null) {
				extractFile(
					jarFile,
					dir,
					zis,
					ze.getName(),
					new Date(ze.getTime()),
					ze.isDirectory());
			}
		} finally {
			if (zis != null) {
				try {
					zis.close();
				} catch (IOException e) {
				}
			}
		}
	}
	protected static void debug(String msg) {
		//System.out.println(msg); 
	}
	protected static void extractFile(
		File srcF,
		File dir,
		InputStream compressedInputStream,
		String entryName,
		Date entryDate,
		boolean isDirectory)
		throws IOException {
		//File f = fileUtils.resolveFile(dir, entryName);
		File f = new File(dir, entryName);
		debug("expanding " + entryName);
		// create intermediary directories - sometimes zip don't add them
		File dirF = f.getParentFile();
		dirF.mkdirs();
		if (isDirectory) {
			f.mkdirs();
		} else {
			byte[] buffer = new byte[1024];
			int length = 0;
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(f);
				while ((length = compressedInputStream.read(buffer)) >= 0) {
					fos.write(buffer, 0, length);
				}
				fos.close();
				fos = null;
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
					}
				}
			}
		}
		//fileUtils.setFileLastModified(f, entryDate.getTime());
	}
	public static void main(String args[]) throws Exception {
		File jarFile = new File(args[0]);
		File destFile = new File(args[1]);
		System.out.println("Unjarring " + jarFile.getPath() + " to " + destFile.getPath());
		unjar(jarFile, destFile);
		System.out.println("Done!");
	}
}
