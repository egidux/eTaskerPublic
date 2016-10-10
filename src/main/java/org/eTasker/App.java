package org.eTasker;

import org.eTasker.web.api.Register;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {Register.class})
public class App {
	//final static Logger lOGGER = Logger.getLogger(Main.class);
	
	public static void main(String[] args) {
		//lOGGER.info("Server starting..");
		SpringApplication.run(App.class, args);
		//lOGGER.info("Server started");
	}
}
