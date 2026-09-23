package com.demo.catalogo;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/catalog")
public class CatalogoController {
    
    @Autowired
    private RestTemplate restTemplate; // Usamos el que tiene Timeout de 1s
    
    @GetMapping("/product/{id}")
    @CircuitBreaker(name = "inventario", fallbackMethod = "fallbackInventario")
    @Retry(name = "inventario")
    public String getProduct(@PathVariable String id) {
        System.out.println("👉 [Catálogo] Intentando consultar a Inventario...");
        
        String inventoryResponse = restTemplate.getForObject(
            "http://inventario:8082/inventory/stock/" + id, 
            String.class
        );
        return "{\"product\":\"Producto " + id + "\", \"inventory\":" + inventoryResponse + "}";
    }

    public String fallbackInventario(String id, Exception e) {
        System.out.println("\n\n==================================================");
        System.out.println("🚨 FALLBACK ACTIVADO: " + e.getMessage());
        System.out.println(" El Circuit Breaker/Retry está protegiendo a Catálogo");
        System.out.println("==================================================\n\n");
        
        return "{\"product\":\"Producto " + id + "\", \"inventory\":\"No disponible (Fallback)\"}";
    }
}