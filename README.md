# Innovatech BFF (Backend For Frontend)

Este documento describe el módulo `innovatech-bff` del proyecto Innovatech. Explica de forma clara y didáctica su arquitectura, organización y el flujo de ejecución local.

## ¿Qué es este módulo?

`innovatech-bff` actúa como una capa Backend For Frontend intermedia encargada de:

* **Orquestar y agregar llamadas** hacia los microservicios core del backend (Gestión de Proyectos, Gestión de Recursos y Monitoreo/Analítica).
* **Adaptar y transformar datos** para acoplarlos estrictamente a las necesidades de consumo de la interfaz de usuario, resolviendo la unificación de DTOs en un único punto.
* **Propagar el contexto de identidad**, interceptando peticiones autenticadas vía tokens JWT en el perímetro para inyectar cabeceras estructuradas a los servicios internos.

## Principales responsabilidades y comportamientos

* **Agregación de datos:** Consolida respuestas complejas cruzando entidades (ej: asociar IDs de proyectos con detalles atómicos de recursos).
* **Clientes HTTP declarativos:** Consume servicios internos abstrayendo la capa de red mediante OpenFeign.
* **Seguridad perimetral:** Valida firmas digitales estructuradas bajo la biblioteca JWT (`jjwt`).
* **Pruebas de alta cobertura:** Implementa una suite de testing unitario y de integración vía MockMvc aislando llamadas de red mediante el uso de `@MockBean`.

## Estructura general del módulo

Estructura de directorios base del proyecto:

* `src/main/java/.../controller/` - Controladores REST expuestos directamente al cliente web.
* `src/main/java/.../client/` - Interfaces de OpenFeign parametrizadas para intercomunicación.
* `src/main/java/.../config/` - Clases de configuración del contexto de Spring (Seguridad, Interceptores Feign).
* `src/main/java/.../dto/` - Objetos de transferencia de datos con validaciones integradas de Jakarta.
* `src/test/java/.../` - Suite de pruebas unitarias y de integración.

## Endpoints principales

La API base del componente BFF se encuentra expuesta bajo el prefijo unificado `/api/v2/bff/`:

* **GET `/api/v2/bff/proyectos`** * *Descripción:* Recupera el listado global de proyectos.
* **GET `/api/v2/bff/proyectos/{id}/detalles`** * *Descripción:* Orquesta la búsqueda del proyecto y unifica de manera síncrona los detalles de los recursos asignados al mismo.
* **PUT `/api/v2/bff/proyectos/{id}/estado`** * *Descripción:* Modifica la fase actual del proyecto aplicando payloads JSON `{"estado": "NUEVO_ESTADO"}`.
* **POST `/api/v2/bff/monitoreo/eventos`** * *Descripción:* Recibe trazas analíticas del cliente y las despacha al microservicio de auditoría.

## Clientes Feign y Aislamiento

Las peticiones HTTP hacia microservicios externos usan configuraciones centralizadas en `FeignClientConfig` para inyectar de forma transparente el token `Authorization` entrante y las cabeceras `X-User-Id` / `X-User-Roles`. En entornos de testing, estos clientes se interceptan mediante mocks para asegurar el determinismo de las pruebas.

## Requisitos y compatibilidad

* **Java / JDK:** Compatible con entornos de ejecución modernos bajo **Java 24 y Java 25** (compilación multi-stage optimizada en entornos contenerizados).
* **Spring Boot:** 3.4.3
* **JaCoCo:** 0.8.15 (Soporte nativo de instrumentación de bytecode para versiones de releases modernas).

## Cómo compilar y ejecutar localmente

### Requisitos previos
* JDK 24 o superior instalado localmente.
* Maven 3.8 o superior configurado.

### Comandos útiles

# Ejecución y Uso del Proyecto

## Compilar y verificar suite completa de pruebas

```bash
mvn clean verify
```

## Compilar omitiendo la fase de pruebas

```bash
mvn clean package -DskipTests
```

## Ejecutar la aplicación Spring Boot localmente

```bash
mvn spring-boot:run
```

## Ejecutar el BFF apuntando a servicios específicos

Para instanciar el BFF utilizando direcciones específicas para los microservicios backend:

```bash
mvn spring-boot:run -DPROYECTOS_URL=http://localhost:8081 -DRECURSOS_URL=http://localhost:8086
```

---

# Ejemplos de Uso

## Listar proyectos existentes

```bash
curl -H "Authorization: Bearer <token_jwt>" \
http://localhost:8080/api/v2/bff/proyectos
```

## Emitir traza analítica al servicio de monitoreo

```bash
curl -X POST \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer <token_jwt>" \
     -d '{
           "tipoEvento":"CLICK",
           "componente":"BffProyectoController",
           "descripcion":"Clic en guardar",
           "duracionMs":15,
           "usuarioId":99
         }' \
     http://localhost:8080/api/v2/bff/monitoreo/eventos
```

---

# Troubleshooting

## Error de timeouts de red o conectividad durante el arranque local

**Posible causa:** Las URLs de los microservicios backend no están configuradas correctamente.

**Solución:**

- Verifique que las variables de entorno asociadas a las URLs de los microservicios estén correctamente definidas.
- Si corresponde, deshabilite el descubrimiento dinámico mediante:

```properties
eureka.client.enabled=false
```

## Excepciones de validación (400 Bad Request) inesperadas

**Posible causa:** Los datos enviados no cumplen con las restricciones definidas en los DTOs.

**Solución:**

- Verifique que los campos anotados con `@NotBlank` contengan valores válidos.
- Revise que las restricciones de tamaño definidas mediante `@Size` sean respetadas.
- Confirme que la estructura del payload coincida con la esperada por la API.