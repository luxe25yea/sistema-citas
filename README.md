# Sistema Distribuido de Reservas y Notificación de Citas

Proyecto de la materia **Programación de Servicios Web**. Sistema de gestión de citas
médicas implementado como una arquitectura de microservicios con Spring Boot y Spring Cloud.

## Arquitectura

```
Cliente (Postman)
     |
     v
appointment-service :8082 -----> notification-service :8083
     |            (notifica)
     | (valida paciente)
     v
patient-service :8081
     |
     v
Base de datos propia

Todos se registran en:
eureka-server :8761
```

## Servicios

| Servicio            | Puerto | Responsabilidad                        |
|---------------------|--------|-----------------------------------------|
| `eureka-server`     | 8761   | Registro y descubrimiento de servicios |
| `patient-service`   | 8081   | Registro y consulta de pacientes       |
| `appointment-service` | 8082 | Reserva de citas y agenda              |
| `notification-service` | 8083 | Registro de notificaciones             |

## Stack

| Tecnologia              | Version        |
|-------------------------|----------------|
| Java                    | 17 LTS         |
| Spring Boot             | 4.1.1          |
| Spring Cloud            | 2025.1.3       |
| Spring Data JPA + H2    | (en memoria)   |
| Build                   | Maven Wrapper  |

## Como ejecutar

### Con Docker (recomendado)

```powershell
docker compose up --build
```

Levanta `patient-service` en `http://localhost:8081`. El healthcheck del contenedor
espera a que `GET /patients` responda antes de marcarlo como `healthy`.

Para ver el estado y los logs:

```powershell
docker compose ps
docker compose logs -f patient-service
```

Para detener:

```powershell
docker compose down
```

### Con Maven

Cada servicio se compila y se levanta por separado.

```bash
# patient-service
cd patient-service
./mvnw spring-boot:run
```

En Windows:

```powershell
cd patient-service
.\mvnw.cmd spring-boot:run
```

Endpoints disponibles:

- `GET http://localhost:8081/patients`
- `GET http://localhost:8081/patients/{id}`
- Consola H2: `http://localhost:8081/h2-console` (JDBC URL `jdbc:h2:mem:patients`, user `sa`, sin password)

## Docker

Cada servicio lleva un `Dockerfile` multi-stage:

| Etapa   | Imagen                    | Que hace                                    |
|---------|---------------------------|---------------------------------------------|
| `build` | `eclipse-temurin:17-jdk`  | `./mvnw package -DskipTests`                |
| runtime | `eclipse-temurin:17-jre`  | Corre el jar como usuario no-root `spring`  |

Se usa el Maven Wrapper del repo, no una imagen de Maven, para que la version de
Maven sea la misma que en local. Hay un `.dockerignore` que excluye `target/` para
no pisar el jar construido con artefactos de la build local.

## Estado

- [x] Sesion 1 - patient-service: entidad, repositorio, consulta
- [x] Sesion 2 - H2 y persistencia
- [ ] Sesion 3 - POST /patients
- [ ] Sesion 4 - segundo servicio y regla de conflicto
- [ ] Sesion 5 - Eureka server y registro de servicios
- [ ] Sesion 6 - comunicacion entre servicios
- [ ] Sesion 7 - notification-service
- [x] Sesion 8 - Docker

## Nota sobre H2 en memoria

Cada servicio usa su propia base de datos en memoria. Los datos se pierden al detener
el proceso: es intencional, para que el sistema arranque sin dependencias externas.
