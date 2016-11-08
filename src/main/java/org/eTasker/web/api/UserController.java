package org.eTasker.web.api;

import java.util.List;

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
	
	private static final String URL_USERS = "users";
	private static final String URL_LOGIN = "login";
	private static final String URL_LOGOUT = "logout";
	private static final String URL_PROFILE = "profile";

	/**
	 * Returns all users
	 * @return if request successful   returns 200(OK) and all users as Json
	 *         if request unsuccessful returns 500(Internal Server Error) and error message as Json
	 */
    @RequestMapping(
            value = URL_USERS,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUsers() {
    	logger.info("Http request GET /user/api/" + URL_USERS);
    	List<User> users = userService.findAll();
    	if (users == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "INTERNAL_SERVER_ERROR"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }
    
    /**
     * Log in and create session
     * @param email
     * @param password
     * @param session
     * @return if request successful   returns 204(No Content)
     *         if wrong email/password returns 400(Bad Requests) and error message as Json
     *         if email not verified   returns 401(Unauthorized) and error message as Json
     */
	@RequestMapping(
    		value = URL_LOGIN,
    		method = RequestMethod.POST,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestParam(value="email") String email, 
    		@RequestParam(value="password") String password, HttpSession session) {
		logger.info("Http request POST /user/api/" + URL_LOGIN);
    	User user = userService.findByEmail(email);
    	if (user == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "wrong email"), HttpStatus.BAD_REQUEST);
    	}
    	if (!user.getPassword().equals(password)) {
    		logger.debug("Http request POST /user/api/" + URL_LOGIN + " wrong password: " + password);
    		return new ResponseEntity<>(MapBuilder.build("error", "wrong password"), HttpStatus.BAD_REQUEST);
    	}
    	if (!user.getIsver()) {
    		logger.debug("Http request POST /user/api/" + URL_LOGIN + " user email not verified: " + email);
    		return new ResponseEntity<>(MapBuilder.build("error", "please validate registration"), 
    				HttpStatus.UNAUTHORIZED);
    	}
    	session.setAttribute("Authorization", email);
    	logger.info("Http request POST /user/api/" + URL_LOGIN + " session created: Authorization:" + email);
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
	/**
	 * Logout user/owner
	 * @param session
	 * @return if request successful returns 204(No Content)
	 *         if not logged in      returns 401(Unauthorized) and error message as Json
	 */
	@RequestMapping(
    		method = RequestMethod.POST,
    		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> logout(HttpSession session) {
		String email = getSessionAuthorization(session);
		if (email == null) {
			logger.debug("Http request POST /user/api/" + URL_LOGOUT + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
		session.invalidate();
		logger.info("Http request POST /user/api/" + URL_LOGOUT + " session invalidated for user: " + 
				email);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
	/**
	 * 
	 * @param user
	 * @param session
	 * @return
	 */
    @RequestMapping(
            value = URL_PROFILE,
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfile(User user, HttpSession session) {
    	logger.info("Http request PUT /user/api/" + URL_PROFILE + " with params: name=" + user.getName() + 
    			", email=" + user.getEmail());
    	String email = getSessionAuthorization(session);
    	if (email == null) {
    		logger.debug("Http request PUT /user/api/" + URL_PROFILE + " failed, not logged in");
    		return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
    	}
    	User updatedUser = userService.update(user, email); 
    	if (updatedUser == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "user does not exist"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	logger.info("Update for user=" + email + " success");
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @RequestMapping(
            value = "password",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@RequestParam(value="currentpassword") String currentPassword, 
    		@RequestParam(value="newpassword") String newPassword, @RequestParam(value="confirmpassword") 
    		String confirmPassword, HttpSession session) {
    	logger.info("Http request PUT /user/api/password with params: currentpassword=" + currentPassword + 
    			", newpassword=" + newPassword + ", confirmpassword=" + confirmPassword);
    	String email = getSessionAuthorization(session);
    	if (email == null) {
    		logger.debug("Http request PUT /user/api/password failed, please login");
    		return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
    	}
    	if (!newPassword.equals(confirmPassword)) {
    		logger.debug("Http request PUT /user/api/password new password dont match -> newpassword:" +
    				newPassword + " & confirmpassword:" + confirmPassword);
    		return new ResponseEntity<>(MapBuilder.build("error", "new password doesn't match"), HttpStatus.BAD_REQUEST);
    	}
    	User user = userService.changePassword(email, currentPassword, newPassword);
    	if (user == null) {
    		logger.debug("Http request PUT /user/api/password failed");
    		return new ResponseEntity<>(MapBuilder.build("error", "check password"), 
    				HttpStatus.BAD_REQUEST);
    	}
    	logger.info("Password changed for user=" + email);
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
