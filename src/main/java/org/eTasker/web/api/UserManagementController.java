package org.eTasker.web.api;

import java.util.List;

import org.eTasker.model.User;
import org.eTasker.service.UserManagementService;
import org.eTasker.tools.MapBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


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
}
