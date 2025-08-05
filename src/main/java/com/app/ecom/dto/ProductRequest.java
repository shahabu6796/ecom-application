package com.app.ecom.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductRequest
{
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String category;
    private String url;
}
