package com.mercadoLibre.bono.laboratorio.service;

import java.util.List;
import java.util.Map;

import com.mercadoLibre.bono.laboratorio.model.RequestModel;
import com.mercadoLibre.bono.laboratorio.model.ResponseFinalModel;

public interface CouponService {

	public List<String> calculate(Map<String, Float> items, Float floatAmount);
	
	public Map<String, Float> processRequest(RequestModel request);
	
	public ResponseFinalModel formatResponse(List<String> list) ;
	
}
