FROM openjdk:17-jdk-alpine
USER root
WORKDIR /app

RUN mkdir -p /app/config

COPY target/config-service-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/config /app/config

EXPOSE 8088

ENTRYPOINT ["java", "-jar", "app.jar"]
