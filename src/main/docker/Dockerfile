FROM openjdk:21
LABEL authors="egorm"

VOLUME /tmp
WORKDIR /app
ADD maven/product_service-0.0.3-SNAPSHOT.jar /app/productservice.jar
EXPOSE 7070
ENTRYPOINT ["java", "-jar", "productservice.jar"]