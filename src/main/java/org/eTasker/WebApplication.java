package org.eTasker;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebApplication  {
	
	public WebApplication() {
		File dir = new File("images");
		if (dir.exists()) {
			File[] files = dir.listFiles();
			for(File file: files) {
				file.delete();
			}
		}
		dir.mkdir();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}
}
