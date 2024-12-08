package com.example;

import com.example.models.Product;
import com.example.models.Client;
import com.example.repository.ClientRepository;
import com.example.repository.ProductRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import java.time.LocalDate;
import java.util.List;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class App {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(App.class, args);
        var productRepository = context.getBean(ProductRepository.class);
        var clientRepository = context.getBean(ClientRepository.class);
        var mongo = context.getBean(MongoTemplate.class);
        // Verificar y guardar productos
        if (productRepository.count() == 0) { // Solo insertar si no hay productos
            List<Product> products = List.of(
                    new Product(null, "Camiseta de algodón", 15.99, 50),
                    new Product(null, "Pantalones vaqueros", 39.99, 30),
                    new Product(null, "Zapatillas deportivas", 59.99, 20),
                    new Product(null, "Reloj inteligente", 199.99, 10),
                    new Product(null, "Auriculares inalámbricos", 89.99, 15),
                    new Product(null, "Mochila de viaje", 29.99, 25),
                    new Product(null, "Cámara digital", 499.99, 5),
                    new Product(null, "Libro de cocina", 24.99, 40),
                    new Product(null, "Juego de mesa", 34.99, 12),
                    new Product(null, "Botella de agua reutilizable", 12.99, 60)
            );
            productRepository.saveAll(products);
        }

        // Verificar y guardar clientes
        if (clientRepository.count() == 0) { // Solo insertar si no hay clientes
            List<Client> clientes = List.of(
                    new Client(null, "Juan Pérez", "12345678A", LocalDate.of(1985, 5, 15), "Masculino"),
                    new Client(null, "María García", "87654321B", LocalDate.of(1990, 8, 22), "Femenino"),
                    new Client(null, "Carlos Rodríguez", "11223344C", LocalDate.of(1978, 3, 10), "Masculino"),
                    new Client(null, "Ana Martínez", "44332211D", LocalDate.of(1995, 11, 30), "Femenino"),
                    new Client(null, "Luis Sánchez", "55667788E", LocalDate.of(1982, 7, 5), "Masculino"),
                    new Client(null, "Laura Fernández", "99887766F", LocalDate.of(1988, 1, 18), "Femenino")
            );
            clientRepository.saveAll(clientes);
        }
    }
}
