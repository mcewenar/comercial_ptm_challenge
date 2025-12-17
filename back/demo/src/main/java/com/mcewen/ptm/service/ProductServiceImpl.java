package com.mcewen.ptm.service;

import com.mcewen.ptm.domain.Product;

import com.mcewen.ptm.dto.*;
import com.mcewen.ptm.exception.NotFoundException;
import com.mcewen.ptm.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Comparator;

@Service
@AllArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    @Override
    public ProductResponse create(ProductCreateRequest req) {
        Product entity = new Product();
        entity.setName(req.name());
        entity.setDescription(req.description());
        entity.setPrice(req.price());
        entity.setStockQuantity(req.quantityStock());

        Product saved = repository.save(entity);
        return toResponse(saved);
    }

        @Override
        @Transactional(readOnly = true)
        public List<ProductResponse> findAll() {
            return repository.findAll().stream()
                    .map(this::toResponse)
                    .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public ProductResponse findById(Long id) {
            Product product = repository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Product not found with id=" + id));
            return toResponse(product);
        }

        @Override
        public ProductResponse update(Long id, ProductUpdateRequest req) {
            Product product = repository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Product not found with id=" + id));

            product.setName(req.name());
            product.setDescription(req.description());
            product.setPrice(req.price());
            product.setStockQuantity(req.quantityStock());

            return toResponse(product);
        }

        @Override
        public void delete(Long id) {
            if (!repository.existsById(id)) {
                throw new NotFoundException("Product not found with id=" + id);
            }
            repository.deleteById(id);
        }

        private ProductResponse toResponse(Product p) {
            return new ProductResponse(
                    p.getId(),
                    p.getName(),
                    p.getDescription(),
                    p.getPrice(),
                    p.getStockQuantity());
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryTotalResponse inventoryTotal() {
        BigDecimal total = repository.findAll().stream().map(p -> p.getPrice().multiply(BigDecimal.valueOf(p.getStockQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new InventoryTotalResponse(total);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryMaxResponse inventoryMax() {
        return repository.findAll().stream()
                .max(Comparator.comparing(p ->
                        p.getPrice().multiply(BigDecimal.valueOf(p.getStockQuantity()))
                ))
                .map(p -> new InventoryMaxResponse(p.getId(), p.getName(), p.getPrice().multiply(BigDecimal.valueOf(p.getStockQuantity()))
                ))
                .orElse(null);
    }
}
