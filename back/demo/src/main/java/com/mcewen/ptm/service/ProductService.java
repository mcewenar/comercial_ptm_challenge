package com.mcewen.ptm.service;

import com.mcewen.ptm.dto.*;

import java.util.List;

public interface ProductService {

    ProductResponse create(ProductCreateRequest req);
    List<ProductResponse> findAll();
    ProductResponse findById(Long id);
    ProductResponse update(Long id, ProductUpdateRequest req);
    void delete(Long id);

    InventoryTotalResponse inventoryTotal();
    InventoryMaxResponse inventoryMax();
}
