package org.eTasker.web.api;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorkerController extends AbstractController {
	
	private static final String URL_WORKERS = "workers";

	@Autowired
	protected WorkerService workerService;
	
	/**
	 * 
	 * @return
	 */
    @RequestMapping(
            value = URL_WORKERS,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getWorkers() {
    	List<Worker> workers = workerService.findAll();
    	logger.info("Http request GET /user/api/workers -> " + JsonBuilder.build(workers));
		return new ResponseEntity<List<Worker>>(workers, HttpStatus.OK);
    }
    
    @RequestMapping(
            value = URL_WORKERS,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerWorker(Worker worker, HttpSession session) {
		String email = getSessionAuthorization(session);
		if (email == null) {
			logger.info("Http request POST /user/api/workers not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	logger.info("Http request POST /user/api/workers with params: name=" + worker.getName() + 
    			", email=" + worker.getEmail() + ", companyname=" +
    			worker.getCompanyname() + ", password=" + worker.getPassword());
    	if (worker.getEmail() == null || worker.getEmail().isEmpty() || worker.getCompanyname() == null || 
    			worker.getCompanyname().isEmpty() || worker.getName() == null || worker.getName().isEmpty() ||
    					worker.getPassword() == null || worker.getPassword().isEmpty()) {
    		logger.debug("Http request POST /user/api/workers missing parameters: " + JsonBuilder.build(worker));
    		return new ResponseEntity<>(MapBuilder.build("error", "missing parameters"),
    				HttpStatus.BAD_REQUEST);
    	}
    	if (workerService.findByEmail(worker.getEmail()) != null) {
    		logger.debug("Http request POST /user/api/workers failed, worker exists with email=" + worker.getEmail());
    		return new ResponseEntity<>(MapBuilder.build("error", "worker with this email exists"), 
    				HttpStatus.CONFLICT);
    	}
    	Worker newWorker = workerService.create(worker);
    	if (newWorker == null) {
    		logger.debug("Http request POST /user/api/workers failed workerService.create(worker) for worker with email=" + 
    				worker.getEmail());
    		return new ResponseEntity<>(MapBuilder.build("error", "INTERNAL_SERVER_ERROR"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	new Thread(() -> {
    		try {
        		emailController.sendEmail(newWorker.getEmail(), "Welcome", 
        				"You are registered worker for " + newWorker.getCompanyname());
        		logger.info("Http request POST /user/api/workers email sent to: " + newWorker.getEmail());
        	} catch (Exception e) {
        		logger.debug("Http request POST /user/api/workers failed to sent email to: " + newWorker.getEmail());
        		workerService.delete(newWorker);
        	}
    	}).start();
    	logger.debug("Http request POST /user/api/workers created new worker with email: " + newWorker.getEmail());
    	return new ResponseEntity<>(JsonBuilder.build(newWorker), HttpStatus.CREATED);
    }
}
