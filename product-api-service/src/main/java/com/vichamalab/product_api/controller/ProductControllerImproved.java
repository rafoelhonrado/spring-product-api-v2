package com.vichamalab.product_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.vichamalab.product_api.dto.ProductRequest;
import com.vichamalab.product_api.dto.ProductResponse;
import com.vichamalab.product_api.dto.Response;
import com.vichamalab.product_api.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v2/product/")
@RequiredArgsConstructor
@Slf4j
public class ProductControllerImproved {
	private final ProductService productService;
	
	@PostMapping
	private ResponseEntity<ProductResponse> saveProduct(@RequestBody ProductRequest productRequest) {
		log.info(String.format("saveProduct"));
		ProductResponse response = productService.createProductImproved(productRequest);
		if (response.getStatus()) {
			return new ResponseEntity<ProductResponse>(response,HttpStatus.CREATED);
		} else {
			return new ResponseEntity<ProductResponse>(response,HttpStatus.BAD_REQUEST);
		}
	}
}
