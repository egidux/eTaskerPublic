package org.eTasker.controller.web.api;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.eTasker.model.Task_type;
import org.eTasker.service.TaskTypeService;
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
public class TaskTypeController extends AbstractController {
	
	private static final String URL_TASK_TYPES = "task_types";

	@Autowired
	protected TaskTypeService taskTypeService;
	
	/**
	 * Retrieves all task types
	 * @return if request successful   returns 200(OK) and all task types as Json
	 *         if request unsuccessful returns 500(Internal Server Error) and error message as Json
	 */
    @RequestMapping(
            value = URL_TASK_TYPES,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTaskTypes(HttpSession session) {
    	logger.info("Http request GET /user/api/" + URL_TASK_TYPES);
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request GET /user/api/" + URL_TASK_TYPES + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	List<Task_type> taskTypes = taskTypeService.findAll();
    	if (taskTypes == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "INTERNAL_SERVER_ERROR"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
		return new ResponseEntity<List<Task_type>>(taskTypes, HttpStatus.OK);
    }
    
    /**
     * Retrives specific task type
     * @param id
     * @return if request successful returns   200(OK) material as Json
     *         if request unsuccessful returns 400(Bad Request) and error message as Json
     */
    @RequestMapping(
            value = URL_TASK_TYPES + "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTaskType(@PathVariable("id") Long id, HttpSession session) {
    	logger.info("Http request GET /user/api/" + URL_TASK_TYPES + "/{id} with id:" + id);
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request GET /user/api/" + URL_TASK_TYPES + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	Task_type tasktype = taskTypeService.findOne(id);
    	if (tasktype == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "No task type found with id=" + id), 
    				HttpStatus.BAD_REQUEST);
    	}
    	return new ResponseEntity<Task_type>(tasktype, HttpStatus.OK);
    }
    
    /**
     * Creates new task type
     * @param task type
     * @param session
     * @return if request successful returns  201(Created) and newly created task type Json data
     * 		   if unauthorized returns        401(Unauthorized) and error message as Json
	 * 		   if missing parameters returns  400(Bad Request) and error message as Json
	 * 		   if worker exists returns       409(Conflict) and error message as Json
	 * 		   if any other error returns     500(Internal Server Error) and error message as Json
     */
    @RequestMapping(
            value = URL_TASK_TYPES,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTaskType(Task_type taskType, HttpSession session) {
    	logger.info("Http request POST /user/api/" + URL_TASK_TYPES + " with object: " + 
    			JsonBuilder.build(taskType));
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request POST /user/api/" + URL_TASK_TYPES + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	if (taskType.getTitle() == null || taskType.getTitle().isEmpty()) {
    		logger.debug("Http request POST /user/api/" + URL_TASK_TYPES + " missing parameters: " + 
    					JsonBuilder.build(taskType));
    		return new ResponseEntity<>(MapBuilder.build("error", "missing parameters"),
    				HttpStatus.BAD_REQUEST);
    	}
    	Task_type newTaskType = taskTypeService.create(taskType);
    	if (newTaskType == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "INTERNAL_SERVER_ERROR"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<String>(JsonBuilder.build(newTaskType), HttpStatus.CREATED);
    }
    
	/**
	 * Updates task type
	 * @param task type
	 * @param session
	 * @return if request successful returns 204(No Content)
	 * 		   if Unauthorized returns       401(Unauthorized) and error message as Json
	 * 		   if update fail return         500(Internal Server Error) and error message as Json
	 */
    @RequestMapping(
            value = URL_TASK_TYPES +"/{id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTaskType(Task_type tasktype, HttpSession session, @PathVariable("id") Long id) {
    	logger.info("Http request PUT /user/api/" + URL_TASK_TYPES + " with material: " + 
    		JsonBuilder.build(tasktype));
    	if (getSessionAuthorization(session) == null) {
    		logger.debug("Http request PUT /user/api/" + URL_TASK_TYPES + " failed, not logged in");
    		return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
    	}
    	Task_type updatedTaskType = taskTypeService.update(tasktype, id); 
    	if (updatedTaskType == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "not found task type with id=" + id), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}