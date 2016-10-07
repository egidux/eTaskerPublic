package eTasker.main;

import org.apache.log4j.Logger;

public class Main {
	final static Logger lOGGER = Logger.getLogger(Main.class);
	
	public static void main(String[] args) {
		lOGGER.debug("Starting main class");
		System.out.println("Hello");
		lOGGER.info("Finishing main class");
	}
}
