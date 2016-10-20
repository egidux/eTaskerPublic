package org.eTasker.service;

import java.util.List;

import org.eTasker.AbstractTest;
import org.eTasker.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserManagementServiceTest extends AbstractTest {

	@Autowired
	UserManagementService userManagementService;
	
	@Test
	public void testUserManagementService() {
		List<User> users = userManagementService.findAll();
		Assert.assertTrue(users.isEmpty());
	}
}
