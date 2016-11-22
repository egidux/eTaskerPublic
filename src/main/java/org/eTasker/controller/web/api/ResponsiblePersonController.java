package org.eTasker.controller.web.api;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.eTasker.model.Responsible_person;
import org.eTasker.service.ResponsiblePersonService;
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
public class ResponsiblePersonController extends AbstractController {
	
	private static final String URL_RESPONSIBLE_PERSONS = "responsible_persons";

	@Autowired
	protected ResponsiblePersonService responsiblePersonService;
	
	/**
	 * Retrieves all responsible persons
	 * @return if request successful   returns 200(OK) and all responsible persons as Json
	 *         if request unsuccessful returns 500(Internal Server Error) and error message as Json
	 */
    @RequestMapping(
            value = URL_RESPONSIBLE_PERSONS,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getResponsiblePersons(HttpSession session) {
    	logger.info("Http request GET /user/api/" + URL_RESPONSIBLE_PERSONS);
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request GET /user/api/" + URL_RESPONSIBLE_PERSONS + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	List<Responsible_person> responsiblePersons = responsiblePersonService.findAll();
    	if (responsiblePersons == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "INTERNAL_SERVER_ERROR"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
		return new ResponseEntity<List<Responsible_person>>(responsiblePersons, HttpStatus.OK);
    }
    
    /**
     * Retrives specific responsible person
     * @param id
     * @return if request successful returns   200(OK) material as Json
     *         if request unsuccessful returns 400(Bad Request) and error message as Json
     */
    @RequestMapping(
            value = URL_RESPONSIBLE_PERSONS + "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getResponsiblePerson(@PathVariable("id") Long id, HttpSession session) {
    	logger.info("Http request GET /user/api/" + URL_RESPONSIBLE_PERSONS + "/{id} with id:" + id);
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request GET /user/api/" + URL_RESPONSIBLE_PERSONS + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	Responsible_person responsiblePerson = responsiblePersonService.findOne(id);
    	if (responsiblePerson == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "No responsible person found with id=" + id), 
    				HttpStatus.BAD_REQUEST);
    	}
    	return new ResponseEntity<>(responsiblePerson, HttpStatus.OK);
    }
    
    /**
     * Creates new responsible person
     * @param responsible person
     * @param session
     * @return if request successful returns  201(Created) and newly created responsible person Json data
     * 		   if unauthorized returns        401(Unauthorized) and error message as Json
	 * 		   if missing parameters returns  400(Bad Request) and error message as Json
	 * 		   if worker exists returns       409(Conflict) and error message as Json
	 * 		   if any other error returns     500(Internal Server Error) and error message as Json
     */
    @RequestMapping(
            value = URL_RESPONSIBLE_PERSONS,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createResponsiblePerson(Responsible_person responsiblePerson, HttpSession session) {
    	logger.info("Http request POST /user/api/" + URL_RESPONSIBLE_PERSONS + " with object: " + 
    			JsonBuilder.build(responsiblePerson));
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request POST /user/api/" + URL_RESPONSIBLE_PERSONS + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	if (responsiblePerson.getClient() == null || responsiblePerson.getEmail() == null || 
    			responsiblePerson.getEmail().isEmpty() || responsiblePerson.getFirst_name() == null ||
    			responsiblePerson.getFirst_name().isEmpty() || responsiblePerson.getPhone() == null ||
    			responsiblePerson.getPhone().isEmpty() || responsiblePerson.getLast_name() == null ||
    			responsiblePerson.getLast_name().isEmpty()) {
    		logger.debug("Http request POST /user/api/" + URL_RESPONSIBLE_PERSONS + " missing parameters: " + 
    					JsonBuilder.build(responsiblePerson));
    		return new ResponseEntity<>(MapBuilder.build("error", "missing parameters"),
    				HttpStatus.BAD_REQUEST);
    	}
    	Responsible_person newResponsiblePerson = responsiblePersonService.create(responsiblePerson);
    	if (newResponsiblePerson == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "INTERNAL_SERVER_ERROR"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<String>(JsonBuilder.build(newResponsiblePerson), HttpStatus.CREATED);
    }
    
	/**
	 * Updates responsible person
	 * @param responsible person
	 * @param session
	 * @return if request successful returns 204(No Content)
	 * 		   if Unauthorized returns       401(Unauthorized) and error message as Json
	 * 		   if update fail return         500(Internal Server Error) and error message as Json
	 */
    @RequestMapping(
            value = URL_RESPONSIBLE_PERSONS +"/{id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateResponsiblePerson(Responsible_person responsiblePerson, HttpSession session, 
    		@PathVariable("id") Long id) {
    	logger.info("Http request PUT /user/api/" + URL_RESPONSIBLE_PERSONS + " with material: " + 
    		JsonBuilder.build(responsiblePerson));
    	if (getSessionAuthorization(session) == null) {
    		logger.debug("Http request PUT /user/api/" + URL_RESPONSIBLE_PERSONS + " failed, not logged in");
    		return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
    	}
    	Responsible_person updatedResponsiblePerson = responsiblePersonService.update(responsiblePerson, id); 
    	if (updatedResponsiblePerson == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "not found responsible person with id=" + id), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
