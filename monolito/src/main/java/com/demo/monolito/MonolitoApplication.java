package com.demo.monolito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
@RequestMapping("/app")
public class MonolitoApplication {

    // Simula un fallo fatal en el módulo de Inventario que afecta a TODO el monolito
    private static boolean moduloInventarioCaido = false;

    public static void main(String[] args) {
        SpringApplication.run(MonolitoApplication.class, args);
    }

    // --- MÓDULO DE INVENTARIO (Dentro del mismo proceso) ---
    @GetMapping("/inventario/stock/{id}")
    public String getStock(@PathVariable String id) {
        if (moduloInventarioCaido) {
            throw new RuntimeException("Fallo fatal en el módulo de Inventario: Base de datos desconectada");
        }
        return "{\"productId\":\"" + id + "\", \"stock\": 10}";
    }

    // --- MÓDULO DE CATÁLOGO (Dentro del mismo proceso) ---
    @GetMapping("/catalogo/product/{id}")
    public String getProduct(@PathVariable String id) {
        if (moduloInventarioCaido) {
            // En un monolito mal aislado, la caída de un módulo crítico 
            // propaga el error y tira toda la aplicación.
            throw new RuntimeException("Error en cascada: El módulo de Inventario está caído, el Catálogo no puede operar.");
        }
        // Llamada local (sin red, instantánea)
        String stock = getStock(id);
        return "{\"product\":\"Producto " + id + "\", \"inventory\":" + stock + "}";
    }

    // --- BOTONES DE CAOS ---
    @GetMapping("/chaos/crash")
    public String crash() {
        moduloInventarioCaido = true;
        return "Módulo de Inventario caído. (En un monolito, esto rompe toda la app)";
    }

    @GetMapping("/chaos/recover")
    public String recover() {
        moduloInventarioCaido = false;
        return "Sistema monolítico recuperado";
    }
}