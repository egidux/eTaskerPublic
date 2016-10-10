package eTasker.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import eTasker.api.LogOut;
import eTasker.api.Login;
import eTasker.api.Register;

@SpringBootApplication(scanBasePackageClasses = {Register.class, LogOut.class, Login.class})
public class App {
	//final static Logger lOGGER = Logger.getLogger(Main.class);
	
	public static void main(String[] args) {
		//lOGGER.info("Server starting..");
		SpringApplication.run(App.class, args);
		//lOGGER.info("Server started");
	}
}
