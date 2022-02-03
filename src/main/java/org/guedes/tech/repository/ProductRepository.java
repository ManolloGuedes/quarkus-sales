package org.guedes.tech.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import org.apache.commons.text.WordUtils;
import org.guedes.tech.model.Product;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

    public void persist(Product product) {
        var productName = WordUtils.capitalize(product.getName());
        var brandName = WordUtils.capitalize(product.getBrandName());

        product.setName(productName);
        product.setBrandName(brandName);

        PanacheRepository.super.persist(product);
    }

    public List<Product> findByName(String productName) {
        return this.list("name", WordUtils.capitalize(productName));
    }

    public List<Product> findByBrand(String productBrand) {
        return this.list("brandName", WordUtils.capitalize(productBrand));
    }

    public List<Product> findByNameAndBrand(String productName, String brandName) {
        return this.list("name = ?1 and brandName = ?2", productName, brandName);
    }

    public Optional<Product> update(Product product) {
        final var id = product.getId();
        var savedOpt = this.findByIdOptional(id);
        if (savedOpt.isEmpty()) {
            return Optional.empty();
        }

        var saved = savedOpt.get();
        saved.setName(product.getName());
        saved.setPrice(product.getPrice());
        saved.setBrandName(product.getBrandName());

        return Optional.of(saved);
    }

}
