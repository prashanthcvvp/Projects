package com.backend;

import java.util.ArrayList;

import android.app.Application;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class GlobalAppData extends Application {
	/*********************************** Variables *******************************************/
	/*****************************************************************************/
	private static int[] mSampleRates = new int[] { 8000, 11025, 22050, 44100 };
	private static short[] aformats = new short[] {AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT };
	private static short[] chConfigs = new short[] {AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO };
	static byte[] buffer;
	static int buffer_size=0;	
	/******************************************************************************/	
	public static String tag ="medapp";
	public static ArrayList<String> intermediate_result_array;
	public static ArrayList<String> final_result_array;
	public static int steth_option_index=0;
	public static String path=Environment.getExternalStorageDirectory()+"/sample";
	public static String input_file="/sample.3gp";
	public static String output_file="/sample_filter.3gp";
	public static boolean cancel =false;	
	/******************************************************************************/
	/******************************************************************************/
	@Override
	public void onCreate() {
		super.onCreate();
		intermediate_result_array = new ArrayList<String>();
		final_result_array = new ArrayList<String>();
	}
	/******************************************************************************/
	public AudioRecord findAudioRecord() {
		for (int rate : mSampleRates) {
			for (short audioFormat : aformats) {
				for (short channelConfig : chConfigs) {
					try {

						int bufferSize = AudioRecord.getMinBufferSize(rate,channelConfig, audioFormat);

						if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
							Log.d("Log", "Attempting rate " + rate+ "Hz, bits: " + audioFormat+ ", channel: " + channelConfig);
							// check if we can instantiate and have a success
							AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, rate,channelConfig, audioFormat, bufferSize);
							Thread.sleep(1000);
							if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
								buffer = new byte[bufferSize];
								
								buffer_size=bufferSize;
								return recorder;
							}

						}
					} catch (Exception e) {
						Log.e("Log", rate + "Exception, keep trying.", e);
					}
				}
			}
		}
		return null;
	}
	/******************************************************************************/
	public static void createBuffer(){
		buffer = new byte[buffer_size];
	}
	/******************************************************************************/

}
