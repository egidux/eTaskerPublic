package org.eTasker.web.api;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.eTasker.model.Material;
import org.eTasker.service.MaterialService;
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
public class MaterialController extends AbstractController {
	
	private static final String URL_MATERIALS = "materials";

	@Autowired
	protected MaterialService materialService;
	
	/**
	 * Retrieves all materials
	 @return if request successful   returns 200(OK) and all materials as Json
	 *       if request unsuccessful returns 500(Internal Server Error) and error message as Json
	 */
    @RequestMapping(
            value = URL_MATERIALS,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMaterials(HttpSession session) {
    	logger.info("Http request GET /user/api/" + URL_MATERIALS);
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request GET /user/api/" + URL_MATERIALS + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	List<Material> materials = materialService.findAll();
    	if (materials == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "INTERNAL_SERVER_ERROR"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
		return new ResponseEntity<List<Material>>(materials, HttpStatus.OK);
    }
    
    /**
     * Retrives specific material
     * @param id
     * @return if request successful returns   200(OK) material as Json
     *         if request unsuccessful returns 400(Bad Request) and error message as Json
     */
    @RequestMapping(
            value = URL_MATERIALS + "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMaterial(@PathVariable("id") Long id, HttpSession session) {
    	logger.info("Http request GET /user/api/" + URL_MATERIALS + "/{id} with id:" + id);
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request GET /user/api/" + URL_MATERIALS + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	Material material = materialService.findOne(id);
    	if (material == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "No material found with id=" + id), 
    				HttpStatus.BAD_REQUEST);
    	}
    	return new ResponseEntity<String>(JsonBuilder.build(material), HttpStatus.OK);
    }
    
    /**
     * Creates new material
     * @param material
     * @param session
     * @return if request successful returns  201(Created) and newly created material Json data
     * 		   if unauthorized returns        401(Unauthorized) and error message as Json
	 * 		   if missing parameters returns  400(Bad Request) and error message as Json
	 * 		   if worker exists returns       409(Conflict) and error message as Json
	 * 		   if any other error returns     500(Internal Server Error) and error message as Json
     */
    @RequestMapping(
            value = URL_MATERIALS,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createMaterial(Material material, HttpSession session) {
    	logger.info("Http request POST /user/api/" + URL_MATERIALS + " with object: " + JsonBuilder.build(material));
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request POST /user/api/" + URL_MATERIALS + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
    	if (material.getSerial_number() == null || material.getSerial_number().isEmpty() ||
    			material.getTitle() == null || material.getTitle().isEmpty()) {
    		logger.debug("Http request POST /user/api/" + URL_MATERIALS + " missing parameters: " + 
    					JsonBuilder.build(material));
    		return new ResponseEntity<>(MapBuilder.build("error", "missing parameters"),
    				HttpStatus.BAD_REQUEST);
    	}
    	Material newMaterial = materialService.create(material);
    	if (newMaterial == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "INTERNAL_SERVER_ERROR"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<String>(JsonBuilder.build(newMaterial), HttpStatus.CREATED);
    }
    
	/**
	 * Updates material
	 * @param material
	 * @param session
	 * @return if request successful returns 204(No Content)
	 * 		   if Unauthorized returns       401(Unauthorized) and error message as Json
	 * 		   if update fail return         500(Internal Server Error) and error message as Json
	 */
    @RequestMapping(
            value = URL_MATERIALS +"/{id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateMaterial(Material material, HttpSession session, @PathVariable("id") Long id) {
    	logger.info("Http request PUT /user/api/" + URL_MATERIALS + " with material: " + JsonBuilder.build(material));
    	if (getSessionAuthorization(session) == null) {
    		logger.debug("Http request PUT /user/api/" + URL_MATERIALS + " failed, not logged in");
    		return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
    	}
    	Material updatedMaterial = materialService.update(material, id); 
    	if (updatedMaterial == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "not found material with id=" + id), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}