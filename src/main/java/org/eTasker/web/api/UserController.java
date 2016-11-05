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
public class UserController extends AbstractController {
    
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
    		logger.debug("Http request /user/api/login user email not verified: " + email);
    		return new ResponseEntity<>(MapBuilder.build("error", "please validate registration"), HttpStatus.BAD_REQUEST);
    	}
    	session.setAttribute("Authorization", email);
    	logger.info("Http request /user/api/login created session: Authorization:" + email);
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
	@RequestMapping("logout")
	public ResponseEntity<?> logout(HttpSession session) {
		String email = getSessionAuthorization(session);
		logger.info("Http request /user/api/logout sesission invalidated for user: " + 
				email);
		if (email == null) {
			logger.info("Http request /user/api/logout not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
		session.invalidate();
		return new ResponseEntity<>(HttpStatus.OK);
	}
    
    @RequestMapping(
            value = "updateprofile",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfile(User user, HttpSession session) {
    	logger.info("Http request /user/api/updateprofile with params: name=" + user.getName() + 
    			", email=" + user.getEmail());
    	String email = getSessionAuthorization(session);
    	if (email == null) {
    		logger.debug("Http request /user/api/updateprofile failed, not logged in");
    		return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
    	}
    	User updatedUser = userManagementService.update(user, email); 
    	if (updatedUser == null) {
    		logger.debug("Http request /user/api/updateprofile failed update user");
    		return new ResponseEntity<>(MapBuilder.build("error", "user does not exist"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	logger.info("Update for user=" + email + " success");
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @RequestMapping(
            value = "changepassword",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@RequestParam(value="currentpassword") String currentPassword, 
    		@RequestParam(value="newpassword") String newPassword, @RequestParam(value="confirmpassword") 
    		String confirmPassword, HttpSession session) {
    	logger.info("Http request /user/api/changepassword with params: currentpassword=" + currentPassword + 
    			", newpassword=" + newPassword + ", confirmpassword=" + confirmPassword);
    	String email = getSessionAuthorization(session);
    	if (email == null) {
    		logger.debug("Http request /user/api/changePassword failed, please login");
    		return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
    	}
    	if (!newPassword.equals(confirmPassword)) {
    		logger.debug("Http request /user/api/changePassword new password dont match -> newpassword:" +
    				newPassword + " & confirmpassword:" + confirmPassword);
    		return new ResponseEntity<>(MapBuilder.build("error", "new password doesn't match"), HttpStatus.BAD_REQUEST);
    	}
    	User user = userManagementService.changePassword(email, currentPassword, newPassword);
    	if (user == null) {
    		logger.debug("Http request /user/api/updatepassword failed");
    		return new ResponseEntity<>(MapBuilder.build("error", "check password"), 
    				HttpStatus.BAD_REQUEST);
    	}
    	logger.info("Password changed for user=" + email);
    	return new ResponseEntity<>(HttpStatus.OK);
    }
}
