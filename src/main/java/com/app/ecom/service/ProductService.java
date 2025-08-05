package com.app.ecom.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;
import com.app.ecom.model.Product;
import com.app.ecom.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService
{
    private final ProductRepository productRepository;
    public ProductResponse createProduct(ProductRequest productRequest)
    {
        Product product = new Product();
        updateProductFromProductRequest(product,productRequest);
        Product savedProduct = productRepository.save(product);
        return mapIntoProductResponse(savedProduct);
    }
    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest)
    {
        Optional<Product> product = productRepository.findById(id);
        return product.map(existingProduct->{
            updateProductFromProductRequest(existingProduct,productRequest);
            Product updatedProduct = productRepository.save(existingProduct);
           return   mapIntoProductResponse(updatedProduct);
        });
    }
    public List<ProductResponse> getAllProducts()
    {
        return productRepository.findByActiveTrue()
                .stream().map(this::mapIntoProductResponse).collect(Collectors.toList());
    }
    public List<ProductResponse> searchProducts(String keyword)
    {
        return productRepository.searchProducts(keyword)
                .stream().map(this::mapIntoProductResponse).collect(Collectors.toList());
    }
    private void updateProductFromProductRequest(Product product, ProductRequest productRequest)
    {
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setCategory(productRequest.getCategory());
        product.setQuantity(productRequest.getQuantity());
        product.setPrice(productRequest.getPrice());
        product.setUrl(productRequest.getUrl());
    }
    private ProductResponse mapIntoProductResponse(Product product)
    {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setQuantity(product.getQuantity());
        productResponse.setPrice(product.getPrice());
        productResponse.setUrl(product.getUrl());
        productResponse.setActive(product.getActive());
        return productResponse;
    }
    public Boolean deleteProduct(Long id)
    {
        return productRepository.findById(id)
                .map(existingProduct->{
                    existingProduct.setActive(true);
                     productRepository.save(existingProduct);
                     return true;
                }).orElse(false);
    }
}
