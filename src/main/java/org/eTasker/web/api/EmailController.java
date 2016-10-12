package org.eTasker.web.api;

import org.eTasker.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;

@Controller
public class EmailController {

	@Autowired
	private JavaMailSender mailSender;
	private SimpleMailMessage templateMessage = new SimpleMailMessage();

	public void sendEmail(User user) {
		templateMessage.setTo(user.getEmail());
		templateMessage.setSubject("Confirm eTasker registration	");
		templateMessage.setText("https://localhost:8085/user/api/ver/" + user.getId());
	        
	    SimpleMailMessage message = new SimpleMailMessage(templateMessage);
	    mailSender.send(message);
	}
}
