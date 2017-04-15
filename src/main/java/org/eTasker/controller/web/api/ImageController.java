package org.eTasker.controller.web.api;

import java.nio.file.Path;
import java.util.List;

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

	private static final String URL_IMAGES = "images";
	
	@Autowired
	protected ImageService imageService;
	
	/**
	 * Retrieves all images
	 @return if request successful   returns 200(OK) and all images as Json
	 *       if request unsuccessful returns 500(Internal Server Error) and error message as Json
	 */
    @RequestMapping(
            value = URL_IMAGES + "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getImage(HttpSession session, @PathVariable("id") Long id) {
    	logger.info("Http request GET /user/api/" + URL_IMAGES);
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request GET /user/api/" + URL_IMAGES + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	List<Image> images = imageService.findAll(id);
    	if (images == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "INTERNAL_SERVER_ERROR"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
		return new ResponseEntity<List<Image>>(images, HttpStatus.OK);
    }
    
	/**
	 * Upload image
	 * @param multFile
	 * @param image
	 * @param session
	 * @return if request successful   returns 200(OK)
	 *         if request unsuccessful returns 400(Bad Request) and error message as Json
	 */
    @RequestMapping(
            value = URL_IMAGES + "/{taskID}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile multFile, Image image, @PathVariable("taskID") Long taskID,
    		HttpSession session) {
    	logger.info("Http request POST /user/api/" + URL_IMAGES + "with image: " + JsonBuilder.build(image) + " id=" + taskID);
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request POST /user/api/" + URL_IMAGES + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
		image.setTask(taskID);
		Image newImage = imageService.store(multFile, image);
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
     *         if request unsuccessful returns 400(Bad Request) and error message as Json
     */
    @RequestMapping(
            value = URL_IMAGES + "/{taskID}/download/{imageID}",
            method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> download(@PathVariable("taskID") Long taskID, HttpSession session, 
    		@PathVariable("imageID") Long imageID, HttpServletResponse response) {
    	logger.info("Http request GET /user/api/" + URL_IMAGES + "/" + taskID + "/download/" + imageID);
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request POST /user/api/" + URL_IMAGES + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
		Path path = imageService.load(taskID, imageID);
    	if (path == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "No file found with taskID=" + taskID + ", imageID=" + imageID), 
    				HttpStatus.BAD_REQUEST);
    	}
		return new ResponseEntity<FileSystemResource>(new FileSystemResource(path.toFile()), HttpStatus.OK);
    }
}