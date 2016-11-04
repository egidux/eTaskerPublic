package org.eTasker.web.api;

import javax.servlet.http.HttpSession;

import org.eTasker.model.User;
import org.eTasker.tool.MapBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserManagementController extends AbstractController {
    
	@RequestMapping(
    		value = "login",
    		method = RequestMethod.POST,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestParam(value="email") String email, 
    		@RequestParam(value="password") String password, HttpSession session) {
    	User user = userManagementService.findByEmail(email);
    	if (user == null || !user.getPassword().equals(password)) {
    		logger.debug("Http request /user/api/login wrong email: " + email + " or password: " + password);
    		return new ResponseEntity<>(MapBuilder.build("error", "wrong email or password"), 
    				HttpStatus.BAD_REQUEST);
    	}
    	if (!user.getIsver()) {
    		logger.debug("Http request /user/api/register user email not verified: " + email);
    		return new ResponseEntity<>(MapBuilder.build("error", "please validate registration"), HttpStatus.BAD_REQUEST);
    	}
    	session.setAttribute("Authorization", email);
    	logger.info("Http request /user/api/register created session: Authorization:" + email);
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
	@RequestMapping("/logout")
	public ResponseEntity<?> logout(HttpSession session) {
		logger.info("Http request /user/api/logout sesission invalidated for user: " + 
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
    		logger.debug("Http request /user/api/testsession no session created");
    		return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
    	}
    	logger.info("Http request /user/api/testsession OK, session created for:" + email);
    	return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
