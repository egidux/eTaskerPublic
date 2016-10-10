package org.eTasker.web.api;

import org.eTasker.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Register {
	
	@Autowired
	RegisterService registerService;
}
