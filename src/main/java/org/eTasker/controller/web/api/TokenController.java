package org.eTasker.controller.web.api;

import javax.servlet.http.HttpSession;

import org.eTasker.model.Token;
import org.eTasker.service.TokenService;
import org.eTasker.tool.JsonBuilder;
import org.eTasker.tool.MapBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController extends AbstractController {

	private static final String URL_TOKEN = "tokens";
	
	@Autowired
	private TokenService tokenService;
	
    /**
     * Creates new token
     * @param token
     * @param session
     * @return if request successful returns  201(Created) and newly created token Json data
     * 		   if unauthorized returns        401(Unauthorized) and error message as Json
     */
    @RequestMapping(
            value = URL_TOKEN,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createToken(Token token, HttpSession session) {
    	logger.info("Http request POST /user/api/" + URL_TOKEN + " with wtoken: " + JsonBuilder.build(token));
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request POST /user/api/" + URL_TOKEN + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	Token tokenCreated = tokenService.create(token);
    	if (tokenCreated == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "INTERNAL_SERVER_ERROR"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<>(JsonBuilder.build(token), HttpStatus.CREATED);
    }
}
