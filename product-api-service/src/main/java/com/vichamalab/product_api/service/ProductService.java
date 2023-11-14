package com.vichamalab.product_api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vichamalab.product_api.controller.ClientError;
import com.vichamalab.product_api.controller.ServerError;
import com.vichamalab.product_api.dto.DeleteResponse;
import com.vichamalab.product_api.dto.ProductRequest;
import com.vichamalab.product_api.dto.ProductResponse;
import com.vichamalab.product_api.dto.Response;
import com.vichamalab.product_api.entity.Product;
import com.vichamalab.product_api.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {
	private final ProductRepository productRepository;
	public Response createProduct(ProductRequest productRequest) {
		Product product= null;
		Response response=null;
		try {
			if (productRequest.getName().isEmpty()) {
				throw new Exception("El nombre del producto no fue proporcionado");
			}
			if (productRequest.getDescription().isEmpty()) {
				throw new Exception("La descripción del producto no fue proporcionada");
			}
			if (productRequest.getPrice()==0) {
				throw new Exception("El precio del producto no fue proporcionado");
			}
			product = Product.builder()
					.name(productRequest.getName())
					.description(productRequest.getDescription())
					.sku(UUID.randomUUID().toString())
					.price(productRequest.getPrice())
					.build();
			productRepository.save(product);
			log.info(String.format("Producto creado con sku: %1$s",product.getSku()));
			 response = Response.builder()
						.sku(product.getSku())
						.status(true)
						.message("El producto fue creado con éxito!")
						.build();
		} catch(Exception ex) {
			 response = Response.builder()
						.sku("")
						.status(false)
						.message(ex.getMessage())
						.build();
		}
		return response;		
	}
	
	public Response updateProduct(String sku,ProductRequest productRequest) {
		Product product=null;
		String message = "El producto fue actualizado con éxito";
		boolean status = true;
		try {
			product = productRepository.findBySku(sku).orElseThrow(()-> new Exception("El producto no fue encontrado"));
			if (!productRequest.getName().isBlank()) {
				product.setName(productRequest.getName());
			} else {
				throw new Exception("El nuevo nombre no debe estar en blanco");
			}
			if (!productRequest.getDescription().isBlank()) {
				product.setDescription(productRequest.getDescription());				
			} else {
				throw new Exception("La nueva descripción no debe estar en blanco");
			}
			if (productRequest.getPrice() > 0) {
				product.setPrice(productRequest.getPrice());				
			} else {
				throw new Exception("El nuevo precio debe ser mayor a cero");
			}
			productRepository.save(product);
			log.info(String.format("Producto con sku: %1$s fue actualizado",sku));
		} catch(Exception ex) {
			message= ex.getMessage();
			status=false;
			log.info(String.format("Producto con sku: %1$s no fue encontrado",sku));
		}
		Response response = Response.builder()
				.sku(sku)
				.status(status)
				.message(message)
				.build();
		return response;		
	}
	
	public Response updatePrice(String sku,ProductRequest productRequest) {
		Product product=null;
		String message = "El precio del producto fue actualizado con éxito";
		boolean status = true;
		try {
			product = productRepository.findBySku(sku).orElseThrow(()-> new Exception("El producto no fue encontrado"));
			if (productRequest.getPrice() > 0) {
				product.setPrice(productRequest.getPrice());				
			} else {
				throw new Exception("El nuevo precio debe ser mayor a cero");
			}
			productRepository.save(product);
			log.info(String.format("El precio del producto con sku: %1$s fue actualizado con éxito",sku));
		} catch(Exception ex) {
			message= ex.getMessage();
			status=false;
			log.info(String.format("Producto con sku: %1$s no fue encontrado",sku));
		}
		Response response = Response.builder()
				.sku(sku)
				.status(status)
				.message(message)
				.build();
		return response;	
	}
	
	public DeleteResponse deleteProduct1(String sku) {
		Product product = null;
		DeleteResponse response = null;
		try {
			product = productRepository.findBySku(sku).orElseThrow(()-> new Exception("El producto no fue encontrado"));
			int count = (int) productRepository.deleteBySku(sku);
			String message = "El producto fue eliminado con éxito";
			Boolean status = true;
			if (count!=1) {
				status=false;
				message = "No se pudo eliminar el producto";
			}
			log.info(message);
		}catch(Exception ex) {
			response = DeleteResponse.builder().count(0).message(ex.getMessage()).status(false).build();
			log.info(String.format("Producto con sku: %1$s no fue encontrado",sku));
		}	
		return response;
	}
	
	public DeleteResponse deleteProduct(String sku) throws ClientError,ServerError {
		Product product = null;
		String message = "El producto fue eliminado con éxito";
		product = productRepository.findBySku(sku).orElseThrow(()-> new ClientError("El producto no fue encontrado"));
		int count = (int) productRepository.deleteBySku(product.getSku());
		if (count!=1) {
			throw new ServerError("El producto no pudo ser eliminado");
		}
		return DeleteResponse.builder().count(count).message(message).status(true).build();
	}
	
	public ProductResponse getProduct(String sku) {
		String message="El producto fue encontrado";
		List<Product> products = new ArrayList<Product>();
		Boolean status = true;
		try {
			Product product = productRepository.findBySku(sku).orElseThrow(()-> new Exception("El producto no fue encontrado"));
			products.add(product);
		} catch(Exception ex) {
			status = false;
			message= ex.getMessage();
		} 
		return ProductResponse.builder()
				.message(message)
				.status(status)
				.products(products)
				.build();
	}
	
	public ProductResponse getProducts(String name) {
		List<Product> products;
		if (name.isEmpty()) {
			products = productRepository.findAll();
		} else {
			products = productRepository.findByNameLike("%"+name+"%");
		}
		return ProductResponse.builder()
				.message(String.format("Se encontró %1$d producto(s)", products.size()))
				.status(true)
				.products(products)
				.build();
	}
	
	public ProductResponse createProductImproved(ProductRequest productRequest) {
		Product product= null;
		ProductResponse response=null;
		List<Product> products = new ArrayList<Product>();
		String sku = UUID.randomUUID().toString();
		try {
			if (productRequest.getName().isEmpty()) {
				throw new Exception("El nombre del producto no fue proporcionado");
			}
			if (productRequest.getDescription().isEmpty()) {
				throw new Exception("La descripción del producto no fue proporcionada");
			}
			if (productRequest.getPrice()==0) {
				throw new Exception("El precio del producto no fue proporcionado");
			}
			product = Product.builder()
					.name(productRequest.getName())
					.description(productRequest.getDescription())
					.sku(sku)
					.price(productRequest.getPrice())
					.build();
			product = productRepository.save(product);
			//product = productRepository.findBySku(sku).orElseThrow(()-> new Exception("El producto no fue encontrado"));
			products.add(product);
			log.info(String.format("Producto creado con sku: %1$s",product.getSku()));
			 response = ProductResponse.builder()
						.products(products)
						.status(true)
						.message("El producto fue creado con éxito!")
						.build();
		} catch(Exception ex) {
			 response = ProductResponse.builder()
						.products(products)
						.status(false)
						.message(ex.getMessage())
						.build();
		}
		return response;		
	}
}
