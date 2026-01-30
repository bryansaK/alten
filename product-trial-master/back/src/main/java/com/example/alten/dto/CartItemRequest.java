package com.example.alten.dto;

import com.example.alten.entity.CartItem;
import com.example.alten.entity.Product;
import com.example.alten.entity.User;

public class CartItemRequest {

	private Long productId;
	private Integer quantity;

	public CartItemRequest() {}

	public CartItemRequest(Long productId, Integer quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public CartItem toEntity(User user, Product product) {
		return new CartItem(user, product, this.quantity);
	}

	public void applyTo(CartItem cartItem) {
		if (this.quantity != null) {
			cartItem.setQuantity(this.quantity);
		}
	}
}
