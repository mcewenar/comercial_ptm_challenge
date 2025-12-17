package com.mcewen.ptm.service;

import com.mcewen.ptm.domain.Product;
import com.mcewen.ptm.dto.ProductCombinationResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    Product create(Product product);

    Product update(Long id, Product product);

    Product findById(Long id);

    List<Product> findAll();

    void delete(Long id);

    BigDecimal calculateTotalInventoryValue();

    Product findProductWithMaxInventoryValue();

    List<ProductCombinationResponse> findBestCombinations(BigDecimal budget);
}
