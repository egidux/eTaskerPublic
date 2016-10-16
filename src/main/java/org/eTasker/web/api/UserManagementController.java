package org.eTasker.web.api;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.eTasker.model.User;
import org.eTasker.service.UserManagementService;
import org.eTasker.tools.MapBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@EnableRedisHttpSession 
@RestController
@RequestMapping("user/api/")
public class UserManagementController {
	
	@Autowired
	UserManagementService userManagementService;
	@Autowired
	EmailController email;
	
    @RequestMapping(
            value = "users",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUsers() {
    	List<User> users = userManagementService.findAll();
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }
    
    @RequestMapping(
            value = "register",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(User user) {
    	if (user.getEmail() == null || user.getEmail().isEmpty()) {
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	User newUser = userManagementService.create(user);
    	if (newUser == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "user exists"), HttpStatus.CONFLICT);
    	}
    	try {
    		email.sendEmail(newUser);
    	} catch (Exception e) {
    		return new ResponseEntity<>(MapBuilder.build("error", "failed to send email"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
    
    @RequestMapping(
            value = "ver/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> confirm(@PathVariable("id") Long id) {
    	userManagementService.validate(id);
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @RequestMapping(
    		value = "login",
    		method = RequestMethod.POST,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestParam(value="email") String email, 
    		@RequestParam(value="password") String password, HttpSession session) {
    	User user = userManagementService.findByEmail(email);
    	if (user == null || !user.getPassword().equals(password)) {
    		return new ResponseEntity<>(MapBuilder.build("error", "wrong email or password"), 
    				HttpStatus.BAD_REQUEST);
    	}
    	session.setAttribute("Authorization", email);
    	return new ResponseEntity<>("", HttpStatus.OK);
    }
    
	@RequestMapping("/logout")
	void logout(HttpSession session) {
		session.invalidate();
	}
    
    @RequestMapping(
    		value = "test",
    		method = RequestMethod.GET,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> test(HttpSession session) {
    	Object email = session.getAttribute("Authorization");
    	if (email == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
    	}
    	return new ResponseEntity<>(HttpStatus.OK);
    }
}
