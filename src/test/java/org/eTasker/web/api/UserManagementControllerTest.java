package org.eTasker.web.api;

import org.eTasker.AbstractControllerTest;
import org.eTasker.service.UserManagementService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UserManagementControllerTest extends AbstractControllerTest {
    
	@Autowired
	UserManagementService userManagementService;

	@Before
	public void setup() {
		super.setUp();
	}
	
    @Test
    public void test() throws Exception {
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
        Assert.assertEquals("/user/api/register failure", "{\"id\":1,\"email\":\"regisetasker@gmail.com\"," +
        		"\"name\":\"John\",\"companyname\":\"any\"}", content);
        
        // test user/api/register with existing user
        result = mvc.perform(MockMvcRequestBuilders.post(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
        status = result.getResponse().getStatus();
        content = result.getResponse().getContentAsString();
        Assert.assertEquals("/user/api/register failure - HTTP status", 409, status);
        Assert.assertEquals("/user/api/register failure", "{\"error\":\"user exists\"}", content);
        
        // test user/api/register with error email
        uri = "/user/api/register?name=John&email=regisetasker&companyname=any&password=123";
        result = mvc.perform(MockMvcRequestBuilders.post(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
        status = result.getResponse().getStatus();
        content = result.getResponse().getContentAsString();
        Assert.assertEquals("/user/api/register failure - HTTP status", 500, status);
        Assert.assertEquals("/user/api/register failure", "{\"error\":\"failed to send email\"}", content);
        
    	// test /user/api/users when user exists
    	uri = "/user/api/users";
    	result = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
        content = result.getResponse().getContentAsString();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/users failure - HTTP status", 200, status);
        Assert.assertEquals("/user/api/users failure", "[{\"id\":1,\"email\":\"regisetasker@gmail.com\"," +
        		"\"name\":\"John\",\"companyname\":\"any\"}]", content);
        
        // test /user/api/testsession when not logged in
    	uri = "/user/api/testsession";
    	result = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
        content = result.getResponse().getContentAsString();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/testsession failure - HTTP status", 401, status);
        Assert.assertEquals("/user/api/testsession failure", "{\"error\":\"please login\"}", content);
        
        // test /user/api/login when email not confirmed
    	uri = "/user/api/login?email=regisetasker@gmail.com&password=123";
    	result = mvc.perform(MockMvcRequestBuilders.post(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
        content = result.getResponse().getContentAsString();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/login failure - HTTP status", 400, status);
        Assert.assertEquals("/user/api/login failure", "{\"error\":\"please validate registration\"}", content);
        
        // test user/api/ver{id}
        uri = "/user/api/ver/{id}";
        Long id = new Long(1);
    	result = mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON)).andReturn();
        content = result.getResponse().getContentAsString();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/login failure - HTTP status", 200, status);
        Assert.assertEquals("/user/api/login failure", "Thank You", content);
        
        // test user/api/ver{id} with invalid id
        uri = "/user/api/ver/{id}";
        id = Long.MAX_VALUE;
    	result = mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON)).andReturn();
        content = result.getResponse().getContentAsString();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/login failure - HTTP status", 400, status);
        Assert.assertEquals("/user/api/login failure", "{\"error\":\"user with id=" + id + " dont exist\"}", content);
        
        // test user/api/login when wrong credentials
    	uri = "/user/api/login?email=regisetasker@gmail.com&password=12";
    	result = mvc.perform(MockMvcRequestBuilders.post(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
        content = result.getResponse().getContentAsString();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/login failure - HTTP status", 400, status);
        Assert.assertEquals("/user/api/login failure", "{\"error\":\"wrong email or password\"}", content);
        
        // test user/api/login
    	uri = "/user/api/login?email=regisetasker@gmail.com&password=123";
    	result = mvc.perform(MockMvcRequestBuilders.post(uri)).andReturn();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/login failure - HTTP status", 200, status);
        
        // test /user/api/logout
    	uri = "/user/api/logout";
    	result = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/testsession failure - HTTP status", 200, status);
    }
}
