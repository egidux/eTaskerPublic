package org.eTasker.controller.web.api;

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
public class CustomerController extends AbstractController {
	
	private static final String URL_CUSTOMER = "customer";
	private static final String EMAIL_SUBJECT = "eTasker confirmation";
	private static final String EMAIL_TEXT = "https://localhost:8085/user/api/customer/";
	private static final String VERIFIED_MESSAGE = "Thank You";
	
	/**
	 * Creates new user/owner 
	 * @param user - new user to register
	 * @return if request successful returns  201(Created) and newly created user/owner Json data
	 * 		   if missing parameters returns  400(Bad Request) and error message as Json
	 * 		   if user already exists returns 409(Conflict) and error message as Json
	 * 		   if any other error returns     500(Internal Server Error) and error message as Json
	 */
	@RequestMapping(
            value = URL_CUSTOMER,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(User user) {
    	logger.info("Http request POST /user/api/" + URL_CUSTOMER + " with params: name=" + user.getName() + 
    			", email=" + user.getEmail() + ", companyname=" +
    			user.getCompanyname() + ", password=" + user.getPassword());
    	if (user.getEmail() == null || user.getEmail().isEmpty() || user.getCompanyname() == null || 
    			user.getCompanyname().isEmpty() || user.getName() == null || user.getName().isEmpty() ||
    			user.getPassword() == null || user.getPassword().isEmpty()) {
    		logger.debug("Http request /user/api/" + URL_CUSTOMER + "  missing parameters: " + JsonBuilder.build(user));
    		return new ResponseEntity<>(MapBuilder.build("error", "missing parameters"),
    				HttpStatus.BAD_REQUEST);
    	}
    	if (userService.findByEmail(user.getEmail()) != null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "user exists"), HttpStatus.CONFLICT);
    	}
    	User newUser = userService.create(user);
    	if (newUser == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "INTERNAL_SERVER_ERROR"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	new Thread(() -> {
    		if (!emailController.sendEmail(newUser.getEmail(), EMAIL_SUBJECT,
        				 EMAIL_TEXT + newUser.getId())) {
    			userService.delete(newUser);
    		}
        }).start();
    	return new ResponseEntity<>(JsonBuilder.build(newUser), HttpStatus.CREATED);
    }
    
	/**
	 * Verify email
	 * @param  id - new registered user/owner id to verify
	 * @return if request successful returns 200(OK) and VERIFIED_MESSAGE as Json
	 * 		   if unsuccesfull       returns 400(Bad Request) and error message as Json
	 */
    @RequestMapping(
            value = URL_CUSTOMER + "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> confirm(@PathVariable("id") Long id) {
    	logger.info("Http request GET /user/api/" + URL_CUSTOMER + "/{id} with id:" + id);
    	if (userService.validate(id) == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "id=" + id + " dont exist"), 
        			HttpStatus.BAD_REQUEST);
    	}
    	return new ResponseEntity<>(VERIFIED_MESSAGE, HttpStatus.OK);
    }
}
