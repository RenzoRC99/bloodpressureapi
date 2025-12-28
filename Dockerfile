# Stage 1: Build
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test --no-daemon

# Stage 2: Run
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/build/libs/bloodpressureapi-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto de la API
EXPOSE 8080

# Ejecuta la app Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
