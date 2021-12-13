package com.aws.peach.infrastructure.dummy;

import com.aws.peach.domain.product.entity.Product;
import com.aws.peach.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDummyRepository implements ProductRepository {


    @Override
    public List<Product> findByIds(List<String> productIds) {
        List<Product> products = new ArrayList<>();
        products.add(Product.builder().price(25000).name("천중도").id("01").build());
        products.add(Product.builder().price(25000).name("황도").id("02").build());
        return  products;
    }
}
