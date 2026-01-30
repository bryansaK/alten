package com.example.alten.dto;

import com.example.alten.entity.Product;
import com.example.alten.entity.User;
import com.example.alten.entity.WishlistItem;

public class WishlistItemRequest {

	private Long productId;

	public WishlistItemRequest() {}

	public WishlistItemRequest(Long productId) {
		this.productId = productId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public WishlistItem toEntity(User user, Product product) {
		return new WishlistItem(user, product);
	}
}
