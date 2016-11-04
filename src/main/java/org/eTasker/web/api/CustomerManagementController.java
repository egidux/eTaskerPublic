package org.eTasker.web.api;

import java.util.List;

import org.eTasker.model.User;
import org.eTasker.tool.JsonBuilder;
import org.eTasker.tool.MapBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerManagementController extends AbstractController {

	//for testing
    @RequestMapping(
            value = "users",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUsers() {
    	List<User> users = userManagementService.findAll();
    	logger.info("Http request /user/api/users -> " + JsonBuilder.build(users));
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }
    
    @RequestMapping(
            value = "register",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(User user) {
    	logger.info("Http request /user/api/register with params: name=" + user.getName() + 
    			", email=" + user.getEmail() + ", companyname=" +
    			user.getCompanyname() + ", password=" + user.getPassword());
    	if (user.getEmail() == null || user.getEmail().isEmpty() || user.getCompanyname() == null || 
    			user.getCompanyname().isEmpty() || user.getName() == null || user.getName().isEmpty() ||
    			user.getPassword() == null || user.getPassword().isEmpty()) {
    		logger.debug("Http request /user/api/register missing parameters: " + JsonBuilder.build(user));
    		return new ResponseEntity<>(MapBuilder.build("error", "missing parameters"),
    				HttpStatus.BAD_REQUEST);
    	}
    	if (userManagementService.findByEmail(user.getEmail()) != null) {
    		logger.debug("Http request /user/api/register failed, user exists with email=" + user.getEmail());
    		return new ResponseEntity<>(MapBuilder.build("error", "user with this email exists"), 
    				HttpStatus.CONFLICT);
    	}
    	User newUser = userManagementService.create(user);
    	if (newUser == null) {
    		logger.debug("Http request /user/api/register failed userManagementService.create(user) for user with email=" + 
    				user.getEmail());
    		return new ResponseEntity<>(MapBuilder.build("error", "INTERNAL_SERVER_ERROR"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	 
    	new Thread(() -> {
    		try {
        		email.sendEmail(newUser);
        		logger.info("Http request /user/api/register email sent to: " + user.getEmail());
        	} catch (Exception e) {
        		logger.debug("Http request /user/api/register failed to sent email to: " + user.getEmail());
        		userManagementService.delete(newUser);
        	}
    	}).start();
    	
    	logger.debug("Http request /user/api/register created new user with email: " + newUser.getEmail());
    	return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    @RequestMapping(
            value = "ver/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> confirm(@PathVariable("id") Long id) {
    	if (userManagementService.validate(id) != null) {
    		logger.info("Http request /user/api/ver{id} user with id: " + id + " verified");
    		return new ResponseEntity<>("Thank You", HttpStatus.OK);
    	}
    	logger.debug("Http request /user/api/ver{id} failed verify user with id: " + id);
    	return new ResponseEntity<>(MapBuilder.build("error", "user with id=" + id + " dont exist"), HttpStatus.BAD_REQUEST);
    }
}
