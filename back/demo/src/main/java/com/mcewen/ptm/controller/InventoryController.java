package com.mcewen.ptm.controller;

import com.mcewen.ptm.dto.InventoryMaxResponse;
import com.mcewen.ptm.dto.InventoryTotalResponse;
import com.mcewen.ptm.service.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final ProductService service;

    public InventoryController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/total")
    public InventoryTotalResponse total() {
        return service.inventoryTotal();
    }

    @GetMapping("/max")
    public InventoryMaxResponse max() {
        return service.inventoryMax();
    }
}
