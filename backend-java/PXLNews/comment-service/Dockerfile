FROM openjdk:17

WORKDIR /app

COPY target/comment-service-0.0.1-SNAPSHOT.jar app.jar
COPY ./wait.sh ./wait.sh

RUN chmod +x ./wait.sh

EXPOSE 8083

ENTRYPOINT [ "/bin/bash", "-c", "./wait.sh localhost:8061/actuator/health -- java -jar app.jar" ]
