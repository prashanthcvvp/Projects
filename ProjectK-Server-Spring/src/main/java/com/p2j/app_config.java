package com.p2j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

public class app_config {
	
	@Bean
	@Qualifier("pdfbox")
	public PdftoJpg pdfInstance(){
		return new PdftoJpg();
	}

}
