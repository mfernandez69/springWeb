package com.example;

import com.example.models.Product;
import com.example.models.Client;
import com.example.repository.ClientRepository;
import com.example.repository.ProductRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(App.class, args);
        var repository = context.getBean(ProductRepository.class);
        var clientRepository = context.getBean(ClientRepository.class);

        List<Product> products = List.of(
                new Product(null, "product1", 5.99, 1),
                new Product(null, "product2", 6.99, 2),
                new Product(null, "product3", 7.99, 4),
                new Product(null, "product4", 8.99, 2),
                new Product(null, "product5", 8.99, 2),
                new Product(null, "product6", 8.99, 2)
        );
        List<Client> clientes = List.of(
                new Client(null, "Juan Pérez", "12345678A", LocalDate.of(1985, 5, 15), "Masculino"),
                new Client(null, "María García", "87654321B", LocalDate.of(1990, 8, 22), "Femenino"),
                new Client(null, "Carlos Rodríguez", "11223344C", LocalDate.of(1978, 3, 10), "Masculino"),
                new Client(null, "Ana Martínez", "44332211D", LocalDate.of(1995, 11, 30), "Femenino"),
                new Client(null, "Luis Sánchez", "55667788E", LocalDate.of(1982, 7, 5), "Masculino"),
                new Client(null, "Laura Fernández", "99887766F", LocalDate.of(1988, 1, 18), "Femenino")
        );
        clientRepository.saveAll(clientes);
        repository.saveAll(products);
    }

}
