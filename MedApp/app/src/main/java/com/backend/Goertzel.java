package com.backend;


/**
 *
 * @author SaiRam
 */
public class Goertzel {

    /**
     * @param args the command line arguments
     */
	static float[] lowFreq = new float[] { 697.0F, 770.0F, 852.0F, 941.0F };
	static float[] highFreq = new float[] { 1209.0F, 1336.0F, 1477.0F, 1633.0F };
	static float[] dtmfTones = new float[] { 697.0F, 770.0F, 852.0F, 941.0F, 1209.0F,
			1336.0F, 1477.0F, 1633.0F };
	static int dtmfBoard[][] = { { 1, 2, 3, 12 }, { 4, 5, 6, 13 }, { 7, 8, 9, 14 },{ 10, 0, 11, 15 } };
	static byte[] buffer = new byte[2048];
	static int sampleRate = 8000;
	String tone_name=null;
	
	public String getTone(byte[] buffer1) {
		this.buffer = buffer1;
		double[] buf;
        Integer value=0;

		buf = new double[buffer.length / 2];

		for (int j = 0; j < ((buffer.length / 2) - 1); j++) {
            value = ((buffer[j * 2] & 0xFF) | (buffer[j * 2 + 1] << 8));
			buf[j] = (double) value.shortValue();
		}
		int tone = findDTMF(buf);
		
		if (tone >= 0) {

			if (tone < 10) {
				tone_name= String.valueOf(tone);
			}else if (tone == 12) {
				tone_name="A";
			}else if (tone == 13) {
				tone_name="B";
			}else if (tone == 14) {
				tone_name="C";
			}else if (tone == 15) {
				tone_name="D";
			}else if (tone == 10) {
				tone_name="*";
			}else if (tone == 11) {
				tone_name="#";
			}
			
		}if(tone<0){
			tone_name="nv";
		}
		return tone_name;
	}

	/*
	 * Check if sample has dtmf tone
	 */
	public static int findDTMF(double[] samples) {
		double[] goertzelValues = new double[8];
		double lowFreqValue = 0;
		int lowFreq = 0;
		double highFreqValue = 0;
		int highFreq = 0;
		boolean flag =false;


		for (int i = 0; i < 8; i++) {
			goertzelValues[i] = goertzel(samples, dtmfTones[i]);
		}
		for (int i = 0; i < 4; i++) // Find st?rste low frequency
		{
			
			//if (goertzelValues[i] > lowFreqValue) {
			if (goertzelValues[i] > lowFreqValue&& (goertzelValues[i] > 200)) {
				 
				lowFreqValue = goertzelValues[i];
				lowFreq = i;
				flag=true;
			}
		}
		for (int i = 4; i < 8; i++) // Find st?rste high frequency
		{
			
			//if (goertzelValues[i] > highFreqValue) {
			
			if ((goertzelValues[i] > highFreqValue) && (goertzelValues[i] > 200)) {
				highFreqValue = goertzelValues[i];
				highFreq = i - 4;
				flag=true;
			}
		}
		if(flag){
		return dtmfBoard[lowFreq][highFreq]; // Returner DTMF tone
		}else{
			return -1;
		}
	}

	public static double goertzel(double[] samples, float freq) {
		double vkn = 0;
		double vkn1 = 0;
		double vkn2 = 0;

		for (int j = 0; j < 256; j++) {
			vkn = ((2* Math.cos(2 * (Math.PI* (freq /sampleRate))) * vkn1) - vkn2) + samples[j];
			vkn2 = vkn1;
			vkn1 = vkn;
			
		}
		double WNk = Math.exp(-2 * Math.PI* (freq * (samples.length / sampleRate)) / samples.length);

		return Math.abs(20*Math.log10(vkn2*vkn2+vkn1*vkn1-WNk*vkn1*vkn2));
		

	}
}

