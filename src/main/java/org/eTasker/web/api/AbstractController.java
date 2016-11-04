package org.eTasker.web.api;

import org.eTasker.controller.EmailController;
import org.eTasker.service.UserManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableRedisHttpSession 
//@RestController
@RequestMapping("user/api/")
public class AbstractController {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	protected UserManagementService userManagementService;
	@Autowired
	protected EmailController email;
}
