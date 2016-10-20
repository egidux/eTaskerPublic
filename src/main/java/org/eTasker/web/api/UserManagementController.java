package org.eTasker.web.api;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.eTasker.controller.EmailController;
import org.eTasker.model.User;
import org.eTasker.service.UserManagementService;
import org.eTasker.tool.JsonBuilder;
import org.eTasker.tool.MapBuilder;
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

@EnableRedisHttpSession 
@RestController
@RequestMapping("user/api/")
public class UserManagementController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserManagementController.class);
	
	@Autowired
	UserManagementService userManagementService;
	@Autowired
	EmailController email;
	
	//for testing
    @RequestMapping(
            value = "users",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUsers() {
    	List<User> users = userManagementService.findAll();
    	LOGGER.info("Http request /user/api/users -> " + JsonBuilder.build(users));
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }
    
    @RequestMapping(
            value = "register",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(User user) {
    	LOGGER.info("Http request /user/api/register with params: name=" + user.getName() + 
    			", email=" + user.getEmail() + ", companyname=" +
    			user.getCompanyname() + ", password=" + user.getPassword());
    	if (user.getEmail() == null || user.getEmail().isEmpty() || user.getCompanyname() == null || 
    			user.getCompanyname().isEmpty() || user.getName() == null || user.getName().isEmpty() ||
    			user.getPassword() == null || user.getPassword().isEmpty()) {
    		LOGGER.debug("Http request /user/api/register missing parameters: " + JsonBuilder.build(user));
    		return new ResponseEntity<>(MapBuilder.build("error", "missing parameters"),
    				HttpStatus.BAD_REQUEST);
    	}
    	if (userManagementService.findByEmail(user.getEmail()) != null) {
    		LOGGER.debug("Http request /user/api/register failed, user exists with email=" + user.getEmail());
    		return new ResponseEntity<>(MapBuilder.build("error", "user with this email exists"), 
    				HttpStatus.CONFLICT);
    	}
    	User newUser = userManagementService.create(user);
    	if (newUser == null) {
    		LOGGER.debug("Http request /user/api/register failed userManagementService.create(user) for user with email=" + 
    				user.getEmail());
    		return new ResponseEntity<>(MapBuilder.build("error", "INTERNAL_SERVER_ERROR"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	 
    	new Thread(() -> {
    		try {
        		email.sendEmail(newUser);
        		LOGGER.info("Http request /user/api/register email sent to: " + user.getEmail());
        	} catch (Exception e) {
        		LOGGER.debug("Http request /user/api/register failed to sent email to: " + user.getEmail());
        		userManagementService.delete(newUser);
        	}
    	}).start();
    	
    	LOGGER.debug("Http request /user/api/register created new user with email: " + newUser.getEmail());
    	return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    @RequestMapping(
            value = "ver/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> confirm(@PathVariable("id") Long id) {
    	if (userManagementService.validate(id) != null) {
    		LOGGER.info("Http request /user/api/ver{id} user with id: " + id + " verified");
    		return new ResponseEntity<>("Thank You", HttpStatus.OK);
    	}
    	LOGGER.debug("Http request /user/api/ver{id} failed verify user with id: " + id);
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
    		LOGGER.debug("Http request /user/api/login wrong email: " + email + " or password: " + password);
    		return new ResponseEntity<>(MapBuilder.build("error", "wrong email or password"), 
    				HttpStatus.BAD_REQUEST);
    	}
    	if (!user.getIsver()) {
    		LOGGER.debug("Http request /user/api/register user email not verified: " + email);
    		return new ResponseEntity<>(MapBuilder.build("error", "please validate registration"), HttpStatus.BAD_REQUEST);
    	}
    	session.setAttribute("Authorization", email);
    	LOGGER.info("Http request /user/api/register created session: Authorization, " + email);
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
	@RequestMapping("/logout")
	public ResponseEntity<?> logout(HttpSession session) {
		LOGGER.info("Http request /user/api/logout sesission invalidated for user: " + 
				session.getAttribute("Authorization"));
		session.invalidate();
		return new ResponseEntity<>(HttpStatus.OK);
	}
    
	//for testing
    @RequestMapping(
    		value = "testsession",
    		method = RequestMethod.GET,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> test(HttpSession session) {
    	Object email = session.getAttribute("Authorization");
    	if (email == null) {
    		LOGGER.debug("Http request /user/api/testsession no session created");
    		return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
    	}
    	LOGGER.info("Http request /user/api/testsession session created for:" + email);
    	return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
