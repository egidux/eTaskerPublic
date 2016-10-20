package org.eTasker.web.api;

import org.eTasker.AbstractControllerTest;
import org.eTasker.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class EmailControllerTest extends AbstractControllerTest {

	@Autowired
	EmailController emailController;
	
	@Test
	public void testEmailController() {
		//test with error email
        User user = new User();
        user.setEmail("noEmail");
        try {
        	emailController.sendEmail(user);
        	Assert.fail();
        } catch(Exception e) {
        	Assert.assertTrue(e.getMessage().contains("The recipient address <noEmail>"));
        }
        
        //test with correct mail
        try {
        	user.setEmail("regisetasker@gmail.com");
        	emailController.sendEmail(user);
        } catch(Exception e) {
        	Assert.fail();
        }
	}
}
