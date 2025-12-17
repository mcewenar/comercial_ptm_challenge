package com.mcewen.ptm.repository;

import com.mcewen.ptm.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
