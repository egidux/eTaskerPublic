package org.eTasker.web.api;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.eTasker.model.Client;
import org.eTasker.service.ClientService;
import org.eTasker.tool.JsonBuilder;
import org.eTasker.tool.MapBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController extends AbstractController {
	private static final String URL_CLIENTS = "clients";

	@Autowired
	protected ClientService clientService;
	
	/**
	 * Retrieves all clients
	 @return if request successful   returns 200(OK) and all users as Json
	 *       if request unsuccessful returns 500(Internal Server Error) and error message as Json
	 */
    @RequestMapping(
            value = URL_CLIENTS,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getClients(HttpSession session) {
    	logger.info("Http request GET /user/api/" + URL_CLIENTS);
    	String email = getSessionAuthorization(session);
		if (email == null) {
			logger.info("Http request GET /user/api/" + URL_CLIENTS + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	List<Client> clients = clientService.findAll();
    	if (clients == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "INTERNAL_SERVER_ERROR"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
		return new ResponseEntity<List<Client>>(clients, HttpStatus.OK);
    }
    
    /**
     * Retrives specific client
     * @param id
     * @return if request successful returns   200(OK) and worker as Json
     *         if request unsuccessful returns 400(Bad Request) and error message as Json
     */
    @RequestMapping(
            value = URL_CLIENTS + "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getClient(@PathVariable("id") Long id) {
    	logger.info("Http request GET /user/api/" + URL_CLIENTS + "/{id} with id:" + id);
    	Client client = clientService.findOne(id);
    	if (client == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "No client found with id=" + id), 
    				HttpStatus.BAD_REQUEST);
    	}
    	return new ResponseEntity<>(JsonBuilder.build(client), HttpStatus.OK);
    }
    
    /**
     * Creates new client
     * @param client
     * @param session
     * @return if request successful returns  201(Created) and newly created client Json data
     * 		   if unauthorized returns        401(Unauthorized) and error message as Json
	 * 		   if missing parameters returns  400(Bad Request) and error message as Json
	 * 		   if client exists returns       409(Conflict) and error message as Json
	 * 		   if any other error returns     500(Internal Server Error) and error message as Json
     */
    @RequestMapping(
            value = URL_CLIENTS,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createClient(Client client, HttpSession session) {
    	logger.info("Http request POST /user/api/" + URL_CLIENTS + " with params: name=" + client.getName() + 
    			", email=" + client.getEmail() + ", companyname=" + client.getCompanyname());
    	String email = getSessionAuthorization(session);
		if (email == null) {
			logger.info("Http request POST /user/api/" + URL_CLIENTS + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	if (client.getEmail() == null || client.getEmail().isEmpty() || client.getCompanyname() == null || 
    			client.getCompanyname().isEmpty() || client.getName() == null || client.getName().isEmpty()) {
    		logger.debug("Http request POST /user/api/" + URL_CLIENTS + " missing parameters: " + 
    					JsonBuilder.build(client));
    		return new ResponseEntity<>(MapBuilder.build("error", "missing parameters"),
    				HttpStatus.BAD_REQUEST);
    	}
    	if (clientService.findByEmail(client.getEmail()) != null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "client exists"), 
    				HttpStatus.CONFLICT);
    	}
    	Client newClient = clientService.create(client);
    	if (newClient == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "INTERNAL_SERVER_ERROR"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<>(JsonBuilder.build(newClient), HttpStatus.CREATED);
    }
    
	/**
	 * Updates client (name, email, company)
	 * @param client
	 * @param session
	 * @return if request successful returns 204(No Content)
	 * 		   if unauthorized returns       401(Unauthorized) and error message as Json
	 * 		   if update fail return         500(Internal Server Error) and error message as Json
	 */
    @RequestMapping(
            value = URL_CLIENTS + "/{id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateClient(Client client, HttpSession session, @PathVariable("id") Long id) {
    	logger.info("Http request PUT /user/api/" + URL_CLIENTS + " with params: name=" + client.getName() + 
    			", email=" + client.getEmail() + ", companyName=" + client.getCompanyname());
    	if (getSessionAuthorization(session) == null) {
    		logger.debug("Http request PUT /user/api/" + URL_CLIENTS + " failed, not logged in");
    		return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
    	}
    	Client updatedClient = clientService.update(client, id); 
    	if (updatedClient== null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "client does not exist"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

