package com.store.service;

import com.store.model.Product;
import com.store.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repo;

    @InjectMocks
    private ProductService service;

    @Test
    void shouldReturnAllProducts() {

        when(repo.findAll()).thenReturn(List.of(
                new Product(1L, "Laptop", "Gaming", 1000, 5),
                new Product(2L, "Mouse", "Wireless", 20, 10)
        ));

        List<Product> result = service.getAllProducts();

        assertEquals(2, result.size());
        verify(repo, times(1)).findAll();
    }

    @Test
    void shouldSaveProduct() {

        Product p = new Product(null, "Phone", "Android", 500, 3);

        when(repo.save(any(Product.class))).thenReturn(p);

        Product saved = service.create(p);

        assertEquals("Phone", saved.getName());
        verify(repo).save(p);
    }
}
