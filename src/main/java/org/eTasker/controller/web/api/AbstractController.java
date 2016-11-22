package org.eTasker.controller.web.api;

import javax.servlet.http.HttpSession;

import org.eTasker.controller.EmailController;
import org.eTasker.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.RequestMapping;

@EnableRedisHttpSession 
@RequestMapping("user/api/")
public class AbstractController {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	protected UserService userService;
	@Autowired
	protected EmailController emailController;

	protected String getSessionAuthorization(HttpSession session) {
		return (String)session.getAttribute("Authorization");
	}
}
