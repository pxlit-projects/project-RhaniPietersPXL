FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/config-service-0.0.1-SNAPSHOT.jar app.jar

COPY src/main/resources/config /app/config

EXPOSE 8088

ENTRYPOINT ["java", "-jar", "app.jar"]
