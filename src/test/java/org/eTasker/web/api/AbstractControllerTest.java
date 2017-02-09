package org.eTasker.web.api;

import java.io.IOException;

import org.eTasker.AbstractTest;
import org.eTasker.service.UserService;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@DirtiesContext
@Transactional
public class AbstractControllerTest extends AbstractTest {

	protected MockMvc mvc;
	
	@Autowired
    protected WebApplicationContext webApplicationContext;
	@Autowired
	protected UserService userService;
	protected MockHttpSession session = new MockHttpSession();
	
	protected void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	protected void createUser() throws Exception {
		String uri = "/user/api/customer?name=John&email=regisetasker@gmail.com&companyname=any&password=123";
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
        int status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/customer failure - HTTP status", 201, status);
	}
	
	protected void login() throws Exception {
        String uri = "/user/api/customer/{id}";
        Long id = new Long(1);
    	MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON)).andReturn();
        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/login failure - HTTP status", 200, status);
        Assert.assertEquals("/user/api/login failure", "Thank You", content);
		
		uri = "/user/api/login?email=regisetasker@gmail.com&password=123";
    	result = mvc.perform(MockMvcRequestBuilders.post(uri).session(session)).andReturn();
        status = result.getResponse().getStatus();
        Assert.assertEquals("/user/api/login failure - HTTP status", 200, status);	
	}
	
    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
    
    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, clazz);
    }
}
