package com.server;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class PdftoJpg {
	// allow images selection for converting
	private static String path_env = System.getenv("OPENSHIFT_DATA_DIR");
	private static String jpg_path=path_env+"/SamplePDF/";
	public static void selectPdf(String path) {
			File file = new File(path);
			convertPDFToJPG(file.toString());
	}

	public static void convertPDFToJPG(String src) {

		try {
			PDDocument doc = PDDocument.load(new FileInputStream(src));
			if(doc.isEncrypted()){
				try {
					doc.decrypt("");
					doc.setAllSecurityToBeRemoved(true);
				} catch (CryptographyException e) {
					//System.out.println("Cannot decrypt");
					e.printStackTrace();
				}
				
				
			}
			List<PDPage> pages = doc.getDocumentCatalog().getAllPages();
			Iterator<PDPage> i = pages.iterator();
			int count = 1; 
			File img_dir = new File(jpg_path);
			if(!img_dir.exists()){
				img_dir.mkdirs();
			}
			while (i.hasNext()) {
				PDPage page = i.next();
				BufferedImage bi = page.convertToImage();
				ImageIO.write(bi, "jpg", new File(img_dir,"page_" + count + ".jpg"));
				
				count++;
			}
			//System.out.println("Conversion complete");
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}
	
	public void Zipfolder(String path){
		try {
			FileOutputStream f_out = new FileOutputStream(new File(path));
			ZipOutputStream zip_out = new ZipOutputStream(f_out);
			File dir = new File(jpg_path);
			File[] file_list = dir.listFiles();
			for(File file:file_list){
				//System.out.println("Adding file "+file.getName());
				FileInputStream each_file_is = new FileInputStream(file);
				byte[] buffer = new byte[(int)file.length()];
				
				zip_out.putNextEntry(new ZipEntry(file.getName()));
				each_file_is.read(buffer, 0, buffer.length);
				zip_out.write(buffer,0,buffer.length);
				each_file_is.close();
				file.delete();
			}
			zip_out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
