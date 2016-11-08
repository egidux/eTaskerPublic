package org.eTasker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;

@Controller
public class EmailController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailController.class);
	
	@Autowired
	private JavaMailSender mailSender;
	private SimpleMailMessage templateMessage = new SimpleMailMessage();

	public boolean sendEmail(String email, String subject, String text) {
		try {
			templateMessage.setTo(email);
			templateMessage.setSubject(subject);
			templateMessage.setText(text);
		    SimpleMailMessage message = new SimpleMailMessage(templateMessage);
		    mailSender.send(message);
		    LOGGER.info("Email sent to: " + email + ", with subject: " + subject);
		    return true;
		} catch (Exception e) {
			LOGGER.debug("Failed send email to:" + email+ ", with subject: " + subject);
			return false;
		}
	}
}
