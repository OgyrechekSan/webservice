FROM openjdk:21

WORKDIR /app

COPY webservice.jar /app/webservice.jar

ENTRYPOINT ["java", "-jar", "webservice.jar"]

