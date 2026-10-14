# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B -q -DskipTests dependency:go-offline

COPY src src
RUN ./mvnw -B -q -DskipTests package \
    && cp target/*.jar app.jar

FROM eclipse-temurin:17-jre

RUN useradd --system --shell /usr/sbin/nologin spring
USER spring

WORKDIR /home/spring
COPY --from=build --chown=spring:spring /app/app.jar ./app.jar

EXPOSE 8081

HEALTHCHECK --interval=15s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -fsS http://localhost:8081/patients > /dev/null || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
