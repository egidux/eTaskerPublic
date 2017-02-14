package org.eTasker.controller.web.api;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.eTasker.model.Worker;
import org.eTasker.service.WorkerService;
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
public class WorkerController extends AbstractController {
	
	private static final String URL_WORKERS = "workers";
	private static final String EMAIL_SUBJECT = "Welcome";

	@Autowired
	protected WorkerService workerService;
	
	/**
	 * Retrieves all workers
	 @return if request successful   returns 200(OK) and all users as Json
	 *       if request unsuccessful returns 500(Internal Server Error) and error message as Json
	 */
    @RequestMapping(
            value = URL_WORKERS,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getWorkers(HttpSession session) {
    	logger.info("Http request GET /user/api/" + URL_WORKERS);
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request GET /user/api/" + URL_WORKERS + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	List<Worker> workers = workerService.findAll();
    	if (workers == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "INTERNAL_SERVER_ERROR"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
		return new ResponseEntity<List<Worker>>(workers, HttpStatus.OK);
    }
    
    /**
     * Retrives specific worker
     * @param id
     * @return if request successful returns   200(OK) and worker as Json
     *         if request unsuccessful returns 400(Bad Request) and error message as Json
     */
    @RequestMapping(
            value = URL_WORKERS + "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getWorker(@PathVariable("id") Long id, HttpSession session) {
    	logger.info("Http request GET /user/api/" + URL_WORKERS + "/{id} with id:" + id);
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request GET /user/api/" + URL_WORKERS + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	Worker worker = workerService.findOne(id);
    	if (worker == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "No worker found with id=" + id), 
    				HttpStatus.BAD_REQUEST);
    	}
    	return new ResponseEntity<>(JsonBuilder.build(worker), HttpStatus.OK);
    }
    
    /**
     * Creates new worker
     * @param worker
     * @param session
     * @return if request successful returns  201(Created) and newly created worker Json data
     * 		   if unauthorized returns        401(Unauthorized) and error message as Json
	 * 		   if missing parameters returns  400(Bad Request) and error message as Json
	 * 		   if worker exists returns       409(Conflict) and error message as Json
	 * 		   if any other error returns     500(Internal Server Error) and error message as Json
     */
    @RequestMapping(
            value = URL_WORKERS,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createWorker(Worker worker, HttpSession session) {
    	logger.info("Http request POST /user/api/" + URL_WORKERS + " with worker: " + JsonBuilder.build(worker));
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request POST /user/api/" + URL_WORKERS + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	if (worker.getEmail() == null || worker.getEmail().isEmpty() || worker.getName() == null 
    			|| worker.getName().isEmpty() || worker.getPassword() == null || worker.getPassword().isEmpty()) {
    		logger.debug("Http request POST /user/api/" + URL_WORKERS + " missing parameters: " + 
    					JsonBuilder.build(worker));
    		return new ResponseEntity<>(MapBuilder.build("error", "missing parameters"),
    				HttpStatus.BAD_REQUEST);
    	}
    	if (workerService.findByEmail(worker.getEmail()) != null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "worker exists"), 
    				HttpStatus.CONFLICT);
    	}
    	Worker newWorker = workerService.create(worker);
    	if (newWorker == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "INTERNAL_SERVER_ERROR"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	new Thread(() -> {
    		if (!emailController.sendEmail(newWorker.getEmail(), 
    				EMAIL_SUBJECT + " " + worker.getName(), "Username :" + worker.getEmail() + 
    				"\nPassword: " + worker.getPassword())) {
    			
    			//workerService.delete(newWorker);
			}
    	}).start();
    	return new ResponseEntity<>(JsonBuilder.build(newWorker), HttpStatus.CREATED);
    }
    
	/**
	 * Updates worker (name, email, company, password)
	 * @param worker
	 * @param session
	 * @return if request successful returns 204(No Content)
	 * 		   if Unauthorized returns       401(Unauthorized) and error message as Json
	 * 		   if update fail return         500(Internal Server Error) and error message as Json
	 */
    @RequestMapping(
            value = URL_WORKERS +"/{id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateWorker(Worker worker, HttpSession session, @PathVariable("id") Long id) {
    	logger.info("Http request PUT /user/api/" + URL_WORKERS + " with worker: " + JsonBuilder.build(worker));
    	if (getSessionAuthorization(session) == null) {
    		logger.debug("Http request PUT /user/api/" + URL_WORKERS + " failed, not logged in");
    		return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
    	}
    	Worker updatedWorker = workerService.update(worker, id); 
    	if (updatedWorker == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "not found worker with id=" + id), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
	/**
	 * Deletes worker
	 * @param worker
	 * @param session
	 * @return if request successful returns 204(No Content)
	 * 		   if Unauthorized returns       401(Unauthorized) and error message as Json
	 * 		   if delete fail return         500(Internal Server Error) and error message as Json
	 */
    @RequestMapping(
            value = URL_WORKERS +"/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteWorker(Worker worker, HttpSession session, @PathVariable("id") Long id) {
    	logger.info("Http request DELETE /user/api/" + URL_WORKERS + " with worker: " + JsonBuilder.build(worker));
    	if (getSessionAuthorization(session) == null) {
    		logger.debug("Http request PUT /user/api/" + URL_WORKERS + " failed, not logged in");
    		return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
    	}
    	workerService.delete(worker);
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
