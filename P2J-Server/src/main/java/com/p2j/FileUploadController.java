package com.p2j;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

	private PdftoJpg pdf_box;

	private static String alphabets = "abcdefghijklmnopqrstuvwxyz";
	private Random rand = new Random();

	@RequestMapping(value = "/PDFServlet", method = RequestMethod.GET)
	public @ResponseBody String uploadInfo() {
		return "Website Under Construction v 2.0";
	}

	@RequestMapping(value = "/PDFServlet", method = RequestMethod.POST)
	public @ResponseBody void handleFileUpload(
			@RequestParam("name") String name,
			@RequestParam("file") MultipartFile file,
			HttpServletResponse response) {
		if (!file.isEmpty()) {
			pdf_box = new PdftoJpg();
			try {
				byte[] bytes = file.getBytes();
				//String base_dir = System.getenv("OPENSHIFT_DATA_DIR");
				String base_dir= System.getenv("HOME");
				name = String.valueOf(alphabets.charAt(rand.nextInt(alphabets
						.length())))
						+ String.valueOf(alphabets.charAt(rand
								.nextInt(alphabets.length())))
						+ String.valueOf(alphabets.charAt(rand
								.nextInt(alphabets.length())));
				// base_dir="/home/prashanth/Desktop/SamplePDF";
				File pdf_file=new File(base_dir + "/" + name+ ".pdf");
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(pdf_file));
				bos.write(bytes);
				bos.close();

				BufferedOutputStream b_os_network = new BufferedOutputStream(response.getOutputStream());

				pdf_box.convertPDFToJPG(base_dir + "/" + name + ".pdf");
				pdf_box.Zipfolder(base_dir + "/" + name + ".zip");

				File file_to_client = new File(base_dir + "/" + name + ".zip");
				FileInputStream fis = new FileInputStream(file_to_client);
				byte[] file_to_client_bytes = new byte[(int) file_to_client.length()];
				fis.read(file_to_client_bytes, 0, file_to_client_bytes.length);
				b_os_network.write(file_to_client_bytes);
				b_os_network.flush();
				b_os_network.close();

				pdf_file.delete();
				file_to_client.delete();
				System.out.println("Success");
				
				// Process p = Runtime.getRuntime().exec("ctl_all restart");
			} catch (IOException e) {
				System.out.println("IO exception 3");
				// e.printStackTrace();
			}
		} else {
		}
	}
}
