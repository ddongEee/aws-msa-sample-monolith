package com.aws.peach.domain.product.repository;


import com.aws.peach.domain.product.entity.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> findByIds(List<String> productIds);
}
