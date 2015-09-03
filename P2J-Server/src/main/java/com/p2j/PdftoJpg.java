package com.p2j;

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

import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.exceptions.InvalidPasswordException;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.encryption.BadSecurityHandlerException;
import org.apache.pdfbox.pdmodel.encryption.StandardDecryptionMaterial;

public class PdftoJpg {
	// allow images selection for converting
	//private static String path_env = System.getenv("OPENSHIFT_DATA_DIR");
	private static String path_env = System.getenv("HOME");
	//private static String path_env = "/home/prashanth/Desktop/SamplePDF/";
	private static String jpg_path = path_env + "/SamplePDF/";

	public static void selectPdf(String path) {
		File file = new File(path);
		convertPDFToJPG(file.toString());
	}

	public static void convertPDFToJPG(String src) {
		PDDocument doc = null;
		try {
			doc = PDDocument.load(new FileInputStream(src));
			if (doc.isEncrypted()) {
				try {
					
					doc.openProtection(new StandardDecryptionMaterial(""));
					doc.setAllSecurityToBeRemoved(true);
				} catch (CryptographyException | BadSecurityHandlerException e) {
					System.out.println("Could not decrypt the package");
				}
				
			}
			List<PDPage> pages = doc.getDocumentCatalog().getAllPages();
			Iterator<PDPage> i = pages.iterator();
			int count = 1;
			File img_dir = new File(jpg_path);
			if (!img_dir.exists()) {
				img_dir.mkdirs();
			}
			BufferedImage bi; 
			while (i.hasNext()) {
				PDPage page = i.next();
				bi= page.convertToImage();
				File file_img = new File(img_dir, "page_" + count+ ".jpg");
				ImageIO.write(bi, "jpg",file_img );
			
				count++;
				
			}
			doc.close();
		} catch (IOException ie) {
			System.out.println("IO exception 1");
			//ie.printStackTrace();
		}
	}

	public void Zipfolder(String path) {
		try {
			FileOutputStream f_out = new FileOutputStream(new File(path));
			ZipOutputStream zip_out = new ZipOutputStream(f_out);
			File dir = new File(jpg_path);
			File[] file_list = dir.listFiles();
			for (File file : file_list) {
				FileInputStream each_file_is = new FileInputStream(file);
				byte[] buffer = new byte[(int) file.length()];

				zip_out.putNextEntry(new ZipEntry(file.getName()));
				each_file_is.read(buffer, 0, buffer.length);
				zip_out.write(buffer, 0, buffer.length);
				each_file_is.close();
				file.delete();
			}
			zip_out.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found exception");
			//e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Exception 2");	
			//e.printStackTrace();
		}
	}

}
