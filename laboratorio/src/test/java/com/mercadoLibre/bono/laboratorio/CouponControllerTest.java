package com.mercadoLibre.bono.laboratorio;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadoLibre.bono.laboratorio.model.RequestModel;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class CouponControllerTest {

	  private static final Logger logger = LoggerFactory.getLogger(CouponControllerTest.class);
	     
	    @Autowired
	    private MockMvc mockMvc;
	 
	    @Autowired
	    ObjectMapper objectmapper;
	     	     
	    @Test
	    void testProcessCouponOkStatus() throws Exception {
	    	List<String> items = new ArrayList<>();
	    	items.add("MCO450978136");
	    	items.add("MCO565110179");
	    	items.add("MCO562867529");
	    	items.add("MCO453221363");
	    	items.add("MCO455834206");  	
	    	 
	    	RequestModel request = new RequestModel();
	    	request.setItem_ids(items);
	    	request.setAmount(500000f);
	         
	        String response = mockMvc.perform(post("http://localhost:5050/coupon/")
	                .content(objectmapper.writeValueAsString(request))
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().is(HttpStatus.OK.value()))
	                .andReturn().getResponse().getContentAsString();
	         
	        logger.info(response);
	    }
	    
	    @Test
	    void testProcessCouponNotFoundStatus() throws Exception {
	    	List<String> items = new ArrayList<>();
	    	items.add("MCO450978136");
	    	items.add("MCO565110179");
	    	items.add("MCO562867529");
	    	items.add("MCO453221363");
	    	items.add("MCO455834206");  	
	    	 
	    	RequestModel request = new RequestModel();
	    	request.setItem_ids(items);
	    	request.setAmount(500f);
	         
	        String response = mockMvc.perform(post("http://localhost:5050/coupon/")
	                .content(objectmapper.writeValueAsString(request))
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
	                .andReturn().getResponse().getContentAsString();
	         
	        logger.info(response);
	    }

}
