package eTasker.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eTasker.entity.User;

@RestController
public class Register {
	
	@RequestMapping(
			value = "/register",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public User register(@RequestParam(value="name", defaultValue="") String name) {
		
		return new User(name);
	}
}
