# Guía de Ejecución con Docker - Backend For Frontend (BFF) V2

Este componente actúa como la capa de orquestación y agregación de datos (**BFF**) del ecosistema **Innovatech**, abstrayendo la complejidad de los microservicios internos del cliente y optimizando las respuestas para el flujo CSR.

## 1. Prerrequisitos y Dependencias
El BFF opera como un puente intermedio dentro de la red interna de Docker:
* **Red de Docker:** `innovatech-net` (requerida para resolver nombres de host).
* **Dependencias de Servicios:** Requiere que el microservicio `ms-gestion-proyectos` esté activo en el puerto interno `8081` para que los clientes Feign (`ProyectoClient`) no fallen por timeout o resolución de DNS.

## 2. Puertos y Mapeo de Red
* **Puerto Interno (Contenedor):** `8080`
* **Puerto Externo (Host):** `8080` (Nota: En producción, este puerto no se expone al host; el tráfico entra exclusivamente redirigido desde el API Gateway en el puerto `8083`).

---

## 3. Comandos de Operación

### Ejecución Aislada (Construcción Limpia)
Para levantar el BFF reconstruyendo sus capas de dependencias Maven:
```bash
docker compose up -d --build innovatech-bff