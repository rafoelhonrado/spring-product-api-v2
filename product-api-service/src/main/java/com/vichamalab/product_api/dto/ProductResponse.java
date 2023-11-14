package com.vichamalab.product_api.dto;

import java.util.List;

import com.vichamalab.product_api.entity.Product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
	private List<Product> products;
	private Boolean status;
	private String message;
}
