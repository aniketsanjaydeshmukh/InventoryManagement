package com.inventory.services;

import java.util.List;

import com.inventory.model.Product;

public interface ProductServices {
	boolean addOrUpdateProduct(Product product);
	Product getProductByNameAndModel(String productName,String modelNumber);
	boolean deleteProduct(long productId);
	List<Product> productList();
	Product getProductById(long productId);
}
