package org.guedes.tech.service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.naming.directory.InvalidAttributesException;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.guedes.tech.model.Product;
import org.guedes.tech.repository.ProductRepository;

@ApplicationScoped
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getProducts(String productName, String brandName) {
        if (StringUtils.isNotBlank(productName) && StringUtils.isNotBlank(brandName)) {
            return productRepository.findByNameAndBrand(productName, brandName);
        } else if (StringUtils.isNotBlank(brandName)) {
            return productRepository.findByBrand(brandName);
        } else if (StringUtils.isNotBlank(productName)) {
            return productRepository.findByName(productName);
        }

        return productRepository.listAll();
    }

    public Optional<Product> findProductById(long id) {
        return productRepository.findByIdOptional(id);
    }

    @Transactional
    public void create(Product product) throws InvalidAttributesException {
        if (product.getId() != null) {
            throw new InvalidAttributesException("Id must not be filled");
        }
        Validate.notNull(product, "Product can not be null");
        Validate.notBlank(product.getBrandName(), "Brand name can not be empty");
        Validate.notBlank(product.getName(), "Name can not be empty");

        productRepository.persist(product);
    }

    @Transactional
    public Product replace(long productId, Product product) {
        product.setId(productId);
        return productRepository.update(product).orElseThrow(() -> new InvalidParameterException("Product not found"));
    }

    @Transactional
    public boolean delete(long productId) {
        return productRepository.deleteById(productId);
    }
}
