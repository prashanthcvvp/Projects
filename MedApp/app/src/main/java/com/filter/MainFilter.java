package com.filter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.backend.GlobalAppData;

public class MainFilter {
	public static void filter(ByteArrayOutputStream byte_buf) {
		MainFilter Filter = new MainFilter();
		try {
			//File file = new File(file_name);
			//FileInputStream fis = new FileInputStream(file);
			FileOutputStream fos = new FileOutputStream(new File(GlobalAppData.path+"_filter.wav"));

			//byte[] file_data = new byte[(int) file.length()];

			//fis.read(file_data, 0, file_data.length);
			byte[] file_data = byte_buf.toByteArray();
			ByteArrayOutputStream baos=null;
			if(GlobalAppData.steth_option_index==1){
				baos = Filter.filter_bytes(file_data,900);
			}else if(GlobalAppData.steth_option_index==2){
				baos = Filter.filter_bytes(file_data,1000);
			}
			fos.write(baos.toByteArray());
			baos.close();
			fos.close();
			//fis.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ByteArrayOutputStream filter_bytes(byte[] file_bytes,int cut_off_freq) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			Butterworth butter = new Butterworth(3, PassbandType.LOWPASS, 0,(cut_off_freq/1000), 0.025);
			
			double[] raw_data = new double[(int) (file_bytes.length / 2)];

			baos.write(file_bytes, 0, 44);

			int j = 0;
			for (int k = 44; k < file_bytes.length - 1; k = k + 2) {
				raw_data[j++] = (double) (file_bytes[k + 1] << 8 | file_bytes[k] & 0xFF) / 32767;
			}

			double[] filtered_ouput = butter.filter_output(raw_data);
			for (int k = 0; k < filtered_ouput.length; k++) {

				short value = (short) (filtered_ouput[k] * 32767);
				baos.write((byte) (value & 0xFF));
				baos.write((value >> 8 & 0xFF));
			}

			baos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("FIltered");
		return baos;

	}
}
