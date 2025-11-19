# Proyecto Spring Boot con Java 21

Proyecto base con estructura estándar para aplicaciones Spring Boot.

## Estructura del proyecto

```
src/
├── main/
│   ├── java/com/ejemplo/
│   │   ├── controller/     # Controladores REST
│   │   ├── service/        # Lógica de negocio
│   │   ├── repository/     # Acceso a datos
│   │   ├── model/          # Entidades JPA
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── config/         # Configuraciones
│   │   ├── exception/      # Excepciones personalizadas
│   │   └── Application.java # Clase principal
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/ejemplo/   # Tests unitarios
```

## Requisitos

- Java 21
- Maven 3.6+

## Ejecución

```bash
mvn spring-boot:run
```

## Compilación

```bash
mvn clean package
```

## Base de datos

Por defecto se usa H2 en memoria. Para acceder a la consola:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Usuario: `sa`
- Contraseña: (vacía)
