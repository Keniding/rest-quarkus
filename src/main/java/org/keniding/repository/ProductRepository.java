package org.keniding.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.keniding.enums.ProductCategory;
import org.keniding.model.Product;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
    private static final String ACTIVE_FIELD = "active";

    public List<Product> findAllActive() {
        return list(ACTIVE_FIELD, true);
    }

    public List<Product> findAllActive(Page page, Sort sort) {
        return find(ACTIVE_FIELD, sort, true).page(page).list();
    }

    public List<Product> findByNameContaining(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de búsqueda no puede estar vacío");
        }
        return list("name LIKE :name AND " + ACTIVE_FIELD + " = true",
                Parameters.with("name", "%" + name.trim() + "%"));
    }

    public List<Product> findByCategory(ProductCategory category) {
        return list("category = :category AND " + ACTIVE_FIELD + " = true",
                Parameters.with("category", category));
    }

    public Optional<Product> findBySku(String sku) {
        return find("sku", sku).firstResultOptional();
    }

    public long countActive() {
        return count(ACTIVE_FIELD, true);
    }
}
