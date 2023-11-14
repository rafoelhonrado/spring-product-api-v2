package com.vichamalab.product_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.vichamalab.product_api.dto.DeleteResponse;
import com.vichamalab.product_api.dto.ProductRequest;
import com.vichamalab.product_api.dto.ProductResponse;
import com.vichamalab.product_api.dto.Response;
import com.vichamalab.product_api.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/product/")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
	private final ProductService productService;

	@PostMapping
	private ResponseEntity<Response> saveProduct(@RequestBody ProductRequest productRequest) {
		log.info(String.format("saveProduct"));
		Response response = productService.createProduct(productRequest);
		if (response.getStatus()) {
			return new ResponseEntity<Response>(response, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/{sku}/")
	private ResponseEntity<Response> updateProduct(@PathVariable(value = "sku") String sku,
			@RequestBody ProductRequest productRequest) {
		log.info(String.format("updateProduct details:%1$s with sku:%2$s ", sku, productRequest.toString()));
		Response response = productService.updateProduct(sku, productRequest);
		if (response.getStatus()) {
			return new ResponseEntity<Response>(response, HttpStatus.OK);
		} else {
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping("/{sku}/")
	private ResponseEntity<Response> updatePrice(@PathVariable(value = "sku") String sku,
			@RequestBody ProductRequest productRequest) {
		log.info(String.format("updatePrice %1$s with sku:%2$s ", sku, productRequest.toString()));
		Response response = productService.updatePrice(sku, productRequest);
		if (response.getStatus()) {
			return new ResponseEntity<Response>(response, HttpStatus.OK);
		} else {
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/{sku}/")
	private ResponseEntity<DeleteResponse> deleteProduct(@PathVariable(value = "sku") String sku) {
		try {
			log.info(String.format("deleteProduct with sku:%1$s ", sku));
			DeleteResponse response = productService.deleteProduct(sku);
			return new ResponseEntity<DeleteResponse>(response,HttpStatus.OK);
		} catch (ServerError serverError) {
			return new ResponseEntity<DeleteResponse>(
					DeleteResponse.builder().count(0).message(serverError.getMessage()).status(false).build(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ClientError clientError) {
			return new ResponseEntity<DeleteResponse>(
					DeleteResponse.builder().count(0).message(clientError.getMessage()).status(false).build(),
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/{sku}/")
	@ResponseStatus(HttpStatus.OK)
	public ProductResponse getProductBySku(@PathVariable(value = "sku") String sku) {
		log.info(String.format("getProductBySku with sku:%1$s ", sku));
		return productService.getProduct(sku);
	}

	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	public ProductResponse getProductsByName(@RequestParam(name = "name", defaultValue = "") String name) {
		log.info("getProducts");
		return productService.getProducts(name);
	}
}
