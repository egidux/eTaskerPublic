package org.eTasker.web.api;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.eTasker.WebApplication;
import org.eTasker.model.User;
import org.eTasker.service.UserManagementService;
import org.eTasker.tools.MapBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@EnableRedisHttpSession 
@RestController
@RequestMapping("user/api/")
public class UserManagementController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserManagementController.class);
	
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
    	try {
			LOGGER.info("Request /user/api/users -> " + new ObjectMapper().writeValueAsString(users));
		} catch (JsonProcessingException e) {
			LOGGER.debug("Failed create JSON string from object", e);
		}
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }
    
    @RequestMapping(
            value = "register",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(User user) {
    	LOGGER.info("Request /user/api/register with params: name=" + user.getName() + ", email=" + user.getEmail() + ", companyname=" +
    			user.getCompanyname() + ", password=" + user.getPassword());
    	if (user.getEmail() == null || user.getEmail().isEmpty()) {
    		LOGGER.debug("Request /user/api/register missing parameter email=" + user.getEmail());
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	if (userManagementService.findByEmail(user.getEmail()) != null) {
    		LOGGER.debug("Request /user/api/register failed, user exists with email=" + user.getEmail());
    		return new ResponseEntity<>(MapBuilder.build("error", "user exists"), HttpStatus.CONFLICT);
    	}
    	User newUser = userManagementService.create(user);
    	if (newUser == null) {
    		LOGGER.debug("Request /user/api/register failed userManagementService.create(user) for user with email=" + user.getEmail());
    		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	try {
    		email.sendEmail(newUser);
    	} catch (Exception e) {
    		userManagementService.delete(newUser);
    		return new ResponseEntity<>(MapBuilder.build("error", "failed to send email"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    
    @RequestMapping(
            value = "ver/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> confirm(@PathVariable("id") Long id) {
    	if (userManagementService.validate(id) != null) {
    		return new ResponseEntity<>("Thank You", HttpStatus.OK);
    	}
    	return new ResponseEntity<>(MapBuilder.build("error", "user with id=" + id + " dont exist"), HttpStatus.BAD_REQUEST);
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
    	if (!user.getIsver()) {
    		return new ResponseEntity<>(MapBuilder.build("error", "please validate registration"), HttpStatus.BAD_REQUEST);
    	}
    	session.setAttribute("Authorization", email);
    	return new ResponseEntity<>(HttpStatus.OK);
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
    	return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
