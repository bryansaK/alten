package com.example.alten.dto;

import java.math.BigDecimal;

import com.example.alten.entity.Product;

public class ProductRequest {

	private String code;
	private String name;
	private BigDecimal price;
	private Integer quantity;
	private String inventoryStatus;

	public ProductRequest() {}

	public ProductRequest(String code, String name, BigDecimal price, Integer quantity, String inventoryStatus) {
		this.code = code;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.inventoryStatus = inventoryStatus;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getInventoryStatus() {
		return inventoryStatus;
	}

	public void setInventoryStatus(String inventoryStatus) {
		this.inventoryStatus = inventoryStatus;
	}

	public Product toEntity() {
		Product product = new Product();
		product.setCode(code);
		product.setName(name);
		product.setPrice(price);
		product.setQuantity(quantity != null ? quantity : 0);
		product.setInventoryStatus(inventoryStatus != null ? inventoryStatus : "INSTOCK");
		return product;
	}

	public void applyTo(Product product) {
		if (code != null) {
			product.setCode(code);
		}
		if (name != null) {
			product.setName(name);
		}
		if (price != null) {
			product.setPrice(price);
		}
		if (quantity != null) {
			product.setQuantity(quantity);
		}
		if (inventoryStatus != null) {
			product.setInventoryStatus(inventoryStatus);
		}
	}
}
