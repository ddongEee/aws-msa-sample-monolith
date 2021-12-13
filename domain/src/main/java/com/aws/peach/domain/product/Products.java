package com.aws.peach.domain.product;

import com.aws.peach.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class Products {
    private Map<String, Product> productMap = new HashMap<String, Product>();

    public Products(List<Product> products){
        this.productMap = products.stream() .collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    public String getProductName(final String productId){
        return this.productMap.get(productId).getName();
    }

    public Product getProduct(final String productId){
        return this.productMap.get(productId);
    }

    public static Products create(final List<Product> products){
        return new Products(products);
    }

}
