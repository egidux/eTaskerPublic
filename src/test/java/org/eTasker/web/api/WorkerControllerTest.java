package org.eTasker.web.api;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class WorkerControllerTest extends AbstractControllerTest {

	@Before
	public void setup() throws Exception {
		super.setUp();
		super.createUser();
		super.login();
	}
	
	@Test
	public void testWorkerController() throws Exception {

		// test get workers
    	String uri = "/user/api/workers";
    	MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri).session(session)).andReturn();
    	String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/login failure - HTTP status", 200, status);
        Assert.assertEquals("/user/api/login failure", "[]", content);
        
        // create new worker with missing param
        uri = "/user/api/workers";
        result = mvc.perform(MockMvcRequestBuilders.post(uri).session(session)).andReturn();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/customer failure - HTTP status", 400, status);
        
        // create new worker
        uri += "?name=John&email=regisetasker@gmail.com&companyname=any&password=123&";
        result = mvc.perform(MockMvcRequestBuilders.post(uri).session(session).accept(MediaType.APPLICATION_JSON)).andReturn();
        status = result.getResponse().getStatus();
        content = result.getResponse().getContentAsString();
        Assert.assertEquals("/user/api/customer failure - HTTP status", 201, status);
        Assert.assertEquals("/user/api/register failure", "{\"id\":1,\"email\":\"regisetasker@gmail.com\"," +
        		"\"name\":\"John\",\"password\":\"123\",\"companyname\":\"any\"}", content);
        
        // create with existing worker
        result = mvc.perform(MockMvcRequestBuilders.post(uri).session(session).accept(MediaType.APPLICATION_JSON)).andReturn();
        status = result.getResponse().getStatus();
        content = result.getResponse().getContentAsString();
        Assert.assertEquals("/user/api/register failure - HTTP status", 409, status);
        Assert.assertEquals("/user/api/register failure", "{\"error\":\"worker exists\"}", content);
        
    	// get workers
    	uri = "/user/api/workers";
    	result = mvc.perform(MockMvcRequestBuilders.get(uri).session(session).accept(MediaType.APPLICATION_JSON)).andReturn();
        content = result.getResponse().getContentAsString();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/users failure - HTTP status", 200, status);
        Assert.assertEquals("/user/api/users failure", "[{\"id\":1,\"email\":\"regisetasker@gmail.com\"," +
        		"\"name\":\"John\",\"password\":\"123\",\"companyname\":\"any\"}]", content);
        
        // update worker
        uri = "/user/api/workers?name=TT&companyname=audi&password=1";
        result = mvc.perform(MockMvcRequestBuilders.put(uri).session(session).
        		accept(MediaType.APPLICATION_JSON)).andReturn();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/profile failure - HTTP status", 204, status);
        
        // test update worker
        uri = "/user/api/workers";
        result = mvc.perform(MockMvcRequestBuilders.get(uri).session(session).accept(MediaType.APPLICATION_JSON)).andReturn();
        content = result.getResponse().getContentAsString();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/users failure - HTTP status", 200, status);
        Assert.assertEquals("/user/api/users failure", "[{\"id\":1,\"email\":\"regisetasker@gmail.com\",\"name\":\"TT\",\"password\":\"1\",\"companyname\":\"audi\"}]",
        		content);
 
	}
	
	@After
	public void after() throws Exception {
    	String uri = "/user/api/logout";
    	MvcResult result = mvc.perform(MockMvcRequestBuilders.post(uri).session(session)).andReturn();
        int status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/testsession failure - HTTP status", 204, status);
	}
}
