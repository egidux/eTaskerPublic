package org.eTasker.web.api;

import org.eTasker.AbstractControllerTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CustomerControllerTest extends AbstractControllerTest {

	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Test
	public void testCustomerController() throws Exception {
    	// test /user/api/users when no user exists
    	String uri = "/user/api/users";
    	MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/users failure - HTTP status", 200, status);
        Assert.assertTrue("/user/api/users failure - expected [] ", content.equals("[]"));
        
        // test user/api/register with missing params
        uri = "/user/api/register";
        result = mvc.perform(MockMvcRequestBuilders.post(uri)).andReturn();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/register failure - HTTP status", 400, status);
        
        //test user/api/register
        uri += "?name=John&email=regisetasker@gmail.com&companyname=any&password=123";
        result = mvc.perform(MockMvcRequestBuilders.post(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
        status = result.getResponse().getStatus();
        content = result.getResponse().getContentAsString();
        Assert.assertEquals("/user/api/register failure - HTTP status", 201, status);
        //Assert.assertEquals("/user/api/register failure", "{\"id\":1,\"email\":\"regisetasker@gmail.com\"," +
        		//"\"name\":\"John\",\"companyname\":\"any\"}", content);
        
        // test user/api/register with existing user
        result = mvc.perform(MockMvcRequestBuilders.post(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
        status = result.getResponse().getStatus();
        content = result.getResponse().getContentAsString();
        Assert.assertEquals("/user/api/register failure - HTTP status", 409, status);
        Assert.assertEquals("/user/api/register failure", "{\"error\":\"user with this email exists\"}", content);
        
    	// test /user/api/users when user exists
    	uri = "/user/api/users";
    	result = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
        content = result.getResponse().getContentAsString();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/users failure - HTTP status", 200, status);
        Assert.assertEquals("/user/api/users failure", "[{\"id\":1,\"email\":\"regisetasker@gmail.com\"," +
        		"\"name\":\"John\",\"companyname\":\"any\"}]", content);
	}
}
