package org.eTasker.controller.web.api;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.eTasker.model.Task;
import org.eTasker.service.TaskService;
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
public class TaskController extends AbstractController {
	
	private static final String URL_TASKS = "tasks";

	@Autowired
	protected TaskService taskService;
	
	/**
	 * Retrieves all tasks for calendar
	 @return if request successful   returns 200(OK) and all tasks as Json
	 *       if request unsuccessful returns 500(Internal Server Error) and error message as Json
	 */
    @RequestMapping(
            value = URL_TASKS + "/calendar",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTasksCalendar(HttpSession session) {
    	logger.info("Http request GET /user/api/" + URL_TASKS);
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request GET /user/api/" + URL_TASKS + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	List<Task> tasks = taskService.findAll();
    	if (tasks == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "INTERNAL_SERVER_ERROR"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
		return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
    }
	
	/**
	 * Retrieves all tasks
	 @return if request successful   returns 200(OK) and all tasks as Json
	 *       if request unsuccessful returns 500(Internal Server Error) and error message as Json
	 */
    @RequestMapping(
            value = URL_TASKS,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTasks(HttpSession session) {
    	logger.info("Http request GET /user/api/" + URL_TASKS);
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request GET /user/api/" + URL_TASKS + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	List<Task> tasks = taskService.findAll();
    	if (tasks == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "INTERNAL_SERVER_ERROR"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
		return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
    }
    
    /**
     * Retrives specific task
     * @param id
     * @return if request successful returns   200(OK) and task as Json
     *         if request unsuccessful returns 400(Bad Request) and error message as Json
     */
    @RequestMapping(
            value = URL_TASKS + "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTask(@PathVariable("id") Long id, HttpSession session) {
    	logger.info("Http request GET /user/api/" + URL_TASKS + "/{id} with id:" + id);
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request GET /user/api/" + URL_TASKS + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	Task task = taskService.findOne(id);
    	if (task == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "No task found with id=" + id), 
    				HttpStatus.BAD_REQUEST);
    	}
    	return new ResponseEntity<>(JsonBuilder.build(task), HttpStatus.OK);
    }
    
    /**
     * Creates new task
     * @param task
     * @param session
     * @return if request successful returns  201(Created) and newly created task Json data
     * 		   if unauthorized returns        401(Unauthorized) and error message as Json
	 * 		   if missing parameters returns  400(Bad Request) and error message as Json
	 * 		   if worker exists returns       409(Conflict) and error message as Json
	 * 		   if any other error returns     500(Internal Server Error) and error message as Json
     */
    @RequestMapping(
            value = URL_TASKS,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTask(Task task, HttpSession session) {
    	logger.info("Http request POST /user/api/" + URL_TASKS + " with task: " + JsonBuilder.build(task));
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request POST /user/api/" + URL_TASKS + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	if (task.getTitle() == null || task.getTitle().isEmpty() ||  task.getClient() == null ||
    			task.getClient().isEmpty() || task.getObject() == null || task.getObject().isEmpty() || 
    			task.getPlanned_time() == null || task.getPlanned_time().isEmpty()) {
    		logger.debug("Http request POST /user/api/" + URL_TASKS + " missing parameters: " + 
    					JsonBuilder.build(task));
    		return new ResponseEntity<>(MapBuilder.build("error", "missing parameters"),
    				HttpStatus.BAD_REQUEST);
    	}
    	Task newTask = taskService.create(task);
    	if (newTask == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "INTERNAL_SERVER_ERROR"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<>(JsonBuilder.build(newTask), HttpStatus.CREATED);
    }
    
	/**
	 * Updates task
	 * @param task
	 * @param session
	 * @return if request successful returns 200(OK)
	 * 		   if Unauthorized returns       401(Unauthorized) and error message as Json
	 * 		   if update fail return         500(Internal Server Error) and error message as Json
	 */
    @RequestMapping(
            value = URL_TASKS +"/{id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTask(Task task, HttpSession session, @PathVariable("id") Long id) {
    	logger.info("Http request PUT /user/api/" + URL_TASKS + " with task: " + JsonBuilder.build(task));
    	if (getSessionAuthorization(session) == null) {
    		logger.debug("Http request PUT /user/api/" + URL_TASKS + " failed, not logged in");
    		return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
    	}
    	Task updatedTask = taskService.update(task, id); 
    	if (updatedTask == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "not found task with id=" + id), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<>(taskService.findOne(id), HttpStatus.OK);
    }
    
	/**
	 * Deletes task
	 * @param task
	 * @param session
	 * @return if request successful returns 204(No Content)
	 * 		   if Unauthorized returns       401(Unauthorized) and error message as Json
	 * 		   if delete fail return         500(Internal Server Error) and error message as Json
	 */
    @RequestMapping(
            value = URL_TASKS +"/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteTask(Task task, HttpSession session, @PathVariable("id") Long id) {
    	logger.info("Http request DELETE /user/api/" + URL_TASKS + " : " + JsonBuilder.build(task));
    	if (getSessionAuthorization(session) == null) {
    		logger.debug("Http request PUT /user/api/" + URL_TASKS + " failed, not logged in");
    		return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
    	}
    	taskService.delete(task);
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

