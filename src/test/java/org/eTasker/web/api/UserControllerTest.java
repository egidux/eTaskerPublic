package org.eTasker.web.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UserControllerTest extends AbstractControllerTest {
	
	@Before
	public void setup() throws Exception {
		super.setUp();
		super.createUser();
	}
	
    @Test
    public void testUserController() throws Exception {
        
        // test /user/api/login when email not confirmed
    	String uri = "/user/api/login?email=regisetasker@gmail.com&password=123";
    	MvcResult result = mvc.perform(MockMvcRequestBuilders.post(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/login failure - HTTP status", 401, status);
        Assert.assertEquals("/user/api/login failure", "{\"error\":\"please validate registration\"}", content);
        
        // test user/api/ver{id}
        uri = "/user/api/customer/{id}";
        Long id = new Long(1);
    	result = mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON)).andReturn();
        content = result.getResponse().getContentAsString();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/login failure - HTTP status", 200, status);
        Assert.assertEquals("/user/api/login failure", "Thank You", content);
        
        // test user/api/ver{id} with invalid id
        uri = "/user/api/customer/{id}";
        id = Long.MAX_VALUE;
    	result = mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON)).andReturn();
        content = result.getResponse().getContentAsString();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/login failure - HTTP status", 400, status);
        Assert.assertEquals("/user/api/login failure", "{\"error\":\"id=" + id + " dont exist\"}", content);
        
        // test user/api/login when wrong credentials
    	uri = "/user/api/login?email=regisetasker@gmail.com&password=12";
    	result = mvc.perform(MockMvcRequestBuilders.post(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
        content = result.getResponse().getContentAsString();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/login failure - HTTP status", 400, status);
        Assert.assertEquals("/user/api/login failure", "{\"error\":\"wrong password\"}", content);
        
        // test user/api/login
    	uri = "/user/api/login?email=regisetasker@gmail.com&password=123";
    	result = mvc.perform(MockMvcRequestBuilders.post(uri).session(session)).andReturn();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/login failure - HTTP status", 204, status);		
        
        // test change password with wong current password
        uri = "/user/api/password?currentpassword=12&newpassword=111&confirmpassword=111";
        result = mvc.perform(MockMvcRequestBuilders.put(uri).session(session).accept(MediaType.APPLICATION_JSON)).andReturn();
        content = result.getResponse().getContentAsString();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/password failure - HTTP status", 400, status);
        Assert.assertEquals("/user/api/password failure", "{\"error\":\"check password\"}", content);
        
        // test change password with not matching new passwords
        uri = "/user/api/password?currentpassword=123&newpassword=111&confirmpassword=11";
        result = mvc.perform(MockMvcRequestBuilders.put(uri).session(session).accept(MediaType.APPLICATION_JSON)).andReturn();
        content = result.getResponse().getContentAsString();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/password failure - HTTP status", 400, status);
        Assert.assertEquals("/user/api/password failure", "{\"error\":\"new password doesn't match\"}", content);
        
        // test change password 
        uri = "/user/api/password?currentpassword=123&newpassword=111&confirmpassword=111";
        result = mvc.perform(MockMvcRequestBuilders.put(uri).session(session).accept(MediaType.APPLICATION_JSON)).andReturn();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/password failure - HTTP status", 204, status);
        
        // update profile
        uri = "/user/api/profile?name=Tom&email=tom@email";
        result = mvc.perform(MockMvcRequestBuilders.put(uri).session(session).
        		accept(MediaType.APPLICATION_JSON)).andReturn();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/profile failure - HTTP status", 204, status);
        
        // test update profile
        uri = "/user/api/users";
        result = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
        content = result.getResponse().getContentAsString();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/users failure - HTTP status", 200, status);
        Assert.assertEquals("/user/api/users failure", "[{\"id\":1,\"email\":\"tom@email\",\"name\":\"Tom\",\"companyname\":\"any\"}]",
        		content);
        
        // test /user/api/logout
    	uri = "/user/api/logout";
    	result = mvc.perform(MockMvcRequestBuilders.post(uri).session(session)).andReturn();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/testsession failure - HTTP status", 204, status);
    }
}
