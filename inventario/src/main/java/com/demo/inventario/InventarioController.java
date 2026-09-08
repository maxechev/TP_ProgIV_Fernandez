package com.demo.inventario;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventarioController {
    
    // Variables globales para la demo (control de caos)
    public static int globalDelay = 0;
    public static boolean isDead = false;
    
    @GetMapping("/stock/{productId}")
    public String getStock(@PathVariable String productId) throws InterruptedException {
        // Simular latencia (para demo de Circuit Breaker)
        if (globalDelay > 0) {
            Thread.sleep(globalDelay);
        }
        
        // Simular caída (para demo de Monolito vs Microservicio)
        if (isDead) {
            throw new RuntimeException("Database connection failed");
        }
        
        return "{\"productId\":\"" + productId + "\", \"stock\": 10}";
    }
    
    // ENDPOINTS DE CAOS (para la demo)
    @GetMapping("/chaos/delay")
    public String setDelay(@RequestParam int ms) {
        globalDelay = ms;
        return "Delay configurado a " + ms + "ms";
    }
    
    @GetMapping("/chaos/crash")
    public String crash() {
        isDead = true;
        return "Inventario está simulando estar caído";
    }
    
    @GetMapping("/chaos/recover")
    public String recover() {
        isDead = false;
        globalDelay = 0;
        return "Inventario recuperado";
    }
}