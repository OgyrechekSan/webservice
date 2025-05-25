# Этап сборки: используем Maven и JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Копируем исходники и зависимости
COPY pom.xml .
COPY src ./src

# Собираем jar без тестов
RUN mvn clean package -DskipTests

# Этап запуска: используем только JDK 21
FROM openjdk:21
WORKDIR /app

# Копируем собранный jar из предыдущей стадии
COPY --from=build /app/target/webservice.jar ./webservice.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "webservice.jar"]
