package com.backend.engine.config;

import com.backend.engine.model.Product;
import com.backend.engine.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() == 0) {
                Product product = new Product();
                product.setName("Flash Sale Item X");
                product.setPrice(49.99);
                product.setStockQuantity(10);
                productRepository.save(product);
                System.out.println("Seeded test product with ID: 1 and Stock: 10");
            }
        };
    }
}