# Stage 1: Сборка
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Запуск
FROM openjdk:21
WORKDIR /app

COPY --from=build /app/target/*.jar /app/webservice.jar

ENTRYPOINT ["java", "-jar", "webservice.jar"]
