FROM openjdk:21
EXPOSE 8080

WORKDIR /app

COPY webservice.jar /app/webservice.jar

ENTRYPOINT ["java","-jar","webservice.jar"]