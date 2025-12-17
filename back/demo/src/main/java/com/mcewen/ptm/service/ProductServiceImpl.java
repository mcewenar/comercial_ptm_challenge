package com.mcewen.ptm.service;

import com.mcewen.ptm.domain.Product;
import com.mcewen.ptm.dto.ProductCombinationResponse;
import com.mcewen.ptm.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product create(Product product) {
        return repository.save(product);
    }

    @Override
    public Product update(Long id, Product product) {
        Product existing = findById(id);

        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStockQuantity(product.getStockQuantity());

        return repository.save(existing);
    }

    @Override
    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id " + id));
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long id) {
        repository.delete(findById(id));
    }

    @Override
    public BigDecimal calculateTotalInventoryValue() {
        return repository.findAll()
                .stream()
                .map(p -> p.getPrice().multiply(BigDecimal.valueOf(p.getStockQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Product findProductWithMaxInventoryValue() {
        return repository.findAll()
                .stream()
                .max(Comparator.comparing(
                        p -> p.getPrice().multiply(BigDecimal.valueOf(p.getStockQuantity()))
                ))
                .orElse(null);
    }


    @Override
    public List<ProductCombinationResponse> findBestCombinations(BigDecimal budget) {
        if (budget == null || budget.compareTo(BigDecimal.ZERO) <= 0) {
            return List.of();
        }

        List<Product> products = repository.findAll();
        int n = products.size();
        if (n < 2) {
            return List.of();
        }

        record Combo(List<String> names, BigDecimal sum) {}

        List<Combo> combos = new java.util.ArrayList<>();

        for (int i = 0; i < n; i++) {
            Product a = products.get(i);
            BigDecimal pa = a.getPrice();

            for (int j = i + 1; j < n; j++) {
                Product b = products.get(j);
                BigDecimal sum2 = pa.add(b.getPrice());

                if (sum2.compareTo(budget) <= 0) {
                    combos.add(new Combo(List.of(a.getName(), b.getName()), sum2));
                }

                for (int k = j + 1; k < n; k++) {
                    Product c = products.get(k);
                    BigDecimal sum3 = sum2.add(c.getPrice());

                    if (sum3.compareTo(budget) <= 0) {
                        combos.add(new Combo(List.of(a.getName(), b.getName(), c.getName()), sum3));
                    }
                }
            }
        }

        return combos.stream()
                .sorted((x, y) -> y.sum().compareTo(x.sum())) // DESC
                .limit(5)
                .map(c -> new ProductCombinationResponse(c.names(), c.sum()))
                .toList();
    }

}
