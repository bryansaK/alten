package com.example.alten.dto;

public class ProductRequest {
    
    private Long id;
    private Integer quantity;
    private String internalReference;
    private String code;
    private String name;
    private String image;
    private Long shellId;
    private String inventoryStatus;
    
    public ProductRequest() {}
    
    public ProductRequest(Long id, Integer quantity, String internalReference, String code, 
                         String name, String image, Long shellId, String inventoryStatus) {
        this.id = id;
        this.quantity = quantity;
        this.internalReference = internalReference;
        this.code = code;
        this.name = name;
        this.image = image;
        this.shellId = shellId;
        this.inventoryStatus = inventoryStatus;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public String getInternalReference() {
        return internalReference;
    }
    
    public void setInternalReference(String internalReference) {
        this.internalReference = internalReference;
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
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
    
    public Long getShellId() {
        return shellId;
    }
    
    public void setShellId(Long shellId) {
        this.shellId = shellId;
    }
    
    public String getInventoryStatus() {
        return inventoryStatus;
    }
    
    public void setInventoryStatus(String inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
    }
}
