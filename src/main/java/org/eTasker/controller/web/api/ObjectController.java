package org.eTasker.controller.web.api;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.eTasker.model.Object;
import org.eTasker.service.ObjectService;
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
public class ObjectController extends AbstractController {
	
	private static final String URL_OBJECTS = "objects";

	@Autowired
	protected ObjectService objectService;
	
	/**
	 * Retrieves all objects
	 @return if request successful   returns 200(OK) and all objects as Json
	 *       if request unsuccessful returns 500(Internal Server Error) and error message as Json
	 */
    @RequestMapping(
            value = URL_OBJECTS,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getObjects(HttpSession session) {
    	logger.info("Http request GET /user/api/" + URL_OBJECTS);
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request GET /user/api/" + URL_OBJECTS + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	List<Object> objects = objectService.findAll();
    	if (objects == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "INTERNAL_SERVER_ERROR"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
		return new ResponseEntity<List<Object>>(objects, HttpStatus.OK);
    }
    
    /**
     * Retrives specific object
     * @param id
     * @return if request successful returns   200(OK) and object as Json
     *         if request unsuccessful returns 400(Bad Request) and error message as Json
     */
    @RequestMapping(
            value = URL_OBJECTS + "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getObject(@PathVariable("id") Long id, HttpSession session) {
    	logger.info("Http request GET /user/api/" + URL_OBJECTS + "/{id} with id:" + id);
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request GET /user/api/" + URL_OBJECTS + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	Object object = objectService.findOne(id);
    	if (object == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "No object found with id=" + id), 
    				HttpStatus.BAD_REQUEST);
    	}
    	return new ResponseEntity<>(JsonBuilder.build(object), HttpStatus.OK);
    }
    
    /**
     * Creates new object
     * @param object
     * @param session
     * @return if request successful returns  201(Created) and newly created object Json data
     * 		   if unauthorized returns        401(Unauthorized) and error message as Json
	 * 		   if missing parameters returns  400(Bad Request) and error message as Json
	 * 		   if worker exists returns       409(Conflict) and error message as Json
	 * 		   if any other error returns     500(Internal Server Error) and error message as Json
     */
    @RequestMapping(
            value = URL_OBJECTS,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createObject(Object object, HttpSession session) {
    	logger.info("Http request POST /user/api/" + URL_OBJECTS + " with object: " + JsonBuilder.build(object));
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request POST /user/api/" + URL_OBJECTS + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	if (object.getName() == null || object.getName().isEmpty() || object.getClient() == null ||
    			object.getClient().isEmpty()) {
    		logger.debug("Http request POST /user/api/" + URL_OBJECTS + " missing parameters: " + 
    					JsonBuilder.build(object));
    		return new ResponseEntity<>(MapBuilder.build("error", "missing parameters"),
    				HttpStatus.BAD_REQUEST);
    	}
    	Object newObject = objectService.create(object);
    	if (newObject == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "INTERNAL_SERVER_ERROR"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<>(JsonBuilder.build(newObject), HttpStatus.CREATED);
    }
    
	/**
	 * Updates object
	 * @param task
	 * @param session
	 * @return if request successful returns 204(No Content)
	 * 		   if Unauthorized returns       401(Unauthorized) and error message as Json
	 * 		   if update fail return         500(Internal Server Error) and error message as Json
	 */
    @RequestMapping(
            value = URL_OBJECTS +"/{id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateObject(Object object, HttpSession session, @PathVariable("id") Long id) {
    	logger.info("Http request PUT /user/api/" + URL_OBJECTS + " with task: " + JsonBuilder.build(object));
    	if (getSessionAuthorization(session) == null) {
    		logger.debug("Http request PUT /user/api/" + URL_OBJECTS + " failed, not logged in");
    		return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
    	}
    	if (object.getName() == null || object.getName().isEmpty() || object.getClient() == null ||
    			object.getClient().isEmpty()) {
    		logger.debug("Http request POST /user/api/" + URL_OBJECTS + " missing parameters: " + 
    					JsonBuilder.build(object));
    		return new ResponseEntity<>(MapBuilder.build("error", "missing parameters"),
    				HttpStatus.BAD_REQUEST);
    	}
    	Object updatedObject = objectService.update(object, id); 
    	if (updatedObject == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "not found object with id=" + id), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
	/**
	 * Deletes object
	 * @param object
	 * @param session
	 * @return if request successful returns 204(No Content)
	 * 		   if Unauthorized returns       401(Unauthorized) and error message as Json
	 * 		   if delete fail return         500(Internal Server Error) and error message as Json
	 */
    @RequestMapping(
            value = URL_OBJECTS +"/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteObject(Object object, HttpSession session, @PathVariable("id") Long id) {
    	logger.info("Http request DELETE /user/api/" + URL_OBJECTS + " : " + JsonBuilder.build(object));
    	if (getSessionAuthorization(session) == null) {
    		logger.debug("Http request PUT /user/api/" + URL_OBJECTS + " failed, not logged in");
    		return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
    	}
    	objectService.delete(object);
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
