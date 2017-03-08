package org.eTasker.controller.web.api;

import javax.servlet.http.HttpSession;

import org.eTasker.model.Report;
import org.eTasker.service.ReportService;
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
public class ReportController extends AbstractController {

	private static final String URL_REPORT = "report";

	@Autowired
	protected ReportService reportService;
	
    /**
     * Retrives report
     * @return if request successful returns   200(OK) and report as Json
     *         if request unsuccessful returns 500(Internal Server Error) and error message as Json
     */
    @RequestMapping(
            value = URL_REPORT,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getReport(HttpSession session) {
    	logger.info("Http request GET /user/api/" + URL_REPORT);
		if (getSessionAuthorization(session) == null) {
			logger.info("Http request GET /user/api/" + URL_REPORT + " not logged in");
			return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
		}
		Report report = reportService.get();
    	if (report == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "Internal Server Error"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<>(report, HttpStatus.OK);
    }
    
	/**
	 * Updates report
	 * @param report
	 * @param session
	 * @return if request successful returns 204(No Content)
	 * 		   if Unauthorized returns       401(Unauthorized) and error message as Json
	 *         if missing parameters returns 400(Bad Request) and error message as Json
	 * 		   if update fail return         500(Internal Server Error) and error message as Json
	 */
    @RequestMapping(
            value = URL_REPORT,
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateReport(Report report, HttpSession session) {
    	logger.info("Http request PUT /user/api/" + URL_REPORT + ": " + JsonBuilder.build(report));
    	if (getSessionAuthorization(session) == null) {
    		logger.debug("Http request PUT /user/api/" + URL_REPORT + " failed, not logged in");
    		return new ResponseEntity<>(MapBuilder.build("error", "please login"), HttpStatus.UNAUTHORIZED);
    	}
    	if (report.getCompany_address() == null || report.getCompany_address().isEmpty() || 
    			report.getCompany_code() == null || report.getCompany_code().isEmpty() ||
    			report.getCompany_name() == null || report.getCompany_name().isEmpty() ||
    			report.getCompany_phone() == null || report.getCompany_phone().isEmpty()) {
    		return new ResponseEntity<>(MapBuilder.build("error", "missing parameters"),
    				HttpStatus.BAD_REQUEST);
    	}
    	if (reportService.update(report) == null) {
    		return new ResponseEntity<>(MapBuilder.build("error", "Failed update report"), 
    				HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
