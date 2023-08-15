FROM openjdk:11-jdk-buster

RUN chmod +x mvnw

RUN ./mvnw clean package

WORKDIR /app

COPY target/hfms.jar .

ENTRYPOINT ["java", "-jar", "hfms.jar"]