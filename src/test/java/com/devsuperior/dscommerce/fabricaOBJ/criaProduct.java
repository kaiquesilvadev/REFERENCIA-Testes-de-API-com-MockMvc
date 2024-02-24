package com.devsuperior.dscommerce.fabricaOBJ;

import com.devsuperior.dscommerce.entities.Category;
import com.devsuperior.dscommerce.entities.Product;

public class criaProduct {

	public static Product novoProduct() {
		Product product = new Product();
		product.setName("PC Gamer Lenovo");
		product.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, ");
		product.setPrice(2500.90);
		product.setImgUrl("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/17-big.jpg");
	
		Category category = new Category(1l , "Computadores");
		
		product.getCategories().add(category);
		
		return product;
	}
}
