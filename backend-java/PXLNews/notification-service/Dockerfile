FROM openjdk:17

WORKDIR /app

COPY target/notification-service-0.0.1-SNAPSHOT.jar app.jar
COPY ./wait.sh ./wait.sh
COPY .env .env

RUN chmod +x ./wait.sh

EXPOSE 8084

ENTRYPOINT [ "/bin/bash", "-c", "./wait.sh localhost:8061/actuator/health -- java -jar app.jar" ]
