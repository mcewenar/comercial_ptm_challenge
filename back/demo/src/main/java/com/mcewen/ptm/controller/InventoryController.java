package com.mcewen.ptm.controller;

import com.mcewen.ptm.domain.Product;
import com.mcewen.ptm.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final ProductService service;

    public InventoryController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/total")
    public Map<String, BigDecimal> getTotalInventoryValue() {
        return Map.of("total", service.calculateTotalInventoryValue());
    }

    @GetMapping("/max")
    public Product getProductWithMaxInventoryValue() {
        return service.findProductWithMaxInventoryValue();
    }
}
