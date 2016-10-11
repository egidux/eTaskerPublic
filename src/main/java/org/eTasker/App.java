package org.eTasker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App  {
	//final static Logger lOGGER = Logger.getLogger(Main.class);
	
	public static void main(String[] args) {
		//lOGGER.info("Server starting..");
		SpringApplication.run(App.class, args);
		//lOGGER.info("Server started");
	}
}
