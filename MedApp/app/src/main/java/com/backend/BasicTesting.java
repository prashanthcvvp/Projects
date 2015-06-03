package com.backend;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.filter.MainFilter;

import android.media.AudioRecord;
import android.media.ToneGenerator;
import android.util.Log;

public class BasicTesting {
	/******************************************************************************/
	private ToneGenerator tone_generator;
	private AudioRecord recorder;
	private Goertzel goertzel;

	/******************************************************************************/
	public BasicTesting(AudioRecord recorder) {
		tone_generator = new ToneGenerator(0, ToneGenerator.MAX_VOLUME);
		this.recorder = recorder;
		goertzel = new Goertzel();
	}

	/******************************************************************************/
	public StringBuilder pulseOximeter() {
		tone_generator.startTone(ToneGenerator.TONE_DTMF_2, 500);
		try {
			Thread.sleep(502);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.recorder.startRecording();
		GlobalAppData.intermediate_result_array.clear();
		GlobalAppData.final_result_array.clear();
		int type = 0;
		int final_data_count = 0;
		String final_data_value = "";
		GlobalAppData.cancel=false;
		while (true) {
			String value = null;
			GlobalAppData.createBuffer();

			this.recorder.read(GlobalAppData.buffer, 0,GlobalAppData.buffer.length);

			value = goertzel.getTone(GlobalAppData.buffer);

			if (value != "nv") { // To check whether there is any valid value.
				if (value.equalsIgnoreCase("B")) {
					final_data_value = "";
					type = 1;
					Log.d(GlobalAppData.tag, "Type " + String.valueOf(type));
				} else if (value.equalsIgnoreCase("A")) {
					final_data_value = "";
					type = 2;
					Log.d(GlobalAppData.tag, "Type " + String.valueOf(type));
				} else if (value.equalsIgnoreCase("C")) {
					final_data_value = "";
					type = 3;
					Log.d(GlobalAppData.tag, "Type " + String.valueOf(type));
				} else if (type == 1) {
					final_data_value += value;
					if (final_data_value.length() == 3) {
						GlobalAppData.intermediate_result_array.add(final_data_value);
						Log.d(GlobalAppData.tag, "Type " + final_data_value);
						final_data_value = "";
					}
				} else if (type == 2) {
					final_data_value += value;
					if (final_data_value.length() == 3) {
						GlobalAppData.final_result_array.add(final_data_value);
						Log.d(GlobalAppData.tag, "Type " + final_data_value);
						final_data_value = "";
					}
					final_data_count++;

					if (final_data_count == 6) {
						break;
					}
				} else if (type == 3) {
					this.recorder.stop();
					Log.d(GlobalAppData.tag, "Error");
					break;

				}

			}
			
			if(GlobalAppData.cancel){
				this.recorder.stop();
				break;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < GlobalAppData.intermediate_result_array.size(); i++) {
			if ((i % 2) == 0) {
				builder.append("\nPulse Ox Intermediate Data :");
			}
			builder.append(" ");
			builder.append(GlobalAppData.intermediate_result_array.get(i));
		}

		for (int i = 0; i < GlobalAppData.final_result_array.size(); i++) {
			if (i == 0) {
				builder.append("\nPulse Ox Final Data :");
			}
			builder.append(" ");
			builder.append(GlobalAppData.final_result_array.get(i));
		}
		if ((GlobalAppData.final_result_array.size() == 0)|| (GlobalAppData.intermediate_result_array.size() == 0)) {
			builder.append("Error");
		}
		return builder;
	}

	/******************************************************************************/
	public StringBuilder stethoscopeStreaming() {
		tone_generator.startTone(ToneGenerator.TONE_DTMF_1, 500);

		StringBuilder builder = new StringBuilder();
		File steth_file = new File(GlobalAppData.path);
		try {
			FileOutputStream fos = new FileOutputStream(steth_file);

			/******* Steth Audio Processing *******/
			
			ByteArrayOutputStream byte_buff = new ByteArrayOutputStream();
			this.recorder.startRecording();
			GlobalAppData.cancel=false;
			while (true) {
				GlobalAppData.createBuffer();
				this.recorder.read(GlobalAppData.buffer, 0,GlobalAppData.buffer_size);
				byte_buff.write(GlobalAppData.buffer, 0,GlobalAppData.buffer_size);

				if (GlobalAppData.cancel) {
					tone_generator.startTone(ToneGenerator.TONE_DTMF_0, 500);
					this.recorder.stop();
					fos.write(byte_buff.toByteArray(), 0, byte_buff.size());
					fos.close();
					
					builder.append("Success");
					//MainFilter.filter(byte_buff);
					Log.d(GlobalAppData.tag, builder.toString());
					return builder;

				}
			}

		} catch (IOException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
		//this.recorder.release();
		return null;

	}
	/******************************************************************************/
}
