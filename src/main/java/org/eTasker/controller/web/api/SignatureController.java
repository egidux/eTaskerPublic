package org.eTasker.controller.web.api;

import java.nio.file.Path;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eTasker.model.Signature;
import org.eTasker.service.SignatureService;
import org.eTasker.tool.JsonBuilder;
import org.eTasker.tool.MapBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class SignatureController extends AbstractController {

	private static final String URL_SIGNATURES = "signatures";
	
	@Autowired
	protected SignatureService signatureService;
	
	/**
	 * Retrieves all signatures
	 @return if request successful   returns 200(OK) and all signatures as Json
	 *       if request unsuccessful returns 500(Internal Server Error) and error message as Json
	 */
    @RequestMapping(
            value = URL_SIGNATURES,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSignatures(HttpSession session) {
    	logger.info("Http request GET /user/api/" + URL_SIGNATURES);
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request GET /user/api/" + URL_SIGNATURES + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	List<Signature> signatures = signatureService.findAll();
    	if (signatures == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "INTERNAL_SERVER_ERROR"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
		return new ResponseEntity<List<Signature>>(signatures, HttpStatus.OK);
    }
   
    
	/**
	 * Upload Signature
	 * @param multFile
	 * @param signature
	 * @param session
	 * @return if request successful   returns 200(OK)
	 *         if request unsuccessful returns 400(Bad Request) and error message as Json
	 */
    @RequestMapping(
            value = URL_SIGNATURES + "/{id}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadSignature(@RequestParam("file") MultipartFile multFile, Signature signature, @PathVariable("id") Long id,
    		HttpSession session) {
    	logger.info("Http request POST /user/api/" + URL_SIGNATURES + "with signature: " + JsonBuilder.build(signature) + " id=" + id);
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request POST /user/api/" + URL_SIGNATURES + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
		signature.setTask(id);
		Signature newSignature = signatureService.store(multFile, signature);
		if (newSignature == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "Failed upload signature"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<Signature>(newSignature, HttpStatus.OK);
    }
    
    /**
     * Download signature
     * @param id
     * @param session
     * @param response
     * @return if request successful   returns 200(OK) and image
     *         if request unsuccessful returns 400(Bad Request) and error message as Json
     */
    @RequestMapping(
            value = URL_SIGNATURES + "/{id}/download",
            method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> download(@PathVariable("id") Long id, HttpSession session, HttpServletResponse response) {
    	logger.info("Http request POST /user/api/" + URL_SIGNATURES + "/{id}/download");
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request POST /user/api/" + URL_SIGNATURES + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
		Path path = signatureService.load(id);
    	if (path == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "No file found with id=" + id), 
    				HttpStatus.BAD_REQUEST);
    	}
		return new ResponseEntity<FileSystemResource>(new FileSystemResource(path.toFile()), HttpStatus.OK);
    }
}
