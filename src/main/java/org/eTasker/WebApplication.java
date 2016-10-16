package org.eTasker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
public class WebApplication  {
	//final static Logger lOGGER = Logger.getLogger(Main.class);
	
	public static void main(String[] args) {
		//lOGGER.info("Server starting..");
		SpringApplication.run(WebApplication.class, args);
		//lOGGER.info("Server started");
	}
}
