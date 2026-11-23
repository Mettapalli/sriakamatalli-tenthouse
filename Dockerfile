FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY backend/pom.xml backend/pom.xml
COPY backend/src backend/src
COPY frontend/ backend/src/main/resources/static/
WORKDIR /app/backend
RUN mvn clean package -DskipTests
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN mkdir -p /app/uploads
COPY --from=build /app/backend/target/tent-house-management-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xmx350m", "-Xms128m", "-Dspring.profiles.active=prod", "-jar", "/app/app.jar"]