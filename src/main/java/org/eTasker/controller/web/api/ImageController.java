package org.eTasker.controller.web.api;

import java.nio.file.Path;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eTasker.model.Image;
import org.eTasker.service.ImageService;
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
public class ImageController extends AbstractController {

	private static final String URL_FILES = "images";
	
	@Autowired
	protected ImageService fileService;
	
	/**
	 * Uploads image
	 * @param multFile
	 * @param image
	 * @param session
	 * @return if request successful   returns 200(OK)
	 */
    @RequestMapping(
            value = URL_FILES,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> storeImage(@RequestParam("file") MultipartFile multFile, Image image, HttpSession session) {
    	logger.info("Http request POST /user/api/" + URL_FILES + "with image: " + JsonBuilder.build(image));
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request POST /user/api/" + URL_FILES + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
		Image newImage = fileService.store(multFile, image);
		if (newImage == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "Failed upload image"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<Image>(newImage, HttpStatus.OK);
    }
    
    /**
     * Download image
     * @param id
     * @param session
     * @param response
     * @return if request successful   returns 200(OK) and image
     */
    @RequestMapping(
            value = URL_FILES + "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> download(@PathVariable("id") Long id, HttpSession session, HttpServletResponse response) {
    	logger.info("Http request POST /user/api/" + URL_FILES);
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request POST /user/api/" + URL_FILES + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
		Path path = fileService.load(id);
    	if (path == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "No file found with id=" + id), 
    				HttpStatus.BAD_REQUEST);
    	}
		return new ResponseEntity<FileSystemResource>(new FileSystemResource(path.toFile()), HttpStatus.OK);
    }
}