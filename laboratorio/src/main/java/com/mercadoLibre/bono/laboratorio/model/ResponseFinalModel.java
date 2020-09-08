package com.mercadoLibre.bono.laboratorio.model;

import java.util.List;

public class ResponseFinalModel {

	
	private List<String> item_ids;
	
	private Float total;
	
	public List<String> getItem_ids() {
		return item_ids;
	}
	public void setItem_ids(List<String> item_ids) {
		this.item_ids = item_ids;
	}
	public Float getTotal() {
		return total;
	}
	public void setTotal(Float total) {
		this.total = total;
	}	
}
