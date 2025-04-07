package org.keniding.service;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.keniding.enums.ProductCategory;
import org.keniding.model.Product;
import org.keniding.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductService {
    private final ProductRepository productRepository;

    @Inject
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll(int pageIndex, int pageSize, String sortField, boolean ascending) {
        Sort sort = ascending ?
                Sort.ascending(sortField): Sort.descending(sortField);

        return productRepository.findAllActive(Page.of(pageIndex, pageSize), sort);
    }

    public List<Product> findByNameContaining(String search) {
        return productRepository.findByNameContaining(search);
    }

    public List<Product> findByCategory(ProductCategory category) {
        return productRepository.findByCategory(category);
    }

    public Product findById(Long id) {
        return productRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
    }

    public Optional<Product> findBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    @Transactional
    public Product create(Product product) {
        if (product.getSku() != null && !product.getSku().isEmpty()) {
            Optional<Product> existing = productRepository.findBySku(product.getSku());
            if (existing.isPresent()) {
                throw new IllegalArgumentException("Product with SKU " + product.getSku() + " already exists");
            }
        }

        productRepository.persist(product);
        return product;
    }

    @Transactional
    public Product update(Long id, Product product) {
        Product existingProduct = findById(id);

        if (product.getSku() != null && !product.getSku().equals(existingProduct.getSku())) {
            Optional<Product> productWithSameSku = productRepository.findBySku(product.getSku());
            if (productWithSameSku.isPresent()) {
                throw new IllegalArgumentException("Ya existe un producto con el SKU: " + product.getSku());
            }
        }

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setSku(product.getSku());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setImageUrl(product.getImageUrl());
        existingProduct.setActive(product.isActive());

        return existingProduct;
    }

    @Transactional
    public void delete(Long id) {
        Product product = findById(id);
        product.setActive(false);
    }

    @Transactional
    public void deleteHard(Long id) {
        productRepository.deleteById(id);
    }

    public long count() {
        return productRepository.countActive();
    }

    @Transactional
    public Product updateStock(Long id, int quantity) {
        Product product = findById(id);

        if (product.getStock() + quantity < 0) {
            throw new IllegalArgumentException("Stock amount excedes stock");
        }

        product.setStock(product.getStock() + quantity);
        return product;
    }
}
