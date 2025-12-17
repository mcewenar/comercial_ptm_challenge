package com.mcewen.ptm.controller;

import com.mcewen.ptm.domain.Product;
import com.mcewen.ptm.dto.ProductCombinationResponse;
import com.mcewen.ptm.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@Valid @RequestBody Product product) {
        return service.create(product);
    }

    @PutMapping("/{id}")
    public Product update(
            @PathVariable Long id,
            @Valid @RequestBody Product product
    ) {
        return service.update(id, product);
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    public List<Product> findAll() {
        return service.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }


    @GetMapping("/combinations")
    public List<ProductCombinationResponse> getBestCombinations(@RequestParam BigDecimal budget) {
        return service.findBestCombinations(budget);
    }

}
