package com.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PDFServlet
 */
@WebServlet("/PDFServlet")
public class PDFServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String alphabets = "abcdefghijklmnopqrstuvwxyz";
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PDFServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		pw.println("Web Application Under Construction -- Only for tablet use.");
		pw.close();

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		Random rand = new Random();
		String filename =String.valueOf(alphabets.charAt(rand.nextInt(alphabets.length())))+String.valueOf(alphabets.charAt(rand.nextInt(alphabets.length())));
		BufferedInputStream b_fis = new BufferedInputStream(request.getInputStream());
		String path_env = System.getenv("OPENSHIFT_DATA_DIR");
		String path =path_env+"/"+filename+".pdf";
		File myfile = new File(path);
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myfile));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i=0;
		byte[] buf = new byte[1024];
		int readBytes=0;
			
			while ((readBytes=b_fis.read(buf))!=-1) {
				baos.write(buf, 0, readBytes);
				i=i+buf.length;
			}
		
		bos.write(baos.toByteArray());
		bos.flush();
		bos.close();
		
		PdftoJpg pdf = new PdftoJpg();
		pdf.selectPdf(path);
		myfile.delete();
		// ZIP and sending it to client.
		String path_folder=path_env+"/"+filename+".zip";
		pdf.Zipfolder(path_folder);
		File my_file = new File(path_folder);
		FileInputStream fis = new FileInputStream(my_file);
		BufferedInputStream f_bis = new BufferedInputStream(fis);
		byte[] buf_zip = new byte[(int)my_file.length()];
		System.out.println("Buffer size "+ buf_zip.length);
		
		BufferedOutputStream b_os = new BufferedOutputStream(response.getOutputStream());
		f_bis.read(buf_zip, 0, buf_zip.length);
		b_os.write(buf_zip, 0, buf_zip.length);
		b_os.flush();
		f_bis.close();
		b_os.close();
		my_file.delete();
		System.out.println("success");
	}

}
