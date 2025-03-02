FROM openjdk:21
LABEL authors="egorm"

VOLUME /tmp
WORKDIR /app
ADD maven/user-service-0.0.1-SNAPSHOT.jar /app/user-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "user-service.jar"]