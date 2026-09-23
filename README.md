# Demo de Resiliencia: Monolitos, Microservicios y Circuit Breaker

Este repositorio contiene la demostración práctica de los patrones de resiliencia (Timeout, Reintentos y Circuit Breaker) en una arquitectura de microservicios.

## Requisitos
- Java 17+
- Maven
- Docker y Docker Compose

## Cómo ejecutar la demostración

1. Compilar ambos proyectos:
   ```bash
   cd catalogo && ./mvnw clean package
   cd ../inventario && ./mvnw clean package
   cd ..