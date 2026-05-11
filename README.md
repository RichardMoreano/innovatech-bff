# Innovatech BFF (Backend For Frontend)

Microservicio encargado de orquestar las peticiones entre el Frontend y los microservicios de backend.

## Funcionalidades
- Agregación de datos (Proyecto + Recursos)
- Adaptación de respuestas para el Frontend
- Centralización de autenticación JWT

## Endpoints

| Método | Endpoint                          | Descripción |
|--------|-----------------------------------|-----------|
| GET    | `/api/bff/proyectos`              | Listar proyectos |
| GET    | `/api/bff/recursos`               | Listar recursos |
| GET    | `/api/bff/proyectos-con-recursos` | Proyectos con recursos asignados (orquestación) |

## Tecnologías
- Spring Boot 3.3
- RestTemplate
- JWT
- Resilience4j (Circuit Breaker)

## Puertos
- **8080** (BFF)
- Llama a Servicio Producto (8081) y Servicio Recurso (8082)