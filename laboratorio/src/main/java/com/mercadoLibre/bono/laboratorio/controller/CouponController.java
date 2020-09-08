package com.mercadoLibre.bono.laboratorio.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.mercadoLibre.bono.laboratorio.model.RequestModel;
import com.mercadoLibre.bono.laboratorio.model.ResponseFinalModel;
import com.mercadoLibre.bono.laboratorio.service.CouponService;

@RestController
@RequestMapping("/coupon")
public class CouponController {

	private static final Logger logger = LogManager.getLogger(CouponController.class);
	
	@Autowired
	public CouponService couponService;
	
    @GetMapping({"/", "hello"})
    public String helloWorld(@RequestParam(required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "hello-world";
    }
	
	@PostMapping
	public ResponseEntity<ResponseFinalModel> processCoupon(@Valid @RequestBody RequestModel request, BindingResult result){
        if (result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, result.toString());
        }
		Map<String, Float> itemsMap = couponService.processRequest(request);
		List<String> responseList = couponService.calculate(itemsMap, request.getAmount());
		ResponseFinalModel response  = couponService.formatResponse(responseList); 

		if(response == null || response.getTotal().equals(0f)) {
			logger.info("No se pudieron sugerir productos..");
			return  ResponseEntity.status( HttpStatus.NOT_FOUND).body(response);
		}
				
        return  ResponseEntity.status( HttpStatus.OK).body(response);
	}
}
