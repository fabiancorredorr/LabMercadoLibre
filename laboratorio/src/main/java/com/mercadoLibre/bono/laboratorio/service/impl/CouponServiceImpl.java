 package com.mercadoLibre.bono.laboratorio.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.mercadoLibre.bono.laboratorio.model.RequestModel;
import com.mercadoLibre.bono.laboratorio.model.ResponseFinalModel;
import com.mercadoLibre.bono.laboratorio.service.CouponService;

import io.swagger.client.ApiException;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.ItemResponse;
import io.swagger.client.model.RefreshToken;

@Service
public class CouponServiceImpl implements CouponService{

	private static final Logger logger = LogManager.getLogger(CouponServiceImpl.class);
	private RefreshToken refToken;
	

	@Override
	/**
	 * This Method calculate the max value over all combinations
	 */
	public List<String> calculate(Map<String, Float> items, Float amount) {
		
		List<String> resultList = new ArrayList<>();
		try {
			if(items == null)
				return resultList;
				
			List<String> keys = new ArrayList<>(items.keySet());
			
			Float maxValue = 0f;
				
		    List<List<String>> listSet = new LinkedList<>();
	
		    float totalVal;
		    
		    for (int i = 1; i <= keys.size(); i++)
		        listSet.addAll(combination(keys, i));
	
		    for(List<String> listIn : listSet) {
		    	totalVal = 0f;
		    	for(String element : listIn) {
		    		totalVal += items.get(element);
		    	}
		    	if(totalVal > maxValue && totalVal <= amount) {
		    		maxValue = totalVal;
		    		resultList.clear();
		    		resultList.addAll(listIn);
		    	}
		    }
		    resultList.add(maxValue+"");
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return resultList;
	}
	
/**
 * This method make sets from all combinations possibles
 * @param values Id's list favorite products
 * @param size set size
 * @return All posible combinations
 */
	public List<List<String>> combination(List<String> values, int size) {

		List<List<String>> combination = new LinkedList<>();
		try {			
		    if (0 == size) {
		        return Collections.singletonList(Collections.<String> emptyList());
		    }
	
		    if (values.isEmpty()) {
		        return Collections.emptyList();
		    }
	
		    String actual = values.iterator().next();
	
		    List<String> subSet = new LinkedList<>(values);
		    subSet.remove(actual);
	
		    List<List<String>> subSetCombination = combination(subSet, size - 1);
	
		    for (List<String> set : subSetCombination) {
		        List<String> newSet = new LinkedList<>(set);
		        newSet.add(0, actual);
		        combination.add(newSet);
		    }
		    combination.addAll(combination(subSet, size));
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
	    return combination;
	}
	

	@Override
	/**
	 * Make request to MercadoLibre API
	 */
	public Map<String, Float> processRequest(RequestModel request) {
		Map<String, Float> items = new TreeMap<>();
		try {
			List<String> listItems = request.getItem_ids(); 
			DefaultApi apiInstance = new DefaultApi();
			String token = refToken != null ? refToken.getRefresh_token() : "TG-5f555bd211bdf4000621af6a-268809227";
			refToken = this.makeRefreshTokenRequest(token);
			String accessToken = refToken != null ? refToken.getAccess_token() : ""; 
					
			Set<String> set = new TreeSet<>(listItems);
			set.stream().forEach(item -> {
				try {
				    ItemResponse result = apiInstance.itemsItemIdGet(item, accessToken);
				    items.put(result.getId() + "", result.getPrice().floatValue());
				    logger.info(result);
				} catch (ApiException e) {
				    logger.error(e.getMessage());
				}
			});	
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return items;
	}
	
	/**
	 * Make Refresh token request
	 * @param refreshToken previous refresh token
	 * @return new refresh token
	 */
	private RefreshToken makeRefreshTokenRequest(String refreshToken) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			MultiValueMap<String, Object> values = new LinkedMultiValueMap<>();
			values.add("grant_type", "refresh_token");
			values.add("client_id", "8549570105031151");
			values.add("client_secret", "S3278rIWgPY1nerCJJ9VHQjcxTkry4kc");
			values.add("refresh_token", refreshToken);
			return restTemplate.postForObject("https://api.mercadolibre.com/oauth/token", values, RefreshToken.class);
		}catch(Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Make token request
	 * @return
	 */
	private RefreshToken makeTokenRequest() {
		try {
			RestTemplate restTemplate = new RestTemplate();
			MultiValueMap<String, Object> values = new LinkedMultiValueMap<>();
			values.add("grant_type", "authorization_code");
			values.add("client_id", "8549570105031151");
			values.add("client_secret", "S3278rIWgPY1nerCJJ9VHQjcxTkry4kc");
			values.add("code", "TG-5f555b9656352a0006909d88-268809227");
			
			return restTemplate.postForObject("https://api.mercadolibre.com/oauth/token", values, RefreshToken.class);
		}catch(Exception e) {
			logger.error(e.getMessage());
			return null;
		}
		
	}

	@Override
	/**
	 * Formatted response according to API sign
	 */
	public ResponseFinalModel formatResponse(List<String> list) {
		Float totalAmount;
		ResponseFinalModel responseFinal = new ResponseFinalModel();
		
		if(list == null) {
			return responseFinal;
		}
			
		if(list.size() == 1 ) {
			list.clear();
			responseFinal.setItem_ids(list);
			responseFinal.setTotal(0f);
		}else {
			totalAmount = Float.parseFloat(list.get(list.size()-1));
			list.remove(list.size()-1);
			responseFinal.setItem_ids(list);
			responseFinal.setTotal(totalAmount);
		}
		return responseFinal;
	}

}
