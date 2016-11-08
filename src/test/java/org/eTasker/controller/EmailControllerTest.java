package org.eTasker.controller;

import org.eTasker.controller.EmailController;
import org.eTasker.model.User;
import org.eTasker.web.api.AbstractControllerTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class EmailControllerTest extends AbstractControllerTest {

	@Autowired
	EmailController emailController;
	
	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Test
	public void testEmailController() {
		//test with error email
        User user = new User();
        user.setEmail("noEmail");
        try {
        	emailController.sendEmail(user.getEmail(), "", "");
        	Assert.fail();
        } catch(Exception e) {
        	Assert.assertTrue(e.getMessage().contains("The recipient address <noEmail>"));
        }
        
        //test with correct mail
        try {
        	user.setEmail("regisetasker@gmail.com");
        	emailController.sendEmail(user.getEmail(), "Test", "");
        } catch(Exception e) {
        	Assert.fail();
        }
	}
}
